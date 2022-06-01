package com.softvision.bank.tdd.model;

import javax.persistence.Entity;

import com.softvision.bank.tdd.ApplicationConstants;

@Entity
public class RegularAccount extends Account {

	private static final long serialVersionUID = 5398958953595428116L;
	
	private double balance;
	private double minimumBalance;
	private double penalty;
	
	
	public RegularAccount() {
		super();
		this.balance = ApplicationConstants.REG_MIN_BALANCE;
		this.minimumBalance = ApplicationConstants.REG_MIN_BALANCE;
		this.penalty = ApplicationConstants.REG_PENALTY;
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

}
