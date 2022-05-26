package com.advancejava.groupexercise1.helper;

import com.advancejava.groupexercise1.entity.Account;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;

@Component
public class CheckAccountTypeImpl implements CheckAccountType{
    @Override
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
