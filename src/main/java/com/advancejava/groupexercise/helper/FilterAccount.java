package com.advancejava.groupexercise.helper;

import com.advancejava.groupexercise.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class FilterAccount {
    
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
            case "interest":
                account.setBalance(account.getBalance());
                break;
            default:
                throw new RuntimeException("Not Found");
        }
        return account;
    }

    public double isBelowMinimumBalance(Account acct){
        if(acct.getBalance() < acct.getMinimumBalance()){
            return acct.getBalance() - acct.getPenalty();
        }else{
            return acct.getBalance();
        }
    }
}
