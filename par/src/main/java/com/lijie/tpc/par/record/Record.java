package com.lijie.tpc.par.record;

/**
 * lijie2pc on 2015/3/22.
 */
public class Record {

    private String userName;

    private int amount;

    private String sourceBankName;

    public Record(String userName, int amount, String sourceBankName, String destBankName) {
        this.userName = userName;
        this.amount = amount;
        this.sourceBankName = sourceBankName;
        this.destBankName = destBankName;
    }

    public Record(){}

    public String getDestBankName() {

        return destBankName;
    }

    public void setDestBankName(String destBankName) {
        this.destBankName = destBankName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSourceBankName() {
        return sourceBankName;
    }

    public void setSourceBankName(String sourceBankName) {
        this.sourceBankName = sourceBankName;
    }

    private String destBankName;

}
