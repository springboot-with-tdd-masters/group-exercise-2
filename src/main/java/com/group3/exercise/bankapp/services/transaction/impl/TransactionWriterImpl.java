package com.group3.exercise.bankapp.services.transaction.impl;

import com.group3.exercise.bankapp.constants.TransactionTypes;
import com.group3.exercise.bankapp.entities.Account;
import com.group3.exercise.bankapp.entities.TransactionLog;
import com.group3.exercise.bankapp.exceptions.BankAppException;
import com.group3.exercise.bankapp.exceptions.BankAppExceptionCode;
import com.group3.exercise.bankapp.repository.AccountRepository;
import com.group3.exercise.bankapp.repository.TransactionLogRepository;
import com.group3.exercise.bankapp.request.TransactionRequest;
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

    private TransactionLogRepository transactionLogRepository;
    private AccountRepository accountRepository;
    public TransactionWriterImpl(TransactionLogRepository repository, AccountRepository accountRepository){
        this.transactionLogRepository = repository;
        this.accountRepository = accountRepository;
    }
    @Override
    public void writeTransaction(Long accountId, TransactionRequest transaction) {
        try {
            final Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new BankAppException(BankAppExceptionCode.ACCOUNT_NOT_FOUND_EXCEPTION));

            final TransactionLog transactionLog = createFrom(account, transaction);

            transactionLogRepository.save(transactionLog);
        } catch (BankAppException e){
            log.debug("BankAppException thrown when logging for account id : {}, message: {}", accountId, e.getMessage());
        } catch (Exception e){
            log.debug("Exception thrown when logging for account id : {}, message: {}", accountId, e.getMessage());
        }
    }
    private TransactionLog createFrom(Account account, TransactionRequest transactionRequest) {

        final TransactionTypes transactionType = transactionRequest.getTypeAsTransactionTypesOptional()
                .orElseThrow(() -> new BankAppException(BankAppExceptionCode.TRANSACTION_TYPE_EXCEPTION));

        TransactionLog transactionLog = new TransactionLog();

        transactionLog.setTransactionTypes(transactionType);
        transactionLog.setAccount(account);
        transactionLog.setAmount(transactionRequest.getAmount());
        transactionLog.setTransactionDate(new Date());

        return transactionLog;
    }
}
