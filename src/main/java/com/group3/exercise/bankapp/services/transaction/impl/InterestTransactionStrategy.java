package com.group3.exercise.bankapp.services.transaction.impl;

import com.group3.exercise.bankapp.entities.InterestAccount;
import com.group3.exercise.bankapp.services.transaction.TransactionStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InterestTransactionStrategy implements TransactionStrategy<InterestAccount> {

    private final Double interest;
    private final Double minimumBalance;

    public InterestTransactionStrategy(
            @Value("${interest.charge:.03}") Double interest,
            @Value("${interest.minimumBal}") Double minimumBalance){
        this.interest = interest;
        this.minimumBalance = minimumBalance;
    }

    @Override
    public InterestAccount generateNewAccountDetails(String name, String acctNumber) {
        // TODO generate Account entity here.
        InterestAccount account = new InterestAccount();
        account.setInterestCharge(interest);
        account.setMinimumBalance(minimumBalance);
        return account;
    }

    @Override
    public InterestAccount withdraw(InterestAccount account, Double amount) {
        // TODO update account entity with business rules
        Double currentBalance = account.getBalance();
        Double updated = currentBalance - amount;
        account.setBalance(updated);
        return account;
    }

    @Override
    public InterestAccount deposit(InterestAccount account, Double amount) {
        // TODO update account entity with business rules
        Double currentBalance = account.getBalance();
        Double updated = currentBalance + amount;
        account.setBalance(updated);
        return account;
    }
}
