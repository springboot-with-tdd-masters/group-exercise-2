package com.softvision.bank.tdd.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.softvision.bank.tdd.model.Account;

public interface BankAccountsService {

	public Account get(long id);

	public Page<Account> get(Pageable pageable);

	public void deleteById(long id);

	public Account createUpdate(Account account);
}
