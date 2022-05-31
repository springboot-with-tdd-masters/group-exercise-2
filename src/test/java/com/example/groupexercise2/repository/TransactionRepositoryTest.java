package com.example.groupexercise2.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.groupexercise2.model.Account;
import com.example.groupexercise2.model.CheckingAccount;
import com.example.groupexercise2.model.InterestAccount;
import com.example.groupexercise2.model.RegularAccount;
import com.example.groupexercise2.model.Transaction;
import com.example.groupexercise2.repository.AccountRepository;
import com.example.groupexercise2.repository.TransactionRepository;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@DataJpaTest
public class TransactionRepositoryTest {

  @Autowired
  TransactionRepository transactionRepository;

  @Autowired
  AccountRepository accountRepository;

  @Test
  @DisplayName("Should return one transaction with correct details and respective created/update date")
  public void getOneTransactionWithCreatedAndUpdateDate() {

    RegularAccount account = new RegularAccount();
    account.setMinimumBalance(500d);
    account.setName("Juan Dela Cruz");

    Account savedAccount = accountRepository.save(account);

    Transaction transaction = new Transaction();
    transaction.setTransactionType("withdraw");
    transaction.setAmount(100d);
    transaction.setAccount(savedAccount);

    Transaction savedTransaction = transactionRepository.save(transaction);

    Optional<Transaction> actualTransaction = transactionRepository.findById(
        savedTransaction.getId());

    assertThat(actualTransaction.get())
        .extracting("id", "amount", "transactionType")
        .containsExactly(savedTransaction.getId(),
            savedTransaction.getAmount(),
            savedTransaction.getTransactionType());

    assertThat(actualTransaction.get().getCreatedAt()).isNotNull();
    assertThat(actualTransaction.get().getUpdatedAt()).isNotNull();

  }
  
  @Test
  @DisplayName("Get all transactions with paging and sorting")
  public void getAllTransactionsWithPagingAndSorting() {
	  Account regularAccount = new RegularAccount();
	  regularAccount.setAcctNumber("012345678");
	  
	  Transaction  withdraw = new Transaction();
	  withdraw.setTransactionType("withdraw");
	  withdraw.setAmount(100d);
	  withdraw.setAccount(regularAccount);
	 
	  Transaction deposit = new Transaction();
	  deposit.setTransactionType("deposit");
	  deposit.setAmount(1000d);
	  deposit.setAccount(regularAccount);
	  
	  Account savedAccount = accountRepository.save(regularAccount);
	  transactionRepository.save(withdraw);
	  transactionRepository.save(deposit);
	  
	  Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
	  Page<Transaction> pagedAccounts = transactionRepository.findByAccountId(savedAccount.getId(), pageable);
	    
	  assertAll(
	 		() -> assertEquals(1, pagedAccounts.getTotalPages()),
			() -> assertEquals(2, pagedAccounts.getTotalElements()),
			() -> assertEquals(2, pagedAccounts.getNumberOfElements()),
			() -> assertEquals("deposit", pagedAccounts.getContent().get(0).getTransactionType()),			   
			() -> assertEquals("withdraw", pagedAccounts.getContent().get(1).getTransactionType())	    
		);		
	}	

}
