package com.example.groupexercise1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.groupexercise1.model.dto.AccountDto;
import com.example.groupexercise1.model.dto.TransactionRequestDto;
import com.example.groupexercise1.service.TransactionService;

@RequestMapping("/accounts")
@RestController
public class TransactionController {

	@Autowired
	private TransactionService service;
	
	@GetMapping("/test")
	public String get() {
		return "test";
	}

	@PostMapping("/{accountId}/transactions")
	public AccountDto makeTransaction(@PathVariable long accountId,
			@RequestBody TransactionRequestDto transactRequest) {
		return service.createTransaction(accountId, transactRequest);
	}

	@DeleteMapping("/{accountId}/transactions/{transactionId}")
	public void deleteAccount(@PathVariable long accountId, @PathVariable long transactionId) {
		service.delete(accountId, transactionId);
	}
}
