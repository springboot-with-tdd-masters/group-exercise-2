package com.example.groupexercise1.model.dto;

import java.util.Objects;

import com.example.groupexercise1.model.Account;

public class AccountDto {
	
	private String type;
	private Long id;
	private String name;
	private String acctNumber;
	private Double balance;
	private Double minimumBalance;
	private Double penalty;
	private Double transactionCharge;
	private Double interestCharge;
	
	public AccountDto() {}
	
	public AccountDto(String type, Long id, String name, String acctNumber, Double balance, Double minimumBalance, Double penalty,
			Double transactionCharge, Double interestCharge) {
		this.type = type;
		this.id = id;
		this.name = name;
		this.acctNumber = acctNumber;
		this.balance = balance;
		this.minimumBalance = minimumBalance;
		this.penalty = penalty;
		this.transactionCharge = transactionCharge;
		this.interestCharge = interestCharge;
	}
	
	public AccountDto(Account account) {
		this(account.getType(), account.getId(), account.getName(), account.getAcctNumber(), account.getBalance(),
				account.getMinimumBalance(), account.getPenalty(), account.getTransactionCharge(), 
				account.getInterestCharge());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAcctNumber() {
		return acctNumber;
	}
	
	public void setAcctNumber(String acctNumber) {
		this.acctNumber = acctNumber;
	}
	
	public Double getBalance() {
		return balance;
	}
	
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	public Double getMinimumBalance() {
		return minimumBalance;
	}
	
	public void setMinimumBalance(Double minimumBalance) {
		this.minimumBalance = minimumBalance;
	}
	
	public Double getPenalty() {
		return penalty;
	}
	
	public void setPenalty(Double penalty) {
		this.penalty = penalty;
	}
	
	public Double getTransactionCharge() {
		return transactionCharge;
	}
	
	public void setTransactionCharge(Double transactionCharge) {
		this.transactionCharge = transactionCharge;
	}
	
	public Double getInterestCharge() {
		return interestCharge;
	}
	
	public void setInterestCharge(Double interestCharge) {
		this.interestCharge = interestCharge;
	}

	@Override
	public int hashCode() {
		return Objects.hash(acctNumber, balance, id, interestCharge, minimumBalance, name, penalty, transactionCharge,
				type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountDto other = (AccountDto) obj;
		return Objects.equals(acctNumber, other.acctNumber) && Objects.equals(balance, other.balance)
				&& Objects.equals(id, other.id) && Objects.equals(interestCharge, other.interestCharge)
				&& Objects.equals(minimumBalance, other.minimumBalance) && Objects.equals(name, other.name)
				&& Objects.equals(penalty, other.penalty) && Objects.equals(transactionCharge, other.transactionCharge)
				&& Objects.equals(type, other.type);
	}
}
