package com.softvision.bank.tdd.services;

import com.softvision.bank.tdd.ApplicationConstants;
import com.softvision.bank.tdd.exceptions.BadRequestException;
import com.softvision.bank.tdd.exceptions.InsufficientFundsAvailable;
import com.softvision.bank.tdd.exceptions.RecordNotFoundException;
import com.softvision.bank.tdd.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.softvision.bank.tdd.repository.AccountRepository;
import com.softvision.bank.tdd.repository.TransactionRepository;

import static java.util.Optional.of;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	TransactionRepository transactionRepository;

	public Account transact(long id, Transaction transaction) {
		Account account = accountRepository.findById(id).orElseThrow(RecordNotFoundException::new);

		if(transaction.getAmount() <= 0)
			throw new BadRequestException();
		
		switch (of(transaction).map(Transaction::getType).map(String::toUpperCase)
				.orElseThrow(BadRequestException::new)) {
			case ApplicationConstants.DEPOSIT:
				doDeposit(account, transaction.getAmount());
				break;
			case ApplicationConstants.WITHDRAW:
				doWithdraw(account, transaction.getAmount());
				break;
			default:
				throw new BadRequestException();
		}
		computeCharges(account);
		transaction.setAccount(account);
		transactionRepository.save(transaction);
		return accountRepository.save(account);
	}

	private void doDeposit(Account account, double amount) {
		double balance = Double.sum(account.getBalance(), amount);
		account.setBalance(balance);
	}

	private void doWithdraw(Account account, double amount) {
		if (amount > account.getBalance()) {
			throw new InsufficientFundsAvailable();
		}
		account.setBalance(account.getBalance() - amount);
	}

	private static void computeCharges(Account account) {
		if (account instanceof RegularAccount) {
			computePenalties(account);
		} else if (account instanceof CheckingAccount) {
			computePenalties(account);
			computeTransactionCharge(account);
		}
	}

	private static void computePenalties(Account account) {
		if (account.getBalance() < account.getMinimumBalance()) {
			account.setBalance(account.getBalance() - account.getPenalty());
		}
	}

	private static void computeTransactionCharge(Account account) {
		account.setBalance(account.getBalance() - account.getTransactionCharge());
	}

	@Override
	public List<Transaction> findbyAccountId(Long id) {
		return transactionRepository.findByAccountId(id);
	}

	@Override
	public void deleteTransactionById(Long accountId, Long transactionId) {
		accountRepository.findById(accountId).orElseThrow(RecordNotFoundException::new);
		transactionRepository.findById(transactionId).orElseThrow(RecordNotFoundException::new);
		transactionRepository.deleteById(transactionId);
	}

	@Override
	public Page<Transaction> readTransactions(Pageable pageable) {
		return transactionRepository.findAll(pageable);
	}
}
