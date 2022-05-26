package com.group3.exercise.bankapp.services;

import com.group3.exercise.bankapp.adapters.AccountAdapter;
import com.group3.exercise.bankapp.entities.InterestAccount;
import com.group3.exercise.bankapp.exceptions.BankAppException;
import com.group3.exercise.bankapp.exceptions.BankAppExceptionCode;
import com.group3.exercise.bankapp.repository.AccountRepository;
import com.group3.exercise.bankapp.request.TransactionRequest;
import com.group3.exercise.bankapp.response.AccountResponse;
import com.group3.exercise.bankapp.services.account.AccountService;
import com.group3.exercise.bankapp.services.account.AccountServiceImpl;
import com.group3.exercise.bankapp.services.transaction.TransactionStrategyNavigator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository repository;
    @Mock
    private TransactionStrategyNavigator navigator;
    @Mock
    private AccountAdapter adapter;

    private AccountService service;

    @BeforeEach
    void setup(){
        this.service = new AccountServiceImpl(navigator, adapter, repository);
    }

    @Test
    @DisplayName("Should call withdraw account.")
    void shouldCallWithdrawAccountSuccessfully(){
        // given
        TransactionRequest request = new TransactionRequest();
        request.setAmount(100.0);
        request.setType("withdraw");
        InterestAccount stubReturn = new InterestAccount();
        stubReturn.setBalance(100.0);
        InterestAccount updated = new InterestAccount();
        updated.setBalance(0.0);

        when(repository.findById(anyLong())).thenReturn(Optional.of(stubReturn));
        when(navigator.withdraw(stubReturn, 100.0)).thenReturn(updated);
        when(repository.save(updated)).thenReturn(updated);
        when(adapter.mapToResponse(updated)).thenReturn(new AccountResponse());
        // when
        AccountResponse actual = service.withdraw(1L, request);
        // then
        verify(repository, times(1)).findById(1L);
        verify(navigator, times(1)).withdraw(stubReturn, 100.0);
        verify(repository, times(1)).save(updated);
        verify(adapter, times(1)).mapToResponse(updated);
        assertNotNull(actual);
    }
    @Test
    @DisplayName("Should call deposit account.")
    void shouldCallDepositAccountSuccessfully(){
        // given
        TransactionRequest request = new TransactionRequest();
        request.setAmount(100.0);
        request.setType("withdraw");
        InterestAccount stubReturn = new InterestAccount();
        stubReturn.setBalance(100.0);
        InterestAccount updated = new InterestAccount();
        updated.setBalance(200.0);

        when(repository.findById(anyLong())).thenReturn(Optional.of(stubReturn));
        when(navigator.deposit(stubReturn, 100.0)).thenReturn(updated);
        when(repository.save(updated)).thenReturn(updated);
        when(adapter.mapToResponse(updated)).thenReturn(new AccountResponse());
        // when
        AccountResponse actual = service.deposit(1L, request);
        // then
        verify(repository, times(1)).findById(1L);
        verify(navigator, times(1)).deposit(stubReturn, 100.0);
        verify(repository, times(1)).save(updated);
        verify(adapter, times(1)).mapToResponse(updated);
        assertNotNull(actual);
    }

    @Test
    @DisplayName("should throw correct exception if unable to find account upon deposit")
    void shouldThrowTransactionExceptionIfUnableToFindAccountUponDeposit() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(100.0);
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BankAppException.class, () -> service.deposit(1L, request));
    }
    @Test
    @DisplayName("should throw correct exception if unable to find account upon withdraw")
    void shouldThrowTransactionExceptionIfUnableToFindAccountUponWithdraw() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(100.0);
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BankAppException.class, () -> service.withdraw(1L, request));
    }
    @Test
    @DisplayName("should throw correct Exception if unable to map account")
    void shouldThrowTransactionExceptionIfUnableToMapAccount() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(100.0);
        when(repository.findById(anyLong())).thenReturn(Optional.of(new InterestAccount()));
        when(navigator.deposit(any(InterestAccount.class), anyDouble())).thenReturn(new InterestAccount());
        when(adapter.mapToResponse(any(InterestAccount.class))).thenThrow(new BankAppException(BankAppExceptionCode.MAPPING_EXCEPTION));
        when(repository.save(any(InterestAccount.class))).thenReturn(new InterestAccount());
        BankAppException actual = assertThrows(BankAppException.class, () -> service.deposit(1L, request));
        assertEquals("Unable to map response", actual.getMessage());
    }
    @Test
    @DisplayName("should throw correct exception if invalid amount on withdraw")
    void shouldThrowCorrectExceptionIfWithdrawAndInvalidAmount() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(-100.0);
        BankAppException actual = assertThrows(BankAppException.class, () -> service.withdraw(1L, request));
        assertEquals("Please insert a valid amount", actual.getMessage());
    }
    @Test
    @DisplayName("should throw correct exception if invalid amount on deposit")
    void shouldThrowCorrectExceptionIfDepositAndInvalidAmount() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(-100.0);
        BankAppException actual = assertThrows(BankAppException.class, () -> service.deposit(1L, request));
        assertEquals("Please insert a valid amount", actual.getMessage());
    }
}
