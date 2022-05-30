package com.example.groupexercise2.model.dto;

import java.util.Objects;

import com.example.groupexercise2.model.Transaction;

public class TransactionDto extends BaseDto {

  private Long id;
  private String transactionType;
  private Double amount;
  private AccountDto account;

  public TransactionDto(Long id, String transactionType, Double amount, AccountDto account) {
    this.id = id;
    this.transactionType = transactionType;
    this.amount = amount;
    this.account = account;
  }

  public TransactionDto(Transaction transaction) {
    this(transaction.getId(), transaction.getTransactionType(), transaction.getAmount(),
        new AccountDto(transaction.getAccount()));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public AccountDto getAccount() {
    return account;
  }

  public void setAccount(AccountDto account) {
    this.account = account;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionDto that = (TransactionDto) o;
    return Objects.equals(id, that.id) && Objects.equals(transactionType,
        that.transactionType) && Objects.equals(amount, that.amount)
        && Objects.equals(account, that.account);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, transactionType, amount, account);
  }
}
