package com.group3.exercise.bankapp.services.transaction.impl;

import com.group3.exercise.bankapp.entities.Account;
import com.group3.exercise.bankapp.entities.CheckingAccount;
import com.group3.exercise.bankapp.entities.InterestAccount;
import com.group3.exercise.bankapp.entities.RegularAccount;
import com.group3.exercise.bankapp.exceptions.BankAppException;
import com.group3.exercise.bankapp.exceptions.BankAppExceptionCode;
import com.group3.exercise.bankapp.services.transaction.TransactionStrategy;
import com.group3.exercise.bankapp.services.transaction.TransactionStrategyNavigator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TransactionStrategyNavigatorImpl implements TransactionStrategyNavigator {

    private final Map<Class<? extends Account>, TransactionStrategy> txnLookup;
    private final Map<String, TransactionStrategy> txnTypeLookup;

    public TransactionStrategyNavigatorImpl(
    		RegularTransactionStrategy regularTransactionStrategy,
            InterestTransactionStrategy interestTransactionStrategy,
            CheckingTransactionStrategy checkingTransactionStrategy
    ){
        txnLookup = new HashMap<>();
        txnTypeLookup = new HashMap<>();
        txnLookup.put(RegularAccount.class, regularTransactionStrategy);
        txnLookup.put(InterestAccount.class, interestTransactionStrategy);
        txnLookup.put(CheckingAccount.class, checkingTransactionStrategy);
        txnTypeLookup.put(RegularAccount.TYPE, regularTransactionStrategy);
        txnTypeLookup.put(InterestAccount.TYPE, interestTransactionStrategy);
        txnTypeLookup.put(CheckingAccount.TYPE, checkingTransactionStrategy);
    }

    @Override
    public Account generateNewAccountDetails(String name, String acctNumber, String type) {
        return Optional.ofNullable(txnTypeLookup.get(type))
                .map(txn -> txn.generateNewAccountDetails(name, acctNumber))
                .orElseThrow(() -> new BankAppException(BankAppExceptionCode.BAD_REQUEST));
    }

    @Override
    public Account withdraw(Account account, Double amount) {
       return Optional.ofNullable(txnLookup.get(account.getClass()))
                .map(txn -> txn.withdraw(account, amount))
               .orElseThrow(() -> new BankAppException(BankAppExceptionCode.BAD_REQUEST));
    }

    @Override
    public Account deposit(Account account, Double amount) {
        return Optional.ofNullable(txnLookup.get(account.getClass()))
                .map(txn -> txn.deposit(account, amount))
                .orElseThrow(() -> new BankAppException(BankAppExceptionCode.BAD_REQUEST));
    }
}
