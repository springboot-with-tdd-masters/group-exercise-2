package com.example.groupexercise1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.groupexercise1.model.dto.AccountDto;
import com.example.groupexercise1.model.dto.AccountRequestDto;
import com.example.groupexercise1.model.dto.TransactionRequestDto;
import com.example.groupexercise1.service.AccountService;

@RequestMapping("/accounts") 
@RestController
public class AccountController {
	
	@Autowired
	private AccountService accountService;

	
	@PostMapping
	public AccountDto createAccount(@RequestBody AccountRequestDto account) {
		return accountService.createAccount(account);
	}
	
	@GetMapping
	public List<AccountDto> getAllAccounts() {
		return accountService.getAllAccounts();
	}

	@GetMapping("/{id}")
	public AccountDto getAccount(@PathVariable long id) {
		return accountService.getAccount(id);
	}

	@PostMapping("/{id}/transactions")
	public AccountDto makeTransaction(@PathVariable long id, 
			@RequestBody TransactionRequestDto transactRequest) {
		return accountService.createTransaction(transactRequest.getType(), id, transactRequest.getAmount());
	}	
	
	@DeleteMapping("/{id}")
	public void deleteAccount(@PathVariable long id) {
		accountService.deleteAccount(id);
	}
	
}
