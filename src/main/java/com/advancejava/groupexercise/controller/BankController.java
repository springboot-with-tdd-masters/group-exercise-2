package com.advancejava.groupexercise.controller;

import com.advancejava.groupexercise.entity.Account;
import com.advancejava.groupexercise.entity.BankTransaction;
import com.advancejava.groupexercise.model.dto.AccountRequest;
import com.advancejava.groupexercise.model.dto.DTOResponse;
import com.advancejava.groupexercise.service.BankService;
import com.advancejava.groupexercise.model.dto.DTORequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

/*    @PostMapping("/accounts/{id}/transactions")
    public Account depositAccount(@RequestBody DTORequest deposit, @PathVariable Integer id){
        bankService.updateAccount(deposit,id);
        return bankService.getAccount(id);
    }*/
    @DeleteMapping("/accounts/{id}")
    public void deleteAccount(@PathVariable Integer id) {
        bankService.deleteAccount(id);
    }

    @PostMapping("/accounts/{id}")
    public Account updateAccount(@RequestBody AccountRequest request,
                                 @PathVariable Integer id){

        Account account = bankService.updateAccount(request, id);

        return bankService.getAccount(account.getId());
    }

    @PutMapping("/accounts/{id}/transactions")
    public Account txnAccount(@RequestBody DTORequest request, @PathVariable Integer id){
        //Account account = bankService.getAccount(id);
        bankService.updateTransaction(request,id);
        return bankService.getAccount(id);
    }

    @GetMapping("/accounts/{field}/{order}")
    private DTOResponse<Page<Account>> getAccountsWithPaginationAndSort(@RequestParam int page, @RequestParam int limit,
                                                                      @PathVariable String field, @PathVariable String order) {
        Page<Account> accounts = bankService.getAccountsWithPaginationAndSort(page, limit, field,order);
        return new DTOResponse<>(accounts.getSize(), accounts);
    }
    @GetMapping("/transactions/{field}/{order}")
    private DTOResponse<Page<BankTransaction>> getBankTxnsWithPaginationAndSort(@RequestParam int page, @RequestParam int limit,
                                                                                @PathVariable String field, @PathVariable String order) {
        Page<BankTransaction> txn = bankService.getBankTxnsWithPaginationAndSort(page, limit, field,order);
        return new DTOResponse<>(txn.getSize(), txn);
    }
}
