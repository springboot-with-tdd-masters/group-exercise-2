package com.example.groupexercise1.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.groupexercise1.model.Account;
import com.example.groupexercise1.model.dto.AccountDto;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.groupexercise1.model.CheckingAccount;
import com.example.groupexercise1.model.InterestAccount;
import com.example.groupexercise1.model.RegularAccount;


@DataJpaTest
public class AccountRepositoryTest {
	
	 @Autowired
	 private AccountRepository accountRepository;
	
	 @Test
	 @DisplayName("Should save RegularAccount Entity with the correct details")
	 public void saveRegularAccount() {
	     RegularAccount newAccount = new RegularAccount();
	     newAccount.setMinimumBalance(500d);
	     
	     RegularAccount actualResponse = accountRepository.save(newAccount);
	     
	     assertThat(actualResponse.getMinimumBalance()).isEqualTo(newAccount.getMinimumBalance());
	 }
	 
	 
	 @Test
	 @DisplayName("Should return all accounts with correct details")
	 public void getAllAccounts() {
	     RegularAccount regularAccount = new RegularAccount();
	     regularAccount.setName("Juan Dela Cruz");
	     regularAccount.setMinimumBalance(500d);
	     
	     CheckingAccount checkingAccount = new CheckingAccount();
	     checkingAccount.setName("Juan Dela Cruz");
	     checkingAccount.setMinimumBalance(100d);
	
	     InterestAccount interestAccount = new InterestAccount();
	     interestAccount.setName("Juan Dela Cruz");
	     interestAccount.setMinimumBalance(0d);
	
	     RegularAccount savedRegAccount = accountRepository.save(regularAccount);
	     CheckingAccount savedCheckAccount = accountRepository.save(checkingAccount);
	     InterestAccount savedInterestAccount = accountRepository.save(interestAccount);
	     
	     assertThat(accountRepository.findAll())
	     	.hasSize(3)
	     	.contains(savedRegAccount, savedCheckAccount, savedInterestAccount);
	 }

	 @Test
	 @DisplayName("Should return one account with correct details")
	 public void getOneAccount(){

		 RegularAccount newAccount = new RegularAccount();
		 newAccount.setMinimumBalance(500d);
		 newAccount.setName("Juan Dela Cruz");

		 RegularAccount savedRegularAccount = accountRepository.save(newAccount);

		 Optional<Account> actualAccountDto = accountRepository.findById(savedRegularAccount.getId());

		 assertThat(actualAccountDto.get())
				 .extracting("id", "name", "minimumBalance")
				 .containsExactly(savedRegularAccount.getId(),savedRegularAccount.getName(),savedRegularAccount.getMinimumBalance());
	 }		 
	 
	 @Test
	 @DisplayName("Should be able to deposit on regular account")
	 public void shouldeBeAbleToDepositOnRegularAccount() {
	     RegularAccount newAccount = new RegularAccount();
	     newAccount.setMinimumBalance(500d);
	     
	     RegularAccount actualResponse = accountRepository.save(newAccount);
	     
	     Double depositAmt = 100d;
	     actualResponse.setBalance(actualResponse.getMinimumBalance() + depositAmt);
	     
	     RegularAccount updatedResponse = accountRepository.save(newAccount);

	     assertThat(updatedResponse.getBalance()).isEqualTo(actualResponse.getBalance());
	 }
}
