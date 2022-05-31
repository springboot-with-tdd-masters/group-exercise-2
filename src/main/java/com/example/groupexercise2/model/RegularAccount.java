package com.example.groupexercise2.model;

import javax.persistence.Entity;


@Entity
public class RegularAccount extends Account {

	@Override
	public String getType() {
		return "regular";
	}

	@Override
	public void initialize(String name) {
		super.initialize(name);
		setMinimumBalance(500d);
		setBalance(getMinimumBalance());
		setPenalty(10d);
		setTransactionCharge(0d);
		setInterestCharge(0d);
	}
}
