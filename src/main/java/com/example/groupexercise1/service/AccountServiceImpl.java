package com.example.groupexercise1.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.groupexercise1.exeption.AccountNotFoundException;
import com.example.groupexercise1.exeption.InsufficientBalanceException;
import com.example.groupexercise1.exeption.InvalidAccountTypeException;
import com.example.groupexercise1.exeption.InvalidTransactionAmountException;
import com.example.groupexercise1.exeption.InvalidTransactionTypeException;
import com.example.groupexercise1.model.Account;
import com.example.groupexercise1.model.CheckingAccount;
import com.example.groupexercise1.model.InterestAccount;
import com.example.groupexercise1.model.RegularAccount;
import com.example.groupexercise1.model.dto.AccountDto;
import com.example.groupexercise1.model.dto.AccountRequestDto;
import com.example.groupexercise1.repository.AccountRepository;
import com.example.groupexercise1.util.AccountGenerator;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public AccountDto createAccount(AccountRequestDto accountRequest) {
		if (accountRequest.getType().equals("regular")) {
			Account newAccount = new RegularAccount();
			newAccount.setName(accountRequest.getName());
			newAccount.setMinimumBalance(500d);
			newAccount.setBalance(newAccount.getMinimumBalance());
			newAccount.setPenalty(10d);
			newAccount.setTransactionCharge(0d);
			newAccount.setInterestCharge(0d);
			newAccount.setAcctNumber(AccountGenerator.generateAccountNumber());

			Account savedAccount = accountRepository.save(newAccount);
			return new AccountDto(savedAccount);

		} else if (accountRequest.getType().equals("interest")) {
			Account newInterestAccount = new InterestAccount();
			newInterestAccount.setName(accountRequest.getName());
			newInterestAccount.setMinimumBalance(0d);
			newInterestAccount.setBalance(newInterestAccount.getMinimumBalance());
			newInterestAccount.setPenalty(0d);
			newInterestAccount.setTransactionCharge(0d);
			newInterestAccount.setInterestCharge(0.03);
			newInterestAccount.setAcctNumber(AccountGenerator.generateAccountNumber());
			newInterestAccount.setCreatedDate(LocalDate.now());

			Account savedAccount = accountRepository.save(newInterestAccount);
			return new AccountDto(savedAccount);
		} else if (accountRequest.getType().equals("checking")) {
			Account newAccount = new CheckingAccount();
			newAccount.setName(accountRequest.getName());
			newAccount.setMinimumBalance(100d);
			newAccount.setBalance(newAccount.getMinimumBalance());
			newAccount.setPenalty(10d);
			newAccount.setTransactionCharge(1d);
			newAccount.setInterestCharge(0d);
			newAccount.setAcctNumber(AccountGenerator.generateAccountNumber());

			Account savedAccount = accountRepository.save(newAccount);
			return new AccountDto(savedAccount);
		} else {
			throw new InvalidAccountTypeException();
		}
	}

	@Override
	public AccountDto getAccount(Long accountId) {

		Optional<Account> result = accountRepository.findById(accountId);

		if (result.isPresent()) {

			Account account = result.get();

			if (account.getType().equals("interest")) {
				Double monthlyInterest = getMonthlyInterest(account);

				account.setBalance(account.getBalance() + monthlyInterest);
				if (monthlyInterest > 0) {
					account.setCreatedDate(LocalDate.now());
				}
				accountRepository.save(account);
			}

			return new AccountDto(account);
		} else {
			throw new AccountNotFoundException("Account not found");
		}
	}

	@Override
	public List<AccountDto> getAllAccounts() {
		return accountRepository.findAll().stream().map(a -> new AccountDto(a)).collect(Collectors.toList());
	}

	@Override
	public AccountDto createTransaction(String type, long accountId, double amount) {
		Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);

		if (amount < 0) {
			throw new InvalidTransactionAmountException();
		}

		if (type.equals("deposit")) {
			makeDeposit(account, amount);
		} else if (type.equals("withdraw")) {
			makeWithdraw(account, amount);
		} else {
			throw new InvalidTransactionTypeException();
		}

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

	@Override
	public void deleteAccount(long accountId) {

		Optional<Account> account = accountRepository.findById(accountId);
		if (account.isPresent()) {
			accountRepository.delete(account.get());
		} else {
			throw new AccountNotFoundException("Account Not Found!");
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
}
