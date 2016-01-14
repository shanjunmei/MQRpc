package com.lanhun.rpc.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanhun.common.utils.ReflectUtils;
import com.lanhun.rpc.core.annotation.Handler;

/**
 * <p>
 * RabbitMQ消息队列监听
 * </p>
 * 
 * @author hz15051252
 * @date 2015年6月4日 上午11:18:41
 */
public class MessageRemoteListener
	implements MessageListener, ApplicationContextAware {

    private final static Logger logger = LogManager.getLogger();

    @Resource
    private TaskExecutor taskExecutor;

    @Resource
    private RabbitTemplate rabbitTemplate;

    private String hzMessageQueue;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<Integer, MessageHandler<?, ?>> messageHandlerMap = new ConcurrentHashMap<Integer, MessageHandler<?, ?>>();

    public void setHzMessageQueue(String hzMessageQueue) {
	this.hzMessageQueue = hzMessageQueue;
    }

    @Override
    public void onMessage(final Message message) {

	taskExecutor.execute(new Runnable() {

	    @SuppressWarnings({ "unchecked", "rawtypes" })
	    @Override
	    public void run() {

		String json = new String(message.getBody());
		Map<String, Object> headers = message.getMessageProperties().getHeaders();
		if (headers != null) {
		    List<Map<String, String>> xdeath = (List<Map<String, String>>) headers.get("x-death");
		    if (xdeath != null && xdeath.size() > 0) {
			String name = xdeath.get(0).get("queue");
			if (!name.equals(hzMessageQueue)) {
			    return;
			}
		    }
		}

		try {
		    Request request = objectMapper.readValue(json, Request.class);
		    MessageHandler messageHandler = messageHandlerMap.get(request.getServiceId());
		    Object ret = messageHandler.handle(request.getData());
		    if (ret != null) {
			rabbitTemplate.convertAndSend(message.getMessageProperties().getReplyToAddress().getExchangeName(), message.getMessageProperties().getReplyToAddress().getRoutingKey(), ret);
		    }
		} catch (JsonParseException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (JsonMappingException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

	    }
	});
    }

    /**
     * 实现ApplicationContextAware 接口，用户动态注册消息处理器
     * 
     * @see Handler
     * @see Handler
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Component.class);// 用compoment
											       // 注解可以获取所有实例，包括动态代理
											       // 其它自定义注解获取不到
	if (beans != null) {
	    for (Entry<String, Object> e : beans.entrySet()) {
		try {
		    Class<?> valid = ReflectUtils.fetchValidClass(e.getValue());// 动态代理上获取不到注解，需获取原类
		    com.lanhun.rpc.core.annotation.Handler handler = valid.getAnnotation(Handler.class);
		    if (handler == null) {
			continue;// 非MessageHandler注解不处理
		    }
		    if (!MessageHandler.class.isAssignableFrom(valid)) {
			throw new RuntimeException("MessageHandler[" + valid + "] must implement interface[" + MessageHandler.class + "]");
		    }
		    if (!messageHandlerMap.containsKey(handler.value())) {
			messageHandlerMap.put(handler.value(), (MessageHandler<?, ?>) e.getValue());
		    } else {
			throw new RuntimeException("@MessageHandler :" + handler.value() + " repeat");
		    }
		} catch (ClassNotFoundException e1) {
		    throw new RuntimeException(e1);
		}
	    }
	}

	for (Entry<Integer, MessageHandler<?, ?>> e : messageHandlerMap.entrySet()) {
	    System.out.println("MessageHandler [" + e.getValue().getClass() + "] registed ,key " + e.getKey());
	}

    }

}
