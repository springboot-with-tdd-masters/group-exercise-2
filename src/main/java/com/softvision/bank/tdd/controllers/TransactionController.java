package com.softvision.bank.tdd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.bank.tdd.model.Account;
import com.softvision.bank.tdd.model.Transaction;
import com.softvision.bank.tdd.services.TransactionService;

@RestController
@RequestMapping("accounts/{id}/transactions")
public class TransactionController {

	@Autowired
	TransactionService transactionService;

	@PostMapping
	public ResponseEntity<Account> transact(@PathVariable("id") long id, @RequestBody Transaction transaction) {
		return new ResponseEntity<>(transactionService.transact(id, transaction), new HttpHeaders(), HttpStatus.OK);
	}

}
