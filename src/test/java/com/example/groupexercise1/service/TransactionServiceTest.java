package com.example.groupexercise1.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.groupexercise1.model.Account;
import com.example.groupexercise1.model.RegularAccount;
import com.example.groupexercise1.model.Transaction;
import com.example.groupexercise1.model.dto.AccountDto;
import com.example.groupexercise1.model.dto.TransactionDto;
import com.example.groupexercise1.repository.TransactionRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TransactionServiceTest {

  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private TransactionService transactionService = new TransactionServiceImpl();

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Should return one transaction with correct details")
  public void shouldReturnOneTransactionsWithCorrectDetails() {

    RegularAccount mockedAccount = mock(RegularAccount.class);

    Transaction expectedResponseTransaction = new Transaction();
    expectedResponseTransaction.setTransactionType("withdraw");
    expectedResponseTransaction.setAmount(100d);
    expectedResponseTransaction.setAccount(mockedAccount);

    when(transactionRepository.findById(1L))
        .thenReturn(Optional.of(expectedResponseTransaction));

    TransactionDto actualResponse = transactionService.getTransactionById(1L);

    verify(transactionRepository).findById(1L);

    assertThat(actualResponse)
        .extracting("id", "transactionType", "amount")
        .containsExactly(actualResponse.getId(), actualResponse.getTransactionType(),
            actualResponse.getAmount());


  }

}
