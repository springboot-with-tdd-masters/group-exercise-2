package com.group3.exercise.bankapp.repository;

import com.group3.exercise.bankapp.entities.TransactionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {

    Page<TransactionLog> findByAccountId(Long accountId, Pageable pageRequest);

}
