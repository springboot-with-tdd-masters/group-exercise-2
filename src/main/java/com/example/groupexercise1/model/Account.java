package com.example.groupexercise1.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes({
	@Type(value = RegularAccount.class, name = "regular"),
	@Type(value = CheckingAccount.class, name = "checking"),
	@Type(value = InterestAccount.class, name = "interest")
})
public abstract class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String acctNumber;
	private Double balance;
	private Double minimumBalance;
	private Double penalty;
	private Double transactionCharge;
	private Double interestCharge;
	@JsonIgnore
	private LocalDate createdDate;
	
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
	
	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public abstract String getType();

	@Override
	public int hashCode() {
		return Objects.hash(acctNumber, balance, id, interestCharge, minimumBalance, name, penalty, transactionCharge);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return Objects.equals(acctNumber, other.acctNumber) && Objects.equals(balance, other.balance)
				&& Objects.equals(id, other.id) && Objects.equals(interestCharge, other.interestCharge)
				&& Objects.equals(minimumBalance, other.minimumBalance) && Objects.equals(name, other.name)
				&& Objects.equals(penalty, other.penalty) && Objects.equals(transactionCharge, other.transactionCharge);
	}
}
