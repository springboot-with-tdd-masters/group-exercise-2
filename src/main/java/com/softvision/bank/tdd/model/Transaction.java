package com.softvision.bank.tdd.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class Transaction extends AuditModel{

	private static final long serialVersionUID = -7752501665975005880L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	
	private String type;
	
	private double amount;

	@ManyToOne
	@JsonIdentityReference(alwaysAsId= true)
	@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
	@JsonProperty("account_id")
	private Account account;
	
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
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
}
