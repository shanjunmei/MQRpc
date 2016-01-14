package com.lanhun.rpc.core;

public interface MessageHandler<REQ, RES> {

    RES handle(REQ req);
}
