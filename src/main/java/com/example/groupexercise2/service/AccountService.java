package com.example.groupexercise2.service;

import com.example.groupexercise2.model.Account;
import com.example.groupexercise2.model.dto.AccountDto;
import com.example.groupexercise2.model.dto.AccountRequestDto;

import java.util.List;

public interface AccountService {
	AccountDto createAccount(AccountRequestDto accountRequest);
	AccountDto getAccount(Long accountId);
	List<AccountDto> getAllAccounts();
	AccountDto createTransaction(String type, long accountId, double amount);
	void deleteAccount(long accountId);
  AccountDto updateAccount(AccountRequestDto accountRequest);
}
