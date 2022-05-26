package com.group3.exercise.bankapp.services.transaction;

import com.group3.exercise.bankapp.entities.Account;

public interface TransactionStrategy<T extends Account> {
    T generateNewAccountDetails(String name, String acctNumber);
    T withdraw(T account, Double amount);
    T deposit(T account, Double amount);
}
