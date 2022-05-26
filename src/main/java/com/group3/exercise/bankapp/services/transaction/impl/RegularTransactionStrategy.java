package com.group3.exercise.bankapp.services.transaction.impl;

import com.group3.exercise.bankapp.entities.RegularAccount;
import com.group3.exercise.bankapp.services.transaction.TransactionStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RegularTransactionStrategy implements TransactionStrategy<RegularAccount> {

    private final Double interest;
    private final Double minimumBalance;
    private final Double penalty;

    public RegularTransactionStrategy(
            @Value("${regular.charge}") Double interest,
            @Value("${regular.minimumBal}") Double minimumBalance,
            @Value("${regular.penalty}") Double penalty) {
        this.interest = interest;
        this.minimumBalance = minimumBalance;
        this.penalty = penalty;
    }

    @Override
    public RegularAccount generateNewAccountDetails(String name, String acctNumber) {

        RegularAccount regularAccount = new RegularAccount(name, acctNumber);
        regularAccount.setInterestCharge(interest);
        regularAccount.setMinimumBalance(minimumBalance);
        regularAccount.setPenalty(penalty);
        regularAccount.setBalance(minimumBalance);

        return regularAccount;
    }

    @Override
    public RegularAccount withdraw(RegularAccount account, Double amount) {

        final Double currentBalance = account.getBalance();

        Double newBalance = currentBalance - amount;

        if (newBalance < account.getMinimumBalance()) {
            newBalance = newBalance - account.getPenalty();
        }

        account.setBalance(newBalance);

        return account;
    }

    @Override
    public RegularAccount deposit(RegularAccount account, Double amount) {

        final Double currentBalance = account.getBalance();

        account.setBalance(currentBalance + amount);

        return account;
    }
}
