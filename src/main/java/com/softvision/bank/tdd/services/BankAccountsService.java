package com.softvision.bank.tdd.services;

import java.util.List;

import com.softvision.bank.tdd.model.Account;

public interface BankAccountsService {

	public Account get(long id);

	public List<Account> get();

	public void deleteById(long id);

	public Account createUpdate(Account account);
}
