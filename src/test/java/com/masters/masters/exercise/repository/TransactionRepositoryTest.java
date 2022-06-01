package com.masters.masters.exercise.repository;

import com.masters.masters.exercise.model.Account;
import com.masters.masters.exercise.model.CheckingAccount;
import com.masters.masters.exercise.model.Transaction;
import com.masters.masters.exercise.model.TransactionType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    AccountRepository repo;

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void saveTransaction(){
        Transaction txn = new Transaction();
        CheckingAccount acct = new CheckingAccount();
        acct.setBalance(10.0);
        acct.setName("name");
        acct.setAcctNumber("123");
        Account savedAcct = repo.save(acct);
        txn.setAccount(savedAcct);
        txn.setType(TransactionType.WITHDRAW);
        txn.setAmount(10.0);
        Transaction savedTxn = transactionRepository.save(txn);
        Assertions.assertThat(savedTxn).extracting("type","amount")
                .containsExactly(TransactionType.WITHDRAW,10.0);
    }

    @Test
    public void findByAccount(){
        Transaction txn = new Transaction();
        CheckingAccount acct = new CheckingAccount();
        acct.setBalance(10.0);
        acct.setName("name");
        acct.setAcctNumber("123");
        Account savedAcct = repo.save(acct);
        txn.setAccount(savedAcct);
        txn.setType(TransactionType.WITHDRAW);
        txn.setAmount(10.0);
        transactionRepository.save(txn);
        Pageable pageable = PageRequest.of(0,20);
        Page<Transaction> transactionList = transactionRepository.findByAccount(savedAcct,pageable);
        org.junit.jupiter.api.Assertions.assertEquals(transactionList.get().count(),1);
    }

    @Test
    public void findByIdAndAccountId(){
        Transaction txn = new Transaction();
        CheckingAccount acct = new CheckingAccount();
        acct.setBalance(10.0);
        acct.setName("name");
        acct.setAcctNumber("123");
        Account savedAcct = repo.save(acct);
        txn.setAccount(savedAcct);
        txn.setType(TransactionType.WITHDRAW);
        txn.setAmount(10.0);
        Transaction newTransaction = transactionRepository.save(txn);
        Optional<Transaction> transactionOptional = transactionRepository.findByIdAndAccountId(savedAcct.getId(),newTransaction.getId());
        org.junit.jupiter.api.Assertions.assertTrue(transactionOptional.isPresent());
        org.junit.jupiter.api.Assertions.assertEquals(transactionOptional.get().getAmount(),10.0);
    }
}
