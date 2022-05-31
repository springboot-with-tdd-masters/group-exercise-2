package com.group3.exercise.bankapp.services.transaction;

import com.group3.exercise.bankapp.exceptions.BankAppException;
import com.group3.exercise.bankapp.exceptions.BankAppExceptionCode;
import com.group3.exercise.bankapp.request.TransactionRequest;
import com.group3.exercise.bankapp.response.TransactionLogResponse;
import com.group3.exercise.bankapp.services.transaction.impl.TransactionWriterImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionWriterTest {


    private TransactionWriter writer;

    @Mock
    private TransactionLogService service;

    @BeforeEach
    void setup() {
        this.writer = new TransactionWriterImpl(service);
    }

    @Test
    @DisplayName("Should write to repository succesfully")
    void shouldWriteToRepositorySuccessfully() {
        // given
        TransactionRequest request = new TransactionRequest();
        request.setType("withdraw");
        request.setAmount(100.0);
        when(service.createLogFor(anyLong(), any(TransactionRequest.class)))
                .thenReturn(new TransactionLogResponse());
        // when
        this.writer.writeTransaction(1L, request);
        // then
        verify(service, times(1)).createLogFor(1L, request);
    }

    @Test
    @DisplayName("Should call logger if fail to write due to BankAppException")
    void shouldCallLoggerIfFailToWriteToRepository_BankAppException() {
        TransactionRequest request = new TransactionRequest();
        request.setType("withdraw");
        request.setAmount(100.0);
        when(service.createLogFor(anyLong(), any(TransactionRequest.class)))
                .thenThrow(new BankAppException(BankAppExceptionCode.ACCOUNT_NOT_FOUND_EXCEPTION));
        // when
        this.writer.writeTransaction(1L, request);
        // then
        verify(service, times(1)).createLogFor(anyLong(), any(TransactionRequest.class));
    }

    @Test
    @DisplayName("Should call logger if fail to write due to Exception")
    void shouldCallLoggerIfFailToWriteToRepository_Exception() {
        TransactionRequest request = new TransactionRequest();
        request.setType("withdraw");
        request.setAmount(100.0);
        when(service.createLogFor(anyLong(), any(TransactionRequest.class)))
                .thenThrow(new IllegalArgumentException(""));
        // when
        this.writer.writeTransaction(1L, request);

    }
}