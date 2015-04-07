package com.lijie.tpc.cli;

import com.lijie.tpc.com.socket.IMessageHandler;
import com.lijie.tpc.com.socket.message.IMessage;
import com.lijie.tpc.com.socket.ObjectClient;
import com.lijie.tpc.com.socket.message.UserMessage;

/**
 * lijie2pc on 2015/3/21.
 */
public class MessageHandler implements IMessageHandler{
    @Override
    public void handleMessage(IMessage IMessage, ObjectClient objectClient) {
        System.out.println(((UserMessage) IMessage).getResponse());
    }
}
