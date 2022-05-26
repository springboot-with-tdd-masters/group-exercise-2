package com.group3.exercise.bankapp.services.transaction.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.group3.exercise.bankapp.entities.CheckingAccount;
import com.group3.exercise.bankapp.services.transaction.TransactionStrategy;

@Service
public class CheckingTransactionStrategy implements TransactionStrategy<CheckingAccount>{
	
	private final Double minimumBalance;
	private final Double penalty;
	private final Double transactionCharge;
	private final Double interestCharge;

    public CheckingTransactionStrategy(@Value("${checking.minimumBal}") Double minimumBalance,
    		@Value("${checking.penalty}") Double penalty,
    		@Value("${checking.transactionCharge}") Double transactionCharge,
            @Value("${checking.interestCharge}") Double interestCharge
            ){
    	this.minimumBalance = minimumBalance;
    	this.penalty = penalty;
    	this.transactionCharge = transactionCharge;
        this.interestCharge = interestCharge;
        
    }

	@Override
	public CheckingAccount generateNewAccountDetails(String name, String acctNumber) {
		CheckingAccount newAccount = new CheckingAccount();
		newAccount.setName(name);
		newAccount.setAcctNumber(acctNumber);
		newAccount.setBalance(minimumBalance);
		newAccount.setMinimumBalance(minimumBalance);
		newAccount.setPenalty(penalty);
		newAccount.setTransactionCharge(transactionCharge);
		newAccount.setInterestCharge(interestCharge);
		return newAccount;
	}

	@Override
	public CheckingAccount withdraw(CheckingAccount account, Double amount) {
		Double updatedBalance = account.getBalance() - amount;
		updatedBalance = updatedBalance - account.getTransactionCharge();
		
		if (updatedBalance < account.getMinimumBalance()) {
			updatedBalance = updatedBalance - account.getPenalty();
		}
		
		account.setBalance(updatedBalance);
		
		return account;
	}

	@Override
	public CheckingAccount deposit(CheckingAccount account, Double amount) {
		Double updatedBalance = account.getBalance() + amount;
		updatedBalance = updatedBalance - account.getTransactionCharge();
		
		if (updatedBalance < account.getMinimumBalance()) {
			updatedBalance = updatedBalance - account.getPenalty();
		}
		
		account.setBalance(updatedBalance);
		
		return account;
	}

}
