package com.softvision.bank.tdd.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping
	public ResponseEntity<List<Transaction>> getAllTransactions(@PathVariable("id") long id) {
		return new ResponseEntity<List<Transaction>>(transactionService.findbyAccountId(id), new HttpHeaders(),
				HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Account> transact(@PathVariable("id") long id, @RequestBody Transaction transaction) {
		return new ResponseEntity<>(transactionService.transact(id, transaction), new HttpHeaders(), HttpStatus.OK);
	}

	@DeleteMapping("/{transaction_id}")
	public ResponseEntity<Object> deleteById(@PathVariable("id") long accountId, @PathVariable("transaction_id") long transactionId) {
		transactionService.deleteTransactionById(accountId, transactionId);
		return ResponseEntity.noContent().build();
	}

}
