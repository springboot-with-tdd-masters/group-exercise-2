package com.advancejava.groupexercise.helper;

import com.advancejava.groupexercise.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class CheckAccountType{
    
    public Account checkAccountType(Account account) {
        String acctType = account.getType();

        switch(acctType){
            case "regular":
                //10 below minimum

                break;
            case "checking":
                //1 per txn
                account.setBalance(account.getBalance() - account.getTransactionCharge());
                //10 below minimum
                break;
            default:
                throw new RuntimeException("Not Found");
        }
        return account;
    }
}
