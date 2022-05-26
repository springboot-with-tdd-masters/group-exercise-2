package com.group3.exercise.bankapp.entities;

import com.group3.exercise.bankapp.constants.TransactionTypes;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private TransactionTypes transactionTypes;

    private Double amount;

    @ManyToOne
    private Account account;

    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionTypes getTransactionTypes() {
        return transactionTypes;
    }

    public void setTransactionTypes(TransactionTypes transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
