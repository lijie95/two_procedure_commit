package com.lijie.tpc.com.socket;

import com.lijie.tpc.com.socket.message.IMessage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * lijie2pc on 2015/3/21.
 */
public class ObjectClient {

    private Socket socket = null;

    private ObjectOutputStream out = null;

    private Thread thread = null;

    private IMessageHandler recieveMessageHandler = null;

    public ObjectClient(IMessageHandler messageHandler,String server, int port) throws IOException {
        this(new Socket(server, port), messageHandler);
    }

    public ObjectClient(Socket socket, IMessageHandler messageHandler) throws IOException {
        recieveMessageHandler = messageHandler;
        this.socket = socket;

        out = new ObjectOutputStream(socket.getOutputStream());

        thread = new Thread(new ReadHandlerThread(socket, messageHandler, this));
        thread.start();
    }

    public void sendMessage(IMessage IMessage) throws IOException {
        out.writeObject(IMessage);
        out.flush();
    }

    public void stop(){
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        thread.interrupt();
    }

    private class ReadHandlerThread implements Runnable {
        private Socket socket;
        private IMessageHandler messageHandler;
        private ObjectClient objectClient;

        public ReadHandlerThread(Socket socket, IMessageHandler messageHandler, ObjectClient objectClient) {
            this.socket = socket;
            this.messageHandler = messageHandler;
            this.objectClient = objectClient;
        }

        @Override
        public void run() {
            ObjectInputStream oin = null;
            try {
                oin = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                while (!Thread.currentThread().isInterrupted()) {
                    IMessage IMessage = (IMessage) oin.readObject();
                    messageHandler.handleMessage(IMessage, objectClient);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }finally{
                try {
                    if(oin != null){
                        oin.close();
                    }
                    if(socket != null){
                        socket = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
