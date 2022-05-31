package com.group3.exercise.bankapp.services.transaction;

import com.group3.exercise.bankapp.request.TransactionRequest;

public interface TransactionWriter{

    void writeTransaction(Long account, TransactionRequest transaction);
}
