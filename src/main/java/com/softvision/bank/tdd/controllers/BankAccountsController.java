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
import com.softvision.bank.tdd.services.BankAccountsService;

@RestController
@RequestMapping("accounts")
public class BankAccountsController {

	@Autowired
	BankAccountsService bankAccountsService;

	@GetMapping("/{id}")
	public Account getById(@PathVariable("id") long id) {
		return bankAccountsService.get(id);
	}

	@GetMapping
	public List<Account> getAll() {
		return bankAccountsService.get();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteById(@PathVariable("id") long id) {
		bankAccountsService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping
	public ResponseEntity<Account> createUpdate(@RequestBody Account account) {
		account = bankAccountsService.createUpdate(account);
		return new ResponseEntity<Account>(account, new HttpHeaders(), HttpStatus.CREATED);
	}
}
