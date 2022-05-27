package com.advancejava.groupexercise.service;

import com.advancejava.groupexercise.entity.Account;
import com.advancejava.groupexercise.entity.BankTransaction;
import com.advancejava.groupexercise.model.dto.AccountRequest;
import com.advancejava.groupexercise.model.dto.DTORequest;

import java.util.List;

public interface BankService {

    public Account getAccount(Integer id);
    public List<Account> getAccounts();
    public Account createAccount(Account acct);
    public Account updateAccount(AccountRequest dep, Integer id);
    public void deleteAccount(Integer id);

    public BankTransaction updateTransaction(DTORequest request, Integer id);

    public BankTransaction getTransaction(Long id);
}
