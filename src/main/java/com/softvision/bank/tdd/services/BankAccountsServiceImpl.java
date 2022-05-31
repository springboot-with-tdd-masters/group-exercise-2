package com.softvision.bank.tdd.services;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.softvision.bank.tdd.ApplicationConstants;
import com.softvision.bank.tdd.exceptions.RecordNotFoundException;
import com.softvision.bank.tdd.model.Account;
import com.softvision.bank.tdd.model.CheckingAccount;
import com.softvision.bank.tdd.model.InterestAccount;
import com.softvision.bank.tdd.model.RegularAccount;
import com.softvision.bank.tdd.repository.AccountRepository;

@Service
public class BankAccountsServiceImpl implements BankAccountsService {

	@Autowired
	AccountRepository repository;

	@Override
	public Account get(long id) {
		return repository.findById(id).orElseThrow(RecordNotFoundException::new);
	}
	
	@Override
	public Page<Account> get(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public void deleteById(long id) {
		repository.findById(id).orElseThrow(RecordNotFoundException::new);
		repository.deleteById(id);
	}

	@Override
	public Account createUpdate(Account accountEntity) {
		Optional<Account> account = repository.findById(accountEntity.getId());
		
		if(account.isPresent()) {
			Account newAccount = account.get();
			newAccount.setName(accountEntity.getName());
			return repository.save(newAccount);
		} else {
			accountEntity.setAcctNumber(generateRandomAcctNum());
			if (accountEntity instanceof RegularAccount) {
				accountEntity.setPenalty(ApplicationConstants.REG_PENALTY);
				accountEntity.setMinimumBalance(ApplicationConstants.REG_MIN_BALANCE);
				accountEntity.setBalance(ApplicationConstants.REG_MIN_BALANCE);
			} else if (accountEntity instanceof CheckingAccount) {
				accountEntity.setMinimumBalance(ApplicationConstants.CHK_MIN_BALANCE);
				accountEntity.setTransactionCharge(ApplicationConstants.CHK_CHARGE);
				accountEntity.setPenalty(ApplicationConstants.CHK_PENALTY);
				accountEntity.setBalance(ApplicationConstants.CHK_MIN_BALANCE);
			} else if (accountEntity instanceof InterestAccount) {
				accountEntity.setInterestCharge(ApplicationConstants.INT_INTEREST);
			}
		}
		
		return repository.save(accountEntity);
	}

	private static String generateRandomAcctNum() {
		return new Random().ints(12, 48, 57)
				.mapToObj(c -> Character.toString((char) c))
				.collect(Collectors.joining());
	}

}
