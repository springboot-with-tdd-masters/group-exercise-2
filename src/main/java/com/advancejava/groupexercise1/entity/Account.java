package com.advancejava.groupexercise1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sun.istack.NotNull;

import javax.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RegularAccount.class, name = "regular"),
        @JsonSubTypes.Type(value = CheckingAccount.class, name = "checking"),
        @JsonSubTypes.Type(value = InterestAccount.class, name = "interest")}
        )
public abstract class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonIgnore
    private String type;

    @NotNull
    protected String name;

    @NotNull
    protected String acctNumber;

    @NotNull
    protected Double balance;

    @NotNull
    protected Double minimumBalance;

    @NotNull
    protected Double penalty;

    @NotNull
    protected Double transactionCharge;

    @NotNull
    protected Double interestCharge;

    public Account() {

    }

    public Account(String acctNumber, Double balance, Double minimumBalance, Double penalty, Double transactionCharge, Double interestCharge) {
        this.acctNumber = acctNumber;
        this.balance = balance;
        this.minimumBalance = minimumBalance;
        this.penalty = penalty;
        this.transactionCharge = transactionCharge;
        this.interestCharge = interestCharge;
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(Integer id) {
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
}
