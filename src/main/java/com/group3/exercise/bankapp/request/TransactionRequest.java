package com.group3.exercise.bankapp.request;

import com.group3.exercise.bankapp.constants.TransactionTypes;

import java.util.Optional;

public class TransactionRequest {
    private String type;
    private Double amount;

    public TransactionRequest() {}

    private TransactionRequest(String type, Double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Optional<TransactionTypes> getTypeAsTransactionTypesOptional() {
        return TransactionTypes.findOptionalByLabel(this.type);
    }

    public static TransactionRequest of(String type, Double amount) {
        return new TransactionRequest(type, amount);
    }
}
