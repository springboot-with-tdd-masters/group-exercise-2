package com.advancejava.groupexercise1.service;

import com.advancejava.groupexercise1.entity.Account;
import com.advancejava.groupexercise1.model.Deposit;

import java.util.List;

public interface BankService {

    public Account getAccount(Integer id);
    public List<Account> getAccounts();
    public Account createAccount(Account acct);
    public Account updateAccount(Deposit dep, Integer id);
    public void deleteAccount(Integer id);
}
