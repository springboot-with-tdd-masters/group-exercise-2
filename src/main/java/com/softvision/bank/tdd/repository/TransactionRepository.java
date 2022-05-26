package com.softvision.bank.tdd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softvision.bank.tdd.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByAccountId(Long id);
}
