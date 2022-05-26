package com.masters.masters.exercise.controller;

import com.masters.masters.exercise.exception.AccountExistException;
import com.masters.masters.exercise.exception.InvalidTypeException;
import com.masters.masters.exercise.exception.RecordNotFoundException;
import com.masters.masters.exercise.model.Account;
import com.masters.masters.exercise.model.dto.AccountDto;
import com.masters.masters.exercise.services.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;

    //get all accounts
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() throws RecordNotFoundException {
        List<Account> list = accountService.getAllAccounts();
        return new ResponseEntity<List<Account>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    // create
    @PostMapping
    public ResponseEntity<Account> createOrUpdateAccount(@RequestBody AccountDto accountDto) throws RecordNotFoundException, InvalidTypeException, AccountExistException {
        Account updated = accountService.createOrUpdateAccount(accountDto);
        return new ResponseEntity<Account>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    // get account by id
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) throws RecordNotFoundException {
    	 Account account = accountService.getAccountById(id);
        return new ResponseEntity<Account>(account, new HttpHeaders(), HttpStatus.OK);
    }
    
    // delete account by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Account> deleteAccountById(@PathVariable Long id) throws RecordNotFoundException {
    	 Account account = accountService.deleteAccountById(id);
        return new ResponseEntity<Account>(account, new HttpHeaders(), HttpStatus.OK);
    }
    
    
    
    // get account by acctNumber
    //withdraw/deposit
    //delete
}
