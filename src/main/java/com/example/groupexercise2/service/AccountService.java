package com.example.groupexercise2.service;

import com.example.groupexercise2.model.dto.AccountDto;
import com.example.groupexercise2.model.dto.AccountRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {
	AccountDto createAccount(AccountRequestDto accountRequest);
	AccountDto getAccount(Long accountId);
	Page<AccountDto> getAllAccounts(Pageable pageable);
	AccountDto createTransaction(String type, long accountId, double amount);
	void deleteAccount(long accountId);
	AccountDto updateAccount(AccountRequestDto accountRequest);
}
