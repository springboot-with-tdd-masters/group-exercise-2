package com.softvision.bank.tdd.model;

import javax.persistence.Entity;

import com.softvision.bank.tdd.ApplicationConstants;

@Entity
public class CheckingAccount extends Account {

	private static final long serialVersionUID = -3325751415600074562L;
	
	private double balance;

	private double minimumBalance;

	private double penalty;

	private double transactionCharge;

	public CheckingAccount() {
		super();
		this.balance = ApplicationConstants.CHK_MIN_BALANCE;
		this.minimumBalance = ApplicationConstants.CHK_MIN_BALANCE;
		this.penalty = ApplicationConstants.CHK_PENALTY;
		this.transactionCharge = ApplicationConstants.CHK_CHARGE;
	}
	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getMinimumBalance() {
		return minimumBalance;
	}

	public void setMinimumBalance(double minimumBalance) {
		this.minimumBalance = minimumBalance;
	}

	public double getPenalty() {
		return penalty;
	}

	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}

	public double getTransactionCharge() {
		return transactionCharge;
	}

	public void setTransactionCharge(double transactionCharge) {
		this.transactionCharge = transactionCharge;
	}
}
