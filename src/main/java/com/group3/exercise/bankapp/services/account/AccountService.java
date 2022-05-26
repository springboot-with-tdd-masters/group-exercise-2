package com.group3.exercise.bankapp.services.account;

import java.util.List;

import com.group3.exercise.bankapp.entities.Account;
import com.group3.exercise.bankapp.request.CreateAccountRequest;
import com.group3.exercise.bankapp.request.TransactionRequest;
import com.group3.exercise.bankapp.response.AccountResponse;

public interface AccountService {
    AccountResponse register(CreateAccountRequest request);
    AccountResponse withdraw(Long id, TransactionRequest request);
    AccountResponse deposit(Long id, TransactionRequest request);
    List<AccountResponse> getAllAccounts();
    AccountResponse getAccountById(Long id);
    Account deleteAccountById(Long id);
}
