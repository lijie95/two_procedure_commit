package com.lijie.tpc.coo;

import com.google.common.collect.Maps;
import com.lijie.tpc.com.log.LogUtil;
import com.lijie.tpc.com.socket.ObjectClient;
import com.lijie.tpc.com.socket.ObjectServer;
import com.lijie.tpc.com.util.JobQueueProcessor;
import com.lijie.tpc.com.util.ServerInfo;
import com.lijie.tpc.com.util.SocketConst;

import java.io.IOException;
import java.util.Map;

/**
 * lijie2pc on 2015/3/21.
 */

public class Coordinator {

    private static final String COORDINATOR_DT_FILE_NAME = "coordinator.txt";

    public static final int WAITING_RESPONSE_TIME = 1000*3;

    public Map<String, ObjectClient> getServerMap() {
        return serverMap;
    }

    private Map<String, ObjectClient> serverMap = Maps.newHashMap();

    private void init(JobProcessor jobProcessor) throws IOException {
        for(ServerInfo serverInfo : SocketConst.getServerInfoList()){
            TPCMessageHandler messageHandler = new TPCMessageHandler(jobProcessor, serverInfo.name);
            ObjectClient objectClient = new ObjectClient(messageHandler, serverInfo.host, serverInfo.port);
            serverMap.put(serverInfo.name, objectClient);
        }
    }

    public static void main(String args[]) throws IOException {
        LogUtil.init(COORDINATOR_DT_FILE_NAME);

        Coordinator coordinator = new Coordinator();

        JobProcessor jobProcessor = new JobProcessor(coordinator);

        coordinator.init(jobProcessor);

        JobQueueProcessor jobQueueProcessor = new JobQueueProcessor(jobProcessor);
        new Thread(jobQueueProcessor).start();

        ObjectServer objectServer = new ObjectServer(new UserMessageHandler(jobQueueProcessor), SocketConst.COORDINATOR_SERVER_PORT);
        new Thread(objectServer).start();

    }
}
