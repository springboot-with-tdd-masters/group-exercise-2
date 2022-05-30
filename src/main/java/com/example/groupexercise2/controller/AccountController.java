package com.example.groupexercise2.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.groupexercise2.model.dto.AccountDto;
import com.example.groupexercise2.model.dto.AccountRequestDto;
import com.example.groupexercise2.model.dto.TransactionRequestDto;
import com.example.groupexercise2.service.AccountService;

@RequestMapping("/accounts") 
@RestController
public class AccountController {
	
	@Autowired
	private AccountService accountService;

	
	@PostMapping
	public AccountDto createOrUpdateAccount(@RequestBody AccountRequestDto account) {
		if (account.getId() > 0){
			return accountService.updateAccount(account);
		}else{
			return accountService.createAccount(account);
		}
	}
	
	@GetMapping
	public Page<AccountDto> getAllAccounts(@RequestParam Map<String, String> params) {
		int page = Integer.valueOf(params.get("page"));
		int size = Integer.valueOf(params.get("size"));
		String[] sort = params.get("sort").split(",");
	
		String sortName = sort[0];
		Sort.Direction direction = Sort.Direction.fromString(sort[1]);
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortName));
		return accountService.getAllAccounts(pageable);
	}

	@GetMapping("/{id}")
	public AccountDto getAccount(@PathVariable long id) {
		return accountService.getAccount(id);
	}

	@DeleteMapping("/{id}")
	public void deleteAccount(@PathVariable long id) {
		accountService.deleteAccount(id);
	}
	
}
