package com.example.groupexercise1.service;

import java.util.List;

import com.example.groupexercise1.model.dto.AccountDto;
import com.example.groupexercise1.model.dto.AccountRequestDto;

public interface AccountService {
	AccountDto createAccount(AccountRequestDto accountRequest);
	AccountDto getAccount(Long accountId);
	List<AccountDto> getAllAccounts();
	AccountDto createTransaction(String type, long accountId, double amount);
	void deleteAccount(long accountId);
}
