package com.group3.exercise.bankapp.services;

import com.group3.exercise.bankapp.adapters.TransactionLogAdapter;
import com.group3.exercise.bankapp.constants.TransactionTypes;
import com.group3.exercise.bankapp.entities.Account;
import com.group3.exercise.bankapp.entities.InterestAccount;
import com.group3.exercise.bankapp.entities.RegularAccount;
import com.group3.exercise.bankapp.entities.TransactionLog;
import com.group3.exercise.bankapp.exceptions.BankAppException;
import com.group3.exercise.bankapp.repository.AccountRepository;
import com.group3.exercise.bankapp.repository.TransactionLogRepository;
import com.group3.exercise.bankapp.request.TransactionRequest;
import com.group3.exercise.bankapp.response.TransactionLogResponse;
import com.group3.exercise.bankapp.services.transaction.TransactionLogService;
import com.group3.exercise.bankapp.services.transaction.impl.TransactionLogServiceImpl;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionLogServiceTest {

    private TransactionLogService transactionLogService;

    @Mock
    private TransactionLogRepository transactionLogRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionLogAdapter transactionLogAdapter;

    @BeforeEach
    void setUp() {
        transactionLogService = new TransactionLogServiceImpl(
                transactionLogRepository,
                accountRepository,
                transactionLogAdapter);
    }

    @Test
    @DisplayName("Should return all transaction log with that account id")
    void findAllByAccount_shouldReturnAllTransactionLogByThatAccount() {
        // Arrange
        Long accountId = 1L;
        Account account = new RegularAccount();
        account.setId(accountId);
        Pageable pageRequest = PageRequest.of(0, 1);

        final List<TransactionLog> content = Arrays.asList(new TransactionLog(), new TransactionLog());
        final PageImpl<TransactionLog> pagedTransactionLogs = new PageImpl<>(content, pageRequest, content.size());

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.ofNullable(account));
        when(transactionLogRepository.findByAccountId(accountId, pageRequest))
                .thenReturn(pagedTransactionLogs);
        when(transactionLogAdapter.mapToResponse(any(TransactionLog.class)))
                .thenReturn(new TransactionLogResponse(), new TransactionLogResponse());

        // Act
        final Page<TransactionLogResponse> transactionLogPage = transactionLogService.findAllByAccountId(accountId, pageRequest);

        // Assert
        verify(accountRepository)
                .findById(accountId);
        verify(transactionLogRepository)
                .findByAccountId(accountId, pageRequest);

        assertThat(transactionLogPage)
                .isNotEmpty();
    }

    @Test
    @DisplayName("Should thrown an error when account id doesn't exists on database")
    void findAllByAccountId_shouldThrowAnErrorIfAccountDoesntExists() {
        // Arrange

        // Act
        final Throwable throwable = catchThrowable(() -> transactionLogService.findAllByAccountId(1L, PageRequest.of(0, 1)));

        // Assert
        assertThat(throwable)
                .hasMessage("Unable to process your request. Account does not exists")
                .asInstanceOf(InstanceOfAssertFactories.type(BankAppException.class));
    }

    @Test
    @DisplayName("Should create transaction log base on parameters and return as transaction log response")
    void createLog_shouldCreateATransactionLogBaseOnParameters() {
        // Arrange
        Long accountId = 1L;
        TransactionTypes transactionType = TransactionTypes.DEPOSIT;

        Account account = new InterestAccount();
        account.setId(accountId);
        TransactionLogResponse transactionLogResponse = new TransactionLogResponse();

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.ofNullable(account));
        when(transactionLogAdapter.mapToResponse(any(TransactionLog.class)))
                .thenReturn(transactionLogResponse);

        // Act
        final TransactionLogResponse transactionLog = transactionLogService.createLogFor(accountId, TransactionRequest.of("deposit", 100.0));

        // Assert
        verify(accountRepository)
                .findById(accountId);
        verify(transactionLogRepository)
                .save(argThat(txnLog ->
                        Objects.equals(txnLog.getTransactionTypes(), transactionType)
                        && Objects.equals(txnLog.getAccount().getId(), accountId)
                ));
        verify(transactionLogAdapter)
                .mapToResponse(argThat(txnLog ->
                        Objects.equals(txnLog.getTransactionTypes(), transactionType)
                                && Objects.equals(txnLog.getAccount().getId(), accountId)
                ));

        assertThat(transactionLog)
                .isNotNull()
                .isEqualTo(transactionLogResponse);
    }

    @Test
    @DisplayName("Should throw an error when account id doesn't exists on database")
    void createLog_shouldThrowErrorWhenAccountDoesntExists() {
        // Arrange

        // Act
        final Throwable throwable = catchThrowable(() -> transactionLogService.createLogFor(1L, TransactionRequest.of("deposit", 100.0)));

        // Assert
        assertThat(throwable)
                .hasMessage("Unable to process your request. Account does not exists")
                .asInstanceOf(InstanceOfAssertFactories.type(BankAppException.class));
    }

    @Test
    @DisplayName("Should throw an error when transaction type is not valid")
    void createLog_shouldThrowErrorWhenTransactionRequestTypeIsNotValid() {
        // Arrange
        Long accountId = 1L;
        Account account = mock(Account.class);

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.ofNullable(account));

        // Act
        final Throwable throwable = catchThrowable(() -> transactionLogService.createLogFor(accountId, TransactionRequest.of("invalid type", 100.0)));

        // Assert
        assertThat(throwable)
                .hasMessage("Invalid Transaction Type")
                .asInstanceOf(InstanceOfAssertFactories.type(BankAppException.class));
    }

    @Test
    @DisplayName("Should delete transaction log successfully")
    void delete_shouldDeleteTransaction() {
        // Arrange
        Long transactionId = 1L;
        TransactionLog transactionLog = mock(TransactionLog.class);

        when(transactionLogRepository.findById(transactionId))
                .thenReturn(Optional.ofNullable(transactionLog));

        // Act
        transactionLogService.delete(transactionId);

        // Assert
        verify(transactionLogRepository)
                .findById(transactionId);
        verify(transactionLogRepository)
                .delete(transactionLog);
    }

    @Test
    @DisplayName("Should throw an error when transaction log id doesn't exists on database")
    void delete_shouldThrowErrorWhenTransactionLogDoesntExists() {
        // Arrange

        // Act
        final Throwable throwable = catchThrowable(() -> transactionLogService.delete(1L));

        // Assert
        assertThat(throwable)
                .hasMessage("Unable to process your request. Transaction log does not exists")
                .asInstanceOf(InstanceOfAssertFactories.type(BankAppException.class));
    }
}
