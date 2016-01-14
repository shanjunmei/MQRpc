/**
 * Copyright (c) 2006-2015 Hzins Ltd. All Rights Reserved. 
 *  
 * This code is the confidential and proprietary information of   
 * Hzins. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Hzins,http://www.hzins.com.
 *  
 */
package com.lanhun.client.rpc;

import com.lanhun.client.rpc.core.MessageHandler;
import com.lanhun.client.rpc.core.annotation.Handler;

/**
 * <p>
 * 
 *
 *
 * </p>
 * 
 * @author hz15101769
 * @date 2015年12月8日 下午6:15:28
 * @version
 */
@Handler(2)
public class RetHandler implements MessageHandler<String, String> {

    @Override
    public String handle(String req) {

	System.out.println(req);

	return "success";
    }

}
