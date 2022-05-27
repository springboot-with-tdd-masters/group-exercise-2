package com.advancejava.groupexercise.controller;

import com.advancejava.groupexercise.entity.Account;
import com.advancejava.groupexercise.entity.BankTransaction;
import com.advancejava.groupexercise.model.dto.AccountRequest;
import com.advancejava.groupexercise.model.dto.DTORequest;
import com.advancejava.groupexercise.model.dto.DTOResponse;
import com.advancejava.groupexercise.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

    final static Logger logger = Logger.getLogger("AccountController");

    @Autowired
    private BankService bankService;

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account acct){
        logger.info("creating account...");
        return new ResponseEntity<>(bankService.createAccount(acct), HttpStatus.CREATED);
    }

    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable Integer id){
        logger.info(String.format("get account id %d", id));
        return bankService.getAccount(id);
    }

    @GetMapping("/accounts")
    public List<Account> getAccount(){
        logger.info("get all accounts...");
        return bankService.getAccounts();
    }

    @PostMapping("/accounts/{id}")
    public Account updateAccount(@RequestBody AccountRequest request, @PathVariable Integer id){
        logger.info(String.format("updating account id %d", id));
        Account account = bankService.updateAccount(request, id);

        return bankService.getAccount(account.getId());
    }

    @GetMapping("/accounts/{field}/{order}")
    private DTOResponse<Page<Account>> getAccountsWithPaginationAndSort(@RequestParam int page, @RequestParam int limit,
                                                                      @PathVariable String field, @PathVariable String order) {
        logger.info("getAccountsWithPaginationAndSort...");
        Page<Account> accounts = bankService.getAccountsWithPaginationAndSort(page, limit, field,order);
        return new DTOResponse<>(accounts.getSize(), accounts);
    }

    @DeleteMapping("/accounts/{id}")
    public void deleteAccount(@PathVariable Integer id) {
        logger.info(String.format("deleting account id %d", id));
        bankService.deleteAccount(id);
    }


}
