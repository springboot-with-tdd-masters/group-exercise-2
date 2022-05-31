package com.group3.exercise.bankapp.repositories;

import com.group3.exercise.bankapp.constants.TransactionTypes;
import com.group3.exercise.bankapp.entities.Account;
import com.group3.exercise.bankapp.entities.InterestAccount;
import com.group3.exercise.bankapp.entities.TransactionLog;
import com.group3.exercise.bankapp.repository.AccountRepository;
import com.group3.exercise.bankapp.repository.TransactionLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TransactionLogRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TransactionLogRepository repository;

    @Test
    @DisplayName("Should only delete transaction for given accountId")
    void shouldDeleteByAccountIdSuccessfully(){
        Account account = new InterestAccount();
        account.setBalance(100.0);
        account.setMinimumBalance(0.0);
        account.setInterestCharge(0.03);
        account.setTransactionCharge(0.0);
        account.setPenalty(0.0);
        account.setAcctNumber("123456789");
        account.setName("JOAN DOE");
        Account account2 = new InterestAccount();
        account2.setBalance(100.0);
        account2.setMinimumBalance(0.0);
        account2.setInterestCharge(0.03);
        account2.setTransactionCharge(0.0);
        account2.setPenalty(0.0);
        account2.setAcctNumber("123456789");
        account2.setName("JOANNE DOE");

        Account forDeletion = testEntityManager.persist(account);
        testEntityManager.persist(account2);

        TransactionLog log = new TransactionLog();
        log.setAccount(account);
        log.setAmount(100.0);
        log.setTransactionTypes(TransactionTypes.WITHDRAW);
        log.setCreatedDate(new Date());
        log.setLastModifiedDate(new Date());

        TransactionLog log2 = new TransactionLog();
        log2.setAccount(account);
        log2.setAmount(100.0);
        log2.setTransactionTypes(TransactionTypes.WITHDRAW);
        log2.setCreatedDate(new Date());
        log2.setLastModifiedDate(new Date());

        TransactionLog log3 = new TransactionLog();
        log3.setAccount(account2); // use account 2
        log3.setAmount(100.0);
        log3.setTransactionTypes(TransactionTypes.WITHDRAW);
        log3.setCreatedDate(new Date());
        log3.setLastModifiedDate(new Date());
        testEntityManager.persist(log);
        testEntityManager.persist(log2);
        testEntityManager.persist(log3);

        // when
        repository.deleteByAccountId(forDeletion.getId());
        List<TransactionLog> actual = repository.findAll();
        assertEquals(1, actual.size());
    }

}
