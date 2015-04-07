package com.lijie.tpc.par.account;

/**
 * lijie2pc on 2015/3/22.
 */
public class Account {

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    private Integer amount;

    public Account(){}

    public Account(int amount) {
        this.amount = amount;
    }
}
