package com.lijie.tpc.par.tp;

import com.google.common.collect.Lists;
import com.lijie.tpc.com.log.LogUtil;
import com.lijie.tpc.com.socket.ObjectClient;
import com.lijie.tpc.com.socket.message.TPCMessage;
import com.lijie.tpc.com.util.ServerInfo;
import com.lijie.tpc.par.Participant;

import java.io.IOException;
import java.util.List;
import java.util.Queue;

/**
 * lijie2pc on 2015/3/23.
 */
public class TerminateProtocol {

    public static void proccessTP(final TPCMessage tpcMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Queue<ServerInfo> serverInfoQueue = Lists.newLinkedList();
                serverInfoQueue.addAll(tpcMessage.getServerList());

                //循环所有的participants 看是否其他的participants有接到coordinator的回复
                //如果连接不上则重新连接，直到已询问了所有的participants或者log中VoteYesButNoCommitOrAbort的transaction已解决
                while (serverInfoQueue.size() != 0  && LogUtil.getInstance().getVoteYesButNoCommitOrAbort().size() != 0){
                    ServerInfo serverInfo = serverInfoQueue.poll();
                    if(serverInfo.name.equals(Participant.SERVER_NAME)){
                        continue;
                    }
                    try {
                        ObjectClient objectClient = new ObjectClient(new TPResponseMessageHandler(), serverInfo.host, serverInfo.tpPort);
                        TPMessage tpMessage = new TPMessage();
                        tpMessage.setTransactionId(tpcMessage.getTransactionId());
                        tpMessage.setOperation(TPMessage.REQUEST_OPT);
                        objectClient.sendMessage(tpMessage);

                    } catch (IOException e) {
                        e.printStackTrace();
                        serverInfoQueue.offer(serverInfo);
                    }
                }
            }
        }).start();
    }
}
