package com.advancejava.groupexercise.helper;

import com.advancejava.groupexercise.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class CheckBalance {

    public double isBelowMinimumBalance(Account acct){
        if(acct.getBalance() < acct.getMinimumBalance()){
            return acct.getBalance() - acct.getPenalty();
        }else{
            return acct.getBalance();
        }

    }


}
