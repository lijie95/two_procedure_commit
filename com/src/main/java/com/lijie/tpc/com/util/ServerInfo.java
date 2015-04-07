package com.lijie.tpc.com.util;

import java.io.Serializable;

/**
 * lijie2pc on 2015/3/21.
 */
public class ServerInfo implements Serializable {

    public String name;

    public String host;

    public int port;

    public int tpPort;

    public ServerInfo(String name, String host, int port, int tpPort) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.tpPort = tpPort;
    }

    public ServerInfo(){}
}
