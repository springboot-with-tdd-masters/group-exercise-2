package com.softvision.bank.tdd.model;

import javax.persistence.Entity;

import com.softvision.bank.tdd.ApplicationConstants;

@Entity
public class InterestAccount extends Account {

	private static final long serialVersionUID = 6992815083192485723L;
	
	private double interestCharge;

	public InterestAccount() {
		super();
		this.interestCharge = ApplicationConstants.INT_INTEREST;
	}

	public double getInterestCharge() {
		return interestCharge;
	}

	public void setInterestCharge(double interestCharge) {
		this.interestCharge = interestCharge;
	}
}
