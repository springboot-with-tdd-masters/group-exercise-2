package com.softvision.bank.tdd.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.softvision.bank.tdd.model.Account;
import com.softvision.bank.tdd.model.Transaction;

public interface TransactionService {
	public Account transact(long id, Transaction transaction);
	
	public Page<Transaction> findbyAccountId(Pageable pageable, Long id);
	
	public void deleteTransactionById(Long accountId, Long transactionId);
}
