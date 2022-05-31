package com.example.groupexercise2.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.groupexercise2.exeption.AccountNotFoundException;
import com.example.groupexercise2.exeption.InsufficientBalanceException;
import com.example.groupexercise2.exeption.InvalidAccountTypeException;
import com.example.groupexercise2.exeption.InvalidTransactionAmountException;
import com.example.groupexercise2.exeption.InvalidTransactionTypeException;
import com.example.groupexercise2.exeption.TransactionNotFoundException;
import com.example.groupexercise2.model.Account;
import com.example.groupexercise2.model.Transaction;
import com.example.groupexercise2.model.dto.AccountDto;
import com.example.groupexercise2.model.dto.TransactionDto;
import com.example.groupexercise2.model.dto.TransactionRequestDto;
import com.example.groupexercise2.repository.AccountRepository;
import com.example.groupexercise2.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public TransactionDto getTransactionById(long transactionId) {

		Optional<Transaction> result = transactionRepository.findById(transactionId);

		if (result.isPresent()) {

			return new TransactionDto(result.get());
		} else {
			throw new TransactionNotFoundException("Transaction not found");
		}
	}

	@Override
	public AccountDto createTransaction(long accountId, TransactionRequestDto request) {
		Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);

		if (request.getAmount() < 0) {
			throw new InvalidTransactionAmountException();
		}

		if (request.getType().equals("deposit")) {
			makeDeposit(account, request.getAmount());
		} else if (request.getType().equals("withdraw")) {
			makeWithdraw(account, request.getAmount());
		} else {
			throw new InvalidTransactionTypeException();
		}
		
		Transaction transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setAmount(request.getAmount());
		transaction.setTransactionType(request.getType());
		transactionRepository.save(transaction);

		return new AccountDto(account);

	}
	
	private void makeDeposit(Account account, double amount) {
		if (account.getType().equals("regular")) {
			account.setBalance(account.getBalance() + amount);
		} else if (account.getType().equals("checking")) {
			account.setBalance(account.getBalance() + amount - account.getTransactionCharge());

		} else if (account.getType().equals("interest")) {

			Double monthlyInterest = getMonthlyInterest(account);

			account.setBalance(account.getBalance() + amount + monthlyInterest);
			if (monthlyInterest > 0) {
				account.setCreatedDate(LocalDate.now());
			}
		} else {
			throw new InvalidAccountTypeException();
		}
	}

	private void makeWithdraw(Account account, double amount) {
		if (account.getBalance() == 0) {
			throw new InsufficientBalanceException();
		}

		if (account.getType().equals("regular")) {
			account.setBalance(account.getBalance() - amount);

			if (account.getBalance() < account.getMinimumBalance()) {
				account.setBalance(account.getBalance() - account.getPenalty());
			}

			if (account.getBalance() < 0) {
				throw new InsufficientBalanceException();
			}
		} else if (account.getType().equals("checking")) {
			account.setBalance(account.getBalance() - amount - account.getTransactionCharge());
			if (account.getBalance() < account.getMinimumBalance()) {
				account.setBalance(account.getBalance() - account.getPenalty());
			}
			if (account.getBalance() < 0) {
				throw new InsufficientBalanceException();
			}

		} else if (account.getType().equals("interest")) {
			Double monthlyInterest = getMonthlyInterest(account);

			account.setBalance(account.getBalance() - amount + monthlyInterest);
			if (monthlyInterest > 0) {
				account.setCreatedDate(LocalDate.now());
			}
		} else {
			throw new InvalidAccountTypeException();
		}
	}
	
	private Double getMonthlyInterest(Account account) {

		Double monthlyInterest = 0d;

		Period age = Period.between(account.getCreatedDate(), LocalDate.now());
		int months = age.getMonths();

		if (months > 0) {
			monthlyInterest = account.getBalance() * (account.getInterestCharge() * months);
		}

		return monthlyInterest;
	}

	@Override
	public void delete(long accountId, long transactionId) {
		accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
		transactionRepository.findById(transactionId).orElseThrow(TransactionNotFoundException::new);
		transactionRepository.deleteById(transactionId);
	}

	@Override
	public Page<TransactionDto> getAllTransactions(Long accountId, Pageable pageable) {
		return transactionRepository.findByAccountId(accountId, pageable).map(TransactionDto::convertToDto);
	}
}
