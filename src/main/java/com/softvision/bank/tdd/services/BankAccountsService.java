package com.softvision.bank.tdd.services;

import java.util.List;

import com.softvision.bank.tdd.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BankAccountsService {

	public Account get(long id);

	public List<Account> get();

	public void deleteById(long id);

	public Account createUpdate(Account account);

	public Page<Account> readAccounts(Pageable pageable);
}
