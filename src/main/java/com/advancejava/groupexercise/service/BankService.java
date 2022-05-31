package com.advancejava.groupexercise.service;

import com.advancejava.groupexercise.entity.Account;
import com.advancejava.groupexercise.entity.BankTransaction;
import com.advancejava.groupexercise.model.dto.AccountRequest;
import com.advancejava.groupexercise.model.dto.DTORequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BankService {

    public Account getAccount(Integer id);
    public List<Account> getAccounts();
    public Account createAccount(Account acct);
    public Account updateAccount(AccountRequest dep, Integer id);
    public void deleteAccount(Integer id);

    public BankTransaction updateTransaction(DTORequest request, Integer id);

    public BankTransaction getTransaction(Long id);

    Page<Account> getAccountsWithPaginationAndSort(int offset, int pageSize, String field, String order);

    Page<BankTransaction> getBankTxnsWithPaginationAndSort(int page, int limit, String field, String order);
}
