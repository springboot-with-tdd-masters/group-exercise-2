package com.example.groupexercise2.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.groupexercise2.exeption.AccountNotFoundException;
import com.example.groupexercise2.exeption.InsufficientBalanceException;
import com.example.groupexercise2.exeption.InvalidAccountTypeException;
import com.example.groupexercise2.exeption.InvalidTransactionAmountException;
import com.example.groupexercise2.exeption.InvalidTransactionTypeException;
import com.example.groupexercise2.model.Account;
import com.example.groupexercise2.model.CheckingAccount;
import com.example.groupexercise2.model.InterestAccount;
import com.example.groupexercise2.model.RegularAccount;
import com.example.groupexercise2.model.dto.AccountDto;
import com.example.groupexercise2.model.dto.AccountRequestDto;
import com.example.groupexercise2.repository.AccountRepository;
import com.example.groupexercise2.util.AccountGenerator;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public AccountDto createAccount(AccountRequestDto accountRequest) {
		Account newAccount = null;
		if (accountRequest.getType().equals("regular")) {
			newAccount = new RegularAccount();
			newAccount.initialize(accountRequest.getName());
		} else if (accountRequest.getType().equals("interest")) {
			newAccount = new InterestAccount();
			newAccount.initialize(accountRequest.getName());
		} else if (accountRequest.getType().equals("checking")) {
			newAccount = new CheckingAccount();
			newAccount.initialize(accountRequest.getName());
		} else {
			throw new InvalidAccountTypeException();
		}

		Account savedAccount = accountRepository.save(newAccount);
		return new AccountDto(savedAccount);
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
	public Page<AccountDto> getAllAccounts(Pageable pageable) {
		return accountRepository.findAll(pageable).map(AccountDto::convertToDto);
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

	@Override
	public AccountDto updateAccount(AccountRequestDto accountRequest) {
		Optional<Account> result = accountRepository.findById(accountRequest.getId());
		if (result.isPresent()) {
			Account account = result.get();
			account.setName(accountRequest.getName());
			accountRepository.save(account);
			return new AccountDto(account);
		} else {
			throw new AccountNotFoundException("Account not found");
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
