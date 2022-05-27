package com.advancejava.groupexercise.repository;

import com.advancejava.groupexercise.entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTxnRepository extends JpaRepository<BankTransaction,Long> {
}
