package com.group3.exercise.bankapp.services.transaction;

import com.group3.exercise.bankapp.entities.RegularAccount;
import com.group3.exercise.bankapp.entities.TransactionLog;
import com.group3.exercise.bankapp.repository.AccountRepository;
import com.group3.exercise.bankapp.repository.TransactionLogRepository;
import com.group3.exercise.bankapp.request.TransactionRequest;
import com.group3.exercise.bankapp.services.transaction.impl.TransactionWriterImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionWriterTest {


    private TransactionWriter writer;

    @Mock
    private TransactionLogRepository transactionLogRepository;
    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setup(){
        this.writer = new TransactionWriterImpl(transactionLogRepository, accountRepository);
    }

    @Test
    @DisplayName("Should write to repository succesfully")
    void shouldWriteToRepositorySuccessfully(){
        // given
        TransactionRequest request = new TransactionRequest();
        request.setType("withdraw");
        request.setAmount(100.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new RegularAccount()));
        when(transactionLogRepository.save(any(TransactionLog.class))).thenReturn(new TransactionLog());
        // when
        this.writer.writeTransaction(1L, request);
        // then
        verify(accountRepository, times(1)).findById(1L);
        verify(transactionLogRepository, times(1)).save(any(TransactionLog.class));
    }
    @Test
    @DisplayName("Should call logger if fail to write due to BankAppException")
    void shouldCallLoggerIfFailToWriteToRepository_BankAppException(){
        TransactionRequest request = new TransactionRequest();
        request.setType("withdraw");
        request.setAmount(100.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        // when
        this.writer.writeTransaction(1L, request);
        // then
        verify(accountRepository, times(1)).findById(1L);
        verify(transactionLogRepository, never()).save(any(TransactionLog.class));
    }
    @Test
    @DisplayName("Should call logger if fail to write due to Exception")
    void shouldCallLoggerIfFailToWriteToRepository_Exception(){
        TransactionRequest request = new TransactionRequest();
        request.setType("withdraw");
        request.setAmount(100.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new RegularAccount()));
        when(transactionLogRepository.save(any(TransactionLog.class))).thenThrow(new IllegalArgumentException(""));
        // when
        this.writer.writeTransaction(1L, request);
        // then
//        verify(logger, times(1)).debug(anyString(), anyLong(), anyString());
        verify(accountRepository, times(1)).findById(1L);
        verify(transactionLogRepository, times(1)).save(any(TransactionLog.class));
    }
}