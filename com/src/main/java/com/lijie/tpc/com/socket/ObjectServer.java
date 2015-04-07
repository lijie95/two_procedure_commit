package com.lijie.tpc.com.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * lijie2pc on 2015/3/21.
 */
public class ObjectServer implements Runnable {

    private ServerSocket socketConnection = null;

    private ObjectOutputStream out = null;

    private Thread thread = null;

    private IMessageHandler recieveMessageHandler = null;

    private List<ObjectClient> objectClientList = new ArrayList<ObjectClient>();

    public ObjectServer(IMessageHandler messageHandler, int port) throws IOException {
        recieveMessageHandler = messageHandler;
        this.socketConnection = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {

                Socket scoket = socketConnection.accept();

                ObjectClient objectClient = new ObjectClient(scoket, this.recieveMessageHandler);
                objectClientList.add(objectClient);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        for(ObjectClient objectClient : objectClientList){
            objectClient.stop();
        }
    }

}
