package com.lijie.tpc.com.util;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * lijie2pc on 2015/3/21.
 */
public class SocketConst {
    public static final int COORDINATOR_SERVER_PORT = 8888;
    public static final int BOC_SERVER_PORT = 8887;
    public static final int CCB_SERVER_PORT = 8886;
    public static final int CBRC_SERVER_PORT = 8885;

    public static final int BOC_SERVER_TP_PORT = 8883;
    public static final int CCB_SERVER_TP_PORT = 8882;
    public static final int CBRC_SERVER_TP_PORT = 8881;

    public static final String CBRC_SERVER_NAME = "cbrc";
    public static final String BOC_SERVER_NAME = "boc";
    public static final String CCB_SERVER_NAME = "ccb";


    public static final ServerInfo bocServerInfo = new ServerInfo(SocketConst.BOC_SERVER_NAME, "localhost", SocketConst.BOC_SERVER_PORT, BOC_SERVER_TP_PORT);
    public static final ServerInfo ccbServerInfo = new ServerInfo(SocketConst.CCB_SERVER_NAME, "localhost", SocketConst.CCB_SERVER_PORT, CCB_SERVER_TP_PORT);
    public static final ServerInfo cbrcServerInfo = new ServerInfo(SocketConst.CBRC_SERVER_NAME, "localhost", SocketConst.CBRC_SERVER_PORT, CBRC_SERVER_TP_PORT);

    private static List<ServerInfo> serverInfoList = Lists.newArrayList();
    public static List<ServerInfo> getServerInfoList() {
        if(serverInfoList.size()==0){
            serverInfoList.add(bocServerInfo);
            serverInfoList.add(ccbServerInfo);
            serverInfoList.add(cbrcServerInfo);
        }
        return serverInfoList;
    }

}
