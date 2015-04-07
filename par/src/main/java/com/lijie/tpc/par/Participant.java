package com.lijie.tpc.par;

import com.lijie.tpc.com.log.Log;
import com.lijie.tpc.com.log.LogUtil;
import com.lijie.tpc.com.socket.ObjectServer;
import com.lijie.tpc.com.util.JobQueueProcessor;
import com.lijie.tpc.com.util.ServerInfo;
import com.lijie.tpc.com.util.SocketConst;
import com.lijie.tpc.par.tp.TPRequestMessageHandler;
import com.lijie.tpc.par.tp.TerminateProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * lijie2pc on 2015/3/22.
 */
public class Participant {

    public static final int WAITING_RESPONSE_TIME = 1000 * 5;

    private static final String PARTICIPANT_DT_FILE_NAME = "participant.txt";

    public static String SERVER_NAME;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        System.out.println("Enter server name:");
        str = br.readLine();
        ServerInfo serverInfo = getServerInfo(str);
        if(serverInfo==null){
            System.out.println("Wrong server name:");
            return;
        }

        SERVER_NAME=serverInfo.name;
        LogUtil.init(SERVER_NAME + "_" + PARTICIPANT_DT_FILE_NAME);

        JobProcessor jobProcessor = new JobProcessor();
        JobQueueProcessor<CoordinatorRequest> coordinatorRequestJobQueueProcessor = new JobQueueProcessor<CoordinatorRequest>(jobProcessor);
        new Thread(coordinatorRequestJobQueueProcessor).start();

        ObjectServer objectServer = new ObjectServer(new ParticipantMessageHandler(coordinatorRequestJobQueueProcessor, jobProcessor), serverInfo.port);
        new Thread(objectServer).start();

        //TPServer
        ObjectServer tpObjectServer = new ObjectServer(new TPRequestMessageHandler(jobProcessor), serverInfo.tpPort);
        new Thread(tpObjectServer).start();

        List<Log> logList = LogUtil.getInstance().getVoteYesButNoCommitOrAbort();
        for(Log log : logList){
            TerminateProtocol.proccessTP(log.getMemo());
        }
    }

    private static ServerInfo getServerInfo(String str) {
        if(str.trim().equals(SocketConst.BOC_SERVER_NAME)) {
            return SocketConst.bocServerInfo;
        }
        else if(str.trim().equals(SocketConst.CCB_SERVER_NAME)){
            return SocketConst.ccbServerInfo;
        }else if(str.trim().equals(SocketConst.CBRC_SERVER_NAME)){
            return SocketConst.cbrcServerInfo;
        }
        return null;
    }
}
