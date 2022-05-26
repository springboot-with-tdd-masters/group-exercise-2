package com.masters.masters.exercise.services;

import com.masters.masters.exercise.exception.AccountExistException;
import com.masters.masters.exercise.exception.InvalidTypeException;
import com.masters.masters.exercise.exception.RecordNotFoundException;
import com.masters.masters.exercise.model.*;
import com.masters.masters.exercise.model.dto.AccountDto;
import com.masters.masters.exercise.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl {

	@Autowired
	private AccountRepository repo;

	// create
	public Account createOrUpdateAccount(AccountDto dto) throws RecordNotFoundException, InvalidTypeException, AccountExistException {
		Account result = null;

		if(dto != null) {
			Optional<Account> account = repo.findByName(dto.getName());

			if(StringUtils.hasText(dto.getType())) {
				String type = dto.getType();
				
				if(account.isPresent()) {
					throw new AccountExistException("The account is already exist");
				}

				if(type.equalsIgnoreCase(AccountType.REGULAR.toString())) {
						Account newEntity = new RegularAccount();
						newEntity.setName(dto.getName());
						newEntity = repo.save(newEntity);
						result = newEntity;
				} else if(type.equalsIgnoreCase(AccountType.INTEREST.toString())) {
						Account newAccount = new InterestAccount();
						newAccount.setName(dto.getName());
						result = repo.save(newAccount);
				} else if(type.equalsIgnoreCase(AccountType.CHECKING.toString())) {
						Account newAccount = new CheckingAccount();
						newAccount.setName(dto.getName());
						result = repo.save(newAccount);
				} else {
					throw new InvalidTypeException("Invalid Account Type");
				}

			} else {
				throw new InvalidTypeException("Invalid Account Type");
			}
		}

		return result;
	}

	public List<Account> getAllAccounts() throws RecordNotFoundException {
		List<Account> accountList = repo.findAll();
		if (accountList.size() > 0) {
			return accountList;
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}
	
	public Account getAccountById(Long id) throws RecordNotFoundException {
		Optional<Account> account = repo.findById(id);
		return account.map(obj -> {
            return obj;
        })
		.orElseThrow(() -> new RecordNotFoundException("No Record found with id: " + id));
	}
	
	public Account deleteAccountById(Long id) throws RecordNotFoundException {
		Optional<Account> account = repo.findById(id);
		return account.map(obj -> {
			repo.deleteById(obj.getId());
            return obj;
        })
		.orElseThrow(() -> new RecordNotFoundException("No Record found with accountNumber: " + id));
	}
	
	
	//withdraw/deposit
	//delete

}
