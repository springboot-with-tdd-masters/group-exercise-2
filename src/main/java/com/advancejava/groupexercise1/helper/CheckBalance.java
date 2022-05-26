package com.advancejava.groupexercise1.helper;

import com.advancejava.groupexercise1.entity.Account;
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
