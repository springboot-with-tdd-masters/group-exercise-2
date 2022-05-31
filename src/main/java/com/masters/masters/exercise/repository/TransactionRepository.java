package com.masters.masters.exercise.repository;

import com.masters.masters.exercise.model.Account;
import com.masters.masters.exercise.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction,Long> {

    Page<Transaction> findByAccount(Account account, Pageable pageRequest);
    Optional<Transaction> findByIdAndAccountId(Long transactionId, Long accountId);
}
