package com.lijie.tpc.par.account;

import com.lijie.tpc.com.json.AbstractJsonFile;

import java.io.File;
import java.util.List;

/**
 * lijie2pc on 2015/3/22.
 */
public class AccountManager extends AbstractJsonFile<Account> {

    public AccountManager(File jsonFile) {
        super(jsonFile);
    }

    public Account getAccount(){
        List<Account> accountList = getObjectList();
        if(accountList.size()>0){
            return accountList.get(0);
        }else{
            return new Account(0);
        }
    }

    public void setAccount(Account account) {
        getObjectList().clear();
        addAndWrite(account);
    }
}
