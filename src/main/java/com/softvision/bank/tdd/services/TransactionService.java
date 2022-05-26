package com.softvision.bank.tdd.services;

import java.util.List;

import com.softvision.bank.tdd.model.Account;
import com.softvision.bank.tdd.model.Transaction;

public interface TransactionService {
	public Account transact(long id, Transaction transaction);
	
	public List<Transaction> findbyAccountId(Long id);
	
	public void deleteTransactionById(Long accountId, Long transactionId);
}
