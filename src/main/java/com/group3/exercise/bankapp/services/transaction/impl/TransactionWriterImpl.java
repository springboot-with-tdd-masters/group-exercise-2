package com.group3.exercise.bankapp.services.transaction.impl;

import com.group3.exercise.bankapp.constants.TransactionTypes;
import com.group3.exercise.bankapp.entities.Account;
import com.group3.exercise.bankapp.entities.TransactionLog;
import com.group3.exercise.bankapp.exceptions.BankAppException;
import com.group3.exercise.bankapp.exceptions.BankAppExceptionCode;
import com.group3.exercise.bankapp.repository.AccountRepository;
import com.group3.exercise.bankapp.repository.TransactionLogRepository;
import com.group3.exercise.bankapp.request.TransactionRequest;
import com.group3.exercise.bankapp.response.TransactionLogResponse;
import com.group3.exercise.bankapp.services.transaction.TransactionLogService;
import com.group3.exercise.bankapp.services.transaction.TransactionWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/** Separate from the Transaction Log Service to separate responsibility.
 *
 */
@Service
@Slf4j
public class TransactionWriterImpl implements TransactionWriter {

    private TransactionLogService service;
    public TransactionWriterImpl(
            TransactionLogService transactionLogService
    ){
        this.service = transactionLogService;
    }
    @Override
    public void writeTransaction(Long accountId, TransactionRequest transaction) {
        try {
            TransactionLogResponse response = service.createLogFor(accountId, transaction);
        } catch (BankAppException e){
            log.debug("BankAppException thrown when logging for account id : {}, message: {}", accountId, e.getMessage());
        } catch (Exception e){
            log.debug("Exception thrown when logging for account id : {}, message: {}", accountId, e.getMessage());
        }
    }
}
