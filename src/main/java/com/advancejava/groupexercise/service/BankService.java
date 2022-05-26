package com.advancejava.groupexercise.service;

import com.advancejava.groupexercise.entity.Account;
import com.advancejava.groupexercise.entity.AccountTransactions;
import com.advancejava.groupexercise.model.dto.DTORequest;

import java.util.List;

public interface BankService {

    public Account getAccount(Integer id);
    public List<Account> getAccounts();
    public Account createAccount(Account acct);
    public Account updateAccount(DTORequest dep, Integer id);
    public void deleteAccount(Integer id);

    public AccountTransactions updateTxn(Account account, DTORequest request, Integer id);

    public AccountTransactions getTxn(Long id);
}
