package com.example.groupexercise2.model;

import java.time.LocalDate;

import javax.persistence.Entity;


@Entity
public class InterestAccount extends Account {

	@Override
	public String getType() {
		return "interest";
	}

	@Override
	public void initialize(String name) {
		super.initialize(name);
		setMinimumBalance(0d);
		setBalance(getMinimumBalance());
		setPenalty(0d);
		setTransactionCharge(0d);
		setInterestCharge(0.03);
		setCreatedDate(LocalDate.now());		
	}
}
