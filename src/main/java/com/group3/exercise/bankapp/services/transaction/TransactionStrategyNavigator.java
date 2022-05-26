package com.group3.exercise.bankapp.services.transaction;

import com.group3.exercise.bankapp.entities.Account;

public interface TransactionStrategyNavigator {
    Account generateNewAccountDetails(String name, String acctNumber, String type);
    Account withdraw(Account account, Double amount);
    Account deposit(Account account, Double amount);
}
