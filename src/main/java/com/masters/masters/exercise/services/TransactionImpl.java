/**
 * 
 */
package com.masters.masters.exercise.services;

import com.masters.masters.exercise.exception.RecordNotFoundException;
import com.masters.masters.exercise.model.CheckingAccount;
import com.masters.masters.exercise.model.Transaction;
import com.masters.masters.exercise.model.TransactionType;
import com.masters.masters.exercise.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.masters.masters.exercise.exception.AmountExceededException;
import com.masters.masters.exercise.model.Account;
import com.masters.masters.exercise.model.dto.TransactionDto;
import com.masters.masters.exercise.repository.AccountRepository;

import java.util.Optional;

/**
 * @author michaeldelacruz
 *
 */

@Service
public class TransactionImpl {

	@Autowired
	private AccountRepository repo;

	@Autowired
	private TransactionRepository transactionRepository;

	public Page<Transaction> findByAccountId(Long accountId, Pageable pageable) {
		return transactionRepository.findByAccountId(accountId, pageable);
	}

	public void deleteTransactionByIdAndAccountId(Long transactionId, Long accountId) throws RecordNotFoundException {
		Optional<Transaction> optionalTransaction = transactionRepository.findByIdAndAccountId(transactionId, accountId);
		if(optionalTransaction.isPresent()){
			transactionRepository.delete(optionalTransaction.get());
		} else {
			throw new RecordNotFoundException("Transaction not found with id " + transactionId + " and accountId " + accountId);
		}
	}

	public Transaction create(Account account, TransactionDto transactionDTO){
		Transaction transaction = Transaction.builder().amount(transactionDTO.getAmount()).type(TransactionType.valueOf(transactionDTO.getType())).account(account).build();
		return transactionRepository.save(transaction);
	}


	public Account withdraw(Account account, double amount) throws AmountExceededException {
		double updatedBalance = 0;

		if(amount > account.getBalance()) {
			throw new AmountExceededException("Insufficient Funds");
		}

		if(account instanceof  CheckingAccount){
			if(account.getBalance() < account.getMinimumBalance()){
				updatedBalance = account.getBalance() - account.getPenalty() - account.getTransactionCharge() - amount;
			}else{
				updatedBalance = account.getBalance() -  account.getTransactionCharge() - amount;
			}
		} else {
			updatedBalance = account.getBalance() - amount;
		}

		account.setBalance(updatedBalance);
		return repo.save(account);
	}

	public Account deposit(Account account, double amount) throws AmountExceededException {

		if(amount <= 0) {
			throw new AmountExceededException("Insufficient Funds");
		}

		Double updatedBalance = null;
		if(account instanceof CheckingAccount){
			updatedBalance  = account.getBalance() + amount - account.getTransactionCharge();
		} else{
			updatedBalance = account.getBalance() + amount;
		}

		account.setBalance(updatedBalance);
		return repo.save(account);
	}
	
}
