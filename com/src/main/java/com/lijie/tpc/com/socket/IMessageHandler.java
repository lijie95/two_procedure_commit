package com.lijie.tpc.com.socket;

import com.lijie.tpc.com.socket.message.IMessage;

/**
 * lijie2pc on 2015/3/21.
 */
public interface IMessageHandler {

    void handleMessage(IMessage IMessage, ObjectClient objectClient);
}
