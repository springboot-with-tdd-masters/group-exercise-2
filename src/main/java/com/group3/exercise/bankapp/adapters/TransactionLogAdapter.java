package com.group3.exercise.bankapp.adapters;

import com.group3.exercise.bankapp.entities.TransactionLog;
import com.group3.exercise.bankapp.response.TransactionLogResponse;
import org.springframework.stereotype.Service;

@Service
public class TransactionLogAdapter {

    public TransactionLogResponse mapToResponse(TransactionLog transactionLog) {

        final TransactionLogResponse transactionLogResponse = new TransactionLogResponse();

        transactionLogResponse.setId(transactionLog.getId());
        transactionLogResponse.setTransactionType(transactionLog.getTransactionTypes().value());
        transactionLogResponse.setAmount(transactionLog.getAmount());
        transactionLogResponse.setAccountId(transactionLog.getAccount().getId());
        transactionLogResponse.setTransactionDate(transactionLog.getTransactionDate());

        return transactionLogResponse;
    }

}
