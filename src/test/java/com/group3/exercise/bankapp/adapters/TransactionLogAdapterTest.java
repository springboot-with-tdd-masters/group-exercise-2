package com.group3.exercise.bankapp.adapters;

import com.group3.exercise.bankapp.constants.TransactionTypes;
import com.group3.exercise.bankapp.entities.Account;
import com.group3.exercise.bankapp.entities.RegularAccount;
import com.group3.exercise.bankapp.entities.TransactionLog;
import com.group3.exercise.bankapp.response.TransactionLogResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TransactionLogAdapterTest {

    private TransactionLogAdapter transactionLogAdapter;

    @BeforeEach
    void setUp() {
        transactionLogAdapter = new TransactionLogAdapter();
    }

    @Test
    @DisplayName("Should map to transaction log response successfully")
    void mapToResponse_shouldMapToTransactionLogResponseSuccessfully() {
        // Arrange
        final Date now = new Date();

        Account account = new RegularAccount();
        account.setId(1L);

        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setId(1L);
        transactionLog.setTransactionTypes(TransactionTypes.WITHDRAW);
        transactionLog.setAmount(100.0);
        transactionLog.setAccount(account);
        transactionLog.setTransactionDate(now);

        // Act
        final TransactionLogResponse actual = transactionLogAdapter.mapToResponse(transactionLog);

        // Assert
        assertEquals(1L, actual.getId());
        assertEquals(TransactionTypes.WITHDRAW.value(), actual.getTransactionType());
        assertEquals(100.0, actual.getAmount());
        assertEquals(1L, actual.getAccountId());
        assertEquals(now, actual.getTransactionDate());
    }
}