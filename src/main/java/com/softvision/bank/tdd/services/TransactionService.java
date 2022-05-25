package com.softvision.bank.tdd.services;

import com.softvision.bank.tdd.model.Account;
import com.softvision.bank.tdd.model.Transaction;

public interface TransactionService {
	Account transact(long id, Transaction transaction);
}
