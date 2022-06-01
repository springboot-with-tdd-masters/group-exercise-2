/**
 * 
 */
package com.masters.masters.exercise.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masters.masters.exercise.model.InterestAccount;
import com.masters.masters.exercise.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.masters.masters.exercise.exception.AmountExceededException;
import com.masters.masters.exercise.exception.RecordNotFoundException;
import com.masters.masters.exercise.model.Account;
import com.masters.masters.exercise.model.TransactionType;
import com.masters.masters.exercise.model.dto.TransactionDto;
import com.masters.masters.exercise.services.AccountServiceImpl;
import com.masters.masters.exercise.services.TransactionImpl;

/**
 * @author michaeldelacruz
 *
 */

@RestController
@RequestMapping("/accounts")
public class TransactionController {
	
	@Autowired
	private AccountServiceImpl accountService;
	
	@Autowired
	private TransactionImpl transactionService;

	@GetMapping("/{accountId}/transactions")
	public Page<Transaction> getAllTranscationByAccountId(@PathVariable(value = "accountId") Long accountId, Pageable pageable) throws RecordNotFoundException {
		Account acct = accountService.getAccountById(accountId);
		return transactionService.findByAccountId(acct, pageable);
	}

	@DeleteMapping("/{accountId}/transactions/{transactionId}")
	public ResponseEntity<?> deleteTransaction(@PathVariable(value = "accountId") Long accountId, @PathVariable(value = "transactionId") Long transactionId) throws RecordNotFoundException {
		transactionService.deleteTransactionByIdAndAccountId(transactionId, accountId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}/transactions")
	public ResponseEntity<Account> transactions(@PathVariable Long id, @RequestBody TransactionDto transaction) throws RecordNotFoundException, JsonProcessingException, AmountExceededException {
		Account response = null;
		Account account = accountService.getAccountById(id);
		String type = transaction.getType();

		if(type.equalsIgnoreCase(TransactionType.DEPOSIT.toString())) {
			accountService.transact(id, transaction);
			response = transactionService.deposit(account, transaction.getAmount());
		} else if(type.equalsIgnoreCase(TransactionType.WITHDRAW.toString())) {
			accountService.transact(id, transaction);
			response = transactionService.withdraw(account, transaction.getAmount());
		}
		
		return new ResponseEntity<Account>(response, new HttpHeaders(), HttpStatus.OK);
	}
	
}
