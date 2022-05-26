package com.example.groupexercise1.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.groupexercise1.model.Account;
import com.example.groupexercise1.model.RegularAccount;
import com.example.groupexercise1.model.Transaction;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
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

}
