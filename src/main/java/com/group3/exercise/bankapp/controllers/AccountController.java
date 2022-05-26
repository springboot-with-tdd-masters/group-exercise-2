package com.group3.exercise.bankapp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.exercise.bankapp.constants.TransactionTypes;
import com.group3.exercise.bankapp.exceptions.BankAppException;
import com.group3.exercise.bankapp.exceptions.BankAppExceptionCode;
import com.group3.exercise.bankapp.request.CreateAccountRequest;
import com.group3.exercise.bankapp.request.TransactionRequest;
import com.group3.exercise.bankapp.response.AccountResponse;
import com.group3.exercise.bankapp.services.account.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public AccountResponse registerAccount(@RequestBody CreateAccountRequest request){
        return service.register(request);
    }
    
    @GetMapping()
    public List<AccountResponse> getAllAccounts() {
    	return service.getAllAccounts();
    }
    
    @GetMapping("/{id}")
    public AccountResponse getAccountById(@PathVariable("id") Long id) {
    	return service.getAccountById(id);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountById(@PathVariable("id") Long id) {
    	service.deleteAccountById(id);
    	return ResponseEntity.noContent().build();
    	
    }
    
    @PostMapping(path = "/{accountId}/transactions")
    public AccountResponse transact(@PathVariable("accountId") Long accountId, @RequestBody TransactionRequest request){
        if(TransactionTypes.isContaining(request.getType())){
            if(TransactionTypes.DEPOSIT.value().equals(request.getType())){
               return this.service.deposit(accountId, request);
            } else if(TransactionTypes.WITHDRAW.value().equals(request.getType())){
               return this.service.withdraw(accountId, request);
            }
        }
        throw new BankAppException(BankAppExceptionCode.TRANSACTION_TYPE_EXCEPTION);
    }
}
