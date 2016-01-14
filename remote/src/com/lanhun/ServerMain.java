/**
 * Copyright (c) 2006-2015 Hzins Ltd. All Rights Reserved. 
 *  
 * This code is the confidential and proprietary information of   
 * Hzins. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Hzins,http://www.hzins.com.
 *  
 */
package com.lanhun;

import org.springframework.context.support.ClassPathXmlApplicationContext;

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
public class ServerMain {

    public static void main(String[] args) {
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/resources/spring/all.xml");
	context.start();
	/*
	 * MessageClient client = context.getBean(MessageClient.class);
	 * Map<String, Object> r = new HashMap<>(); r.put("serviceId", 2);
	 * r.put("data", "你好"); String ret=client.send("hello");
	 * System.out.println(ret);
	 */

    }
}
