/**
 * Copyright (c) 2006-2015 Hzins Ltd. All Rights Reserved. 
 *  
 * This code is the confidential and proprietary information of   
 * Hzins. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Hzins,http://www.hzins.com.
 *  
 */
package com.lanhun.rpc.client;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 
 *
 *
 * </p>
 * 
 * @author hz15101769
 * @date 2015年12月8日 下午6:03:34
 * @version
 */
@Component
public class MessageClient {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public String send(String msg) {
	Object ret = rabbitTemplate.convertSendAndReceive(msg);
	return ret.toString();
    }

}
