package com.lijie.tpc.com.socket.message;

/**
 * lijie2pc on 2015/3/21.
 */
public class UserMessage implements IMessage {

    private String userName;

    private Integer ammount;

    private String sourceBank;

    private String destBank;

    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAmmount() {
        return ammount;
    }

    public void setAmmount(Integer ammount) {
        this.ammount = ammount;
    }

    public String getSourceBank() {
        return sourceBank;
    }

    public void setSourceBank(String sourceBank) {
        this.sourceBank = sourceBank;
    }

    public String getDestBank() {
        return destBank;
    }

    public void setDestBank(String destBank) {
        this.destBank = destBank;
    }


}
