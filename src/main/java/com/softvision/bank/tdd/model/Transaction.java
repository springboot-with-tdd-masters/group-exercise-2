package com.softvision.bank.tdd.model;

public class Transaction {

	private String type;
	private double amount;

	public Transaction() {
		super();
	}

	public Transaction(String type, double amount) {
		super();
		this.type = type;
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
