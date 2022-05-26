package com.group3.exercise.bankapp.services.transaction.impl;

import com.group3.exercise.bankapp.adapters.TransactionLogAdapter;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionLogServiceImpl implements TransactionLogService {

    private TransactionLogRepository transactionLogRepository;

    private AccountRepository accountRepository;

    private TransactionLogAdapter transactionLogAdapter;

    public TransactionLogServiceImpl(
            TransactionLogRepository transactionLogRepository,
            AccountRepository accountRepository,
            TransactionLogAdapter transactionLogAdapter) {
        this.transactionLogRepository = transactionLogRepository;
        this.accountRepository = accountRepository;
        this.transactionLogAdapter = transactionLogAdapter;
    }

    @Override
    public Page<TransactionLogResponse> findAllByAccountId(Long accountId, Pageable pageRequest) {

        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BankAppException(BankAppExceptionCode.ACCOUNT_NOT_FOUND_EXCEPTION));

        return transactionLogRepository.findByAccountId(account.getId(), pageRequest)
                .map(transactionLogAdapter::mapToResponse);
    }

    @Override
    public TransactionLogResponse createLogFor(Long accountId, TransactionRequest transactionRequest) {

        final Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BankAppException(BankAppExceptionCode.ACCOUNT_NOT_FOUND_EXCEPTION));

        final TransactionLog transactionLog = createFrom(account, transactionRequest);

        transactionLogRepository.save(transactionLog);

        return transactionLogAdapter.mapToResponse(transactionLog);
    }

    @Override
    public void delete(Long transactionId) {
        final TransactionLog transactionLog = transactionLogRepository.findById(transactionId)
                .orElseThrow(() -> new BankAppException(BankAppExceptionCode.TRANSACTION_LOG_NOT_FOUND_EXCEPTION));

        transactionLogRepository.delete(transactionLog);
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
