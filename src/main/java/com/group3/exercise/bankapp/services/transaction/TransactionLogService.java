package com.group3.exercise.bankapp.services.transaction;

import com.group3.exercise.bankapp.request.TransactionRequest;
import com.group3.exercise.bankapp.response.TransactionLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionLogService {

    Page<TransactionLogResponse> findAllByAccountId(Long accountId, Pageable pageRequest);

    TransactionLogResponse createLogFor(Long accountId, TransactionRequest transactionRequest);

    void delete(Long transactionId);
}
