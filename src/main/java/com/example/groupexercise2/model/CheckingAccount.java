package com.example.groupexercise2.model;

import javax.persistence.Entity;

import com.example.groupexercise2.util.AccountGenerator;

@Entity
public class CheckingAccount extends Account {

	@Override
	public String getType() {
		return "checking";
	}

	@Override
	public void initialize(String name) {
		setName(name);
		setMinimumBalance(100d);
		setBalance(getMinimumBalance());
		setPenalty(10d);
		setTransactionCharge(1d);
		setInterestCharge(0d);
		setAcctNumber(AccountGenerator.generateAccountNumber());		
	}

}
