package com.advancejava.groupexercise.controller;

import com.advancejava.groupexercise.entity.Account;
import com.advancejava.groupexercise.entity.AccountTransactions;
import com.advancejava.groupexercise.service.BankService;
import com.advancejava.groupexercise.model.dto.DTORequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account acct){
        return new ResponseEntity<>(bankService.createAccount(acct), HttpStatus.CREATED);
    }

    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable Integer id){ return bankService.getAccount(id); }

    @GetMapping("/accounts")
    public List<Account> getAccount(){ return bankService.getAccounts(); }

    @PostMapping("/accounts/{id}/transactions")
    public Account depositAccount(@RequestBody DTORequest deposit, @PathVariable Integer id){
        bankService.updateAccount(deposit,id);
        return bankService.getAccount(id);
    }
    @DeleteMapping("/accounts/{id}")
    public void deleteAccount(@PathVariable Integer id) {
        bankService.deleteAccount(id);
    }

//    @PutMapping("/accounts")
//    public AccountTransactions updateAccount(@RequestBody DTORequest request){
//
//        AccountTransactions accountTxns = bankService.updateAccount(request, id);
//
//        return bankService.getTxn(accountTxns.getId());
//    }

    @PutMapping("/accounts/{id}/transactions")
    public Account txnAccount(@RequestBody DTORequest request, @PathVariable Integer id){
        Account account = bankService.getAccount(id);
        bankService.updateTxn(account,request,id);
        return account;
    }
}
