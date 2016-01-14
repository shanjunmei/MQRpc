package com.lanhun.client.rpc.core;

public interface MessageHandler<REQ, RES> {

    RES handle(REQ req);
}
