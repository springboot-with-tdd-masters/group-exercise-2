package com.group3.exercise.bankapp.repositories;

import com.group3.exercise.bankapp.entities.Account;
import com.group3.exercise.bankapp.entities.InterestAccount;
import com.group3.exercise.bankapp.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AccountRepositoryTest {


    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AccountRepository repository;


    @BeforeEach
    void setup(){
        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    void shouldSaveAsInterestAccount(){
        // given
        Account account = new InterestAccount();
        account.setBalance(100.0);
        account.setMinimumBalance(0.0);
        account.setInterestCharge(0.03);
        account.setTransactionCharge(0.0);
        account.setId(1L);
        account.setPenalty(0.0);
        account.setAcctNumber("123456789");
        account.setName("JOAN DOE");
        // when
        Account saved = repository.save(account);

        Account actual = testEntityManager.find(Account.class, saved.getId()); //repository.findById(saved.getId());
        // then
        assertEquals("interest", actual.getType());
    }
    @Test
    void shouldFindAsInterestAccount(){
        // given
        Account account = new InterestAccount();
        account.setBalance(100.0);
        account.setMinimumBalance(0.0);
        account.setInterestCharge(0.03);
        account.setTransactionCharge(0.0);
        account.setPenalty(0.0);
        account.setAcctNumber("123456789");
        account.setName("JOAN DOE");
        Account saved = testEntityManager.persist(account);
        // when
        Optional<Account> actual = repository.findById(saved.getId()); //repository.findById(saved.getId());
        Account actualGet = actual.get();
        // then
        assertEquals("interest", actualGet.getType());
    }
}
