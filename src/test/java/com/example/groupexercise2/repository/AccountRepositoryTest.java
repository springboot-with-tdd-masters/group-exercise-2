package com.example.groupexercise2.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.groupexercise2.model.Account;
import com.example.groupexercise2.model.CheckingAccount;
import com.example.groupexercise2.model.InterestAccount;
import com.example.groupexercise2.model.RegularAccount;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
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

		assertThat(actualResponse.getCreatedAt()).isNotNull();
		assertThat(actualResponse.getUpdatedAt()).isNotNull();
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
  public void getOneAccount() {

    RegularAccount newAccount = new RegularAccount();
    newAccount.setMinimumBalance(500d);
    newAccount.setName("Juan Dela Cruz");

    RegularAccount savedRegularAccount = accountRepository.save(newAccount);

    Optional<Account> actualAccountDto = accountRepository.findById(savedRegularAccount.getId());

    assertThat(actualAccountDto.get())
        .extracting("id", "name", "minimumBalance")
        .containsExactly(savedRegularAccount.getId(), savedRegularAccount.getName(),
            savedRegularAccount.getMinimumBalance());

    assertThat(actualAccountDto.get().getCreatedAt()).isNotNull();
    assertThat(actualAccountDto.get().getUpdatedAt()).isNotNull();
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
    assertThat(updatedResponse.getCreatedAt()).isNotNull();
    assertThat(updatedResponse.getUpdatedAt()).isNotNull();

  }

  @Test
  @DisplayName("Upon successful update of account name should return the updated value")
  public void updateAccountChangeName() {
    RegularAccount newAccount = new RegularAccount();
    newAccount.setMinimumBalance(500d);
    newAccount.setName("Juan Dela Cruz");

    RegularAccount initialResponse = accountRepository.save(newAccount);
    assertThat(initialResponse.getName()).isEqualTo(newAccount.getName());

    Optional<Account> actualAccount = accountRepository.findById(initialResponse.getId());
    RegularAccount savedAccount = (RegularAccount) actualAccount.get();
    savedAccount.setName("John Delacroix");

    RegularAccount actualResponse = accountRepository.save(savedAccount);

    assertThat(actualResponse.getName()).isEqualTo("John Delacroix");
    assertThat(actualResponse.getCreatedAt()).isNotNull();
    assertThat(actualResponse.getUpdatedAt()).isNotNull();
  }


  @Test
  @DisplayName("Delete account")
  public void deleteAccount() {
    RegularAccount newAccount = new RegularAccount();
    newAccount.setMinimumBalance(500d);
    newAccount.setName("Juan Dela Cruz");

    RegularAccount initialResponse = accountRepository.save(newAccount);
    assertThat(initialResponse.getName()).isEqualTo(newAccount.getName());

    accountRepository.deleteById(initialResponse.getId());

    Optional<Account> actualAccount = accountRepository.findById(initialResponse.getId());

    assertThat(actualAccount.isEmpty()).isEqualTo(true);

  }
  
  @Test
  @DisplayName("Get all acounts with paging and sorting")
  public void getAllAccountsWithPagingAndSorting() {
	  RegularAccount regAccount = new RegularAccount();
	  regAccount.setMinimumBalance(500d);
	  regAccount.setName("Juan Dela Cruz");

	  CheckingAccount checkingAcct = new CheckingAccount();
	  checkingAcct.setMinimumBalance(100d);
	  checkingAcct.setName("Juan Dela Cruz");
	  
	  InterestAccount interestAcct = new InterestAccount();
	  interestAcct.setMinimumBalance(0d);
	  interestAcct.setName("Juan Dela Cruz");
	  
	  accountRepository.save(regAccount);
	  accountRepository.save(checkingAcct);
	  accountRepository.save(interestAcct);
	  
	  Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
	  Page<Account> pagedAccounts = accountRepository.findAll(pageable);
	    
	 	assertAll(
	 		() -> assertEquals(1, pagedAccounts.getTotalPages()),
			() -> assertEquals(3, pagedAccounts.getTotalElements()),
			() -> assertEquals(3, pagedAccounts.getNumberOfElements()),
			() -> assertEquals("interest", pagedAccounts.getContent().get(0).getType()),			   
			() -> assertEquals("checking", pagedAccounts.getContent().get(1).getType()),			    
			() -> assertEquals("regular", pagedAccounts.getContent().get(2).getType())
	 	);		
	}	
}
