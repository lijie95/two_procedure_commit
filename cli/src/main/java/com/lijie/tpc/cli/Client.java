package com.lijie.tpc.cli;

import com.lijie.tpc.com.socket.IMessageHandler;
import com.lijie.tpc.com.socket.ObjectClient;
import com.lijie.tpc.com.socket.message.UserMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * lijie2pc on 2015/3/21.
 */
public class Client {

    private static ObjectClient objectClient = null;

    private static String server;

    private static String port;

    public static void main(String [] args) throws IOException {

        IMessageHandler messageHandler = new MessageHandler();

        while(true){
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String str;
            System.out.println("Enter your command:");
            str = br.readLine();
            if(!validate(str)){
                return;
            }
            System.out.println("your command is :"+str);

            String commands[] = str.split(" ");
            if(commands.length < 7){
                return;
            }

            UserMessage userMessage = extractUserMessage(commands);

            if(objectClient == null){
                server = commands[5];
                port = commands[6];
                objectClient = new ObjectClient(messageHandler, server, Integer.valueOf(port));
            }else{
                if(!server.equals(commands[5]) || !port.equals(commands[6])){
                    System.out.println("restart the client if you need change the connected server");
                    return;
                }
            }

            objectClient.sendMessage(userMessage);
        }
    }

    private static UserMessage extractUserMessage(String[] commands) {
        UserMessage userMessage = new UserMessage();
        userMessage.setUserName(commands[1]);
        userMessage.setAmmount(Integer.valueOf(commands[2]));
        userMessage.setSourceBank(commands[3]);
        userMessage.setDestBank(commands[4]);
        return userMessage;
    }

    private static boolean validate(String str) {
        if(str.equals("c") || !str.matches("^cli.*")){
            return false;
        }

        return true;
    }
}
