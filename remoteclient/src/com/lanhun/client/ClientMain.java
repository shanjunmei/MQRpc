/**
 * Copyright (c) 2006-2015 Hzins Ltd. All Rights Reserved. 
 *  
 * This code is the confidential and proprietary information of   
 * Hzins. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Hzins,http://www.hzins.com.
 *  
 */
package com.lanhun.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanhun.client.rpc.client.MessageClient;

/**
 * <p>
 * 
 *
 *
 * </p>
 * 
 * @author hz15101769
 * @date 2015年12月8日 下午5:41:12
 * @version
 */
public class ClientMain {

    public static void main(String[] args) {

	Logger logger = LogManager.getLogger();
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("resources/spring/all.xml");
	context.start();
	MessageClient client = context.getBean(MessageClient.class);
	Map<String, Object> r = new HashMap<>();
	r.put("serviceId", 2);
	r.put("data", "你好");
	ObjectMapper objectMapper = new ObjectMapper();
	String ret = "";
	long t = System.currentTimeMillis();

	try {

	    ret = client.send(objectMapper.writeValueAsString(r));
	    logger.info(ret);

	} catch (JsonProcessingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	t = System.currentTimeMillis() - t;
	logger.info("take :" + t + " ms");
    }
}
