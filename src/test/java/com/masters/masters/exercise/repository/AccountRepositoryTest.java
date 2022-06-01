package com.masters.masters.exercise.repository;

import com.masters.masters.exercise.model.Account;
import com.masters.masters.exercise.model.CheckingAccount;
import com.masters.masters.exercise.model.InterestAccount;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    AccountRepository repo;
    
    @Test
    public void saveInterestAccountHappyPath(){
        InterestAccount account = new InterestAccount();
        account.setName("name");
        InterestAccount savedAccount = repo.save(account);
        Assertions.assertThat(savedAccount).extracting("name","interestCharge")
        .containsExactly("name",0.03);
    }
    
    @Test
    public void updateInterestAccountHappyPath(){
        InterestAccount account = new InterestAccount();
        account.setName("name");
        account.setBalance(500);
        InterestAccount savedAccount = repo.save(account);
        Assertions.assertThat(savedAccount).extracting("name","interestCharge")
        .containsExactly("name",0.03);
        savedAccount.setBalance(200);
        InterestAccount updatedAccount = repo.save(account);
        Assertions.assertThat(updatedAccount).extracting("name","balance")
        .containsExactly("name",200.0);
    }

    @Test
    public void saveCheckingAccountHappyPath(){
        CheckingAccount account = new CheckingAccount();
        account.setName("name");
        CheckingAccount newAccount = repo.save(account);
        Assertions.assertThat(newAccount).extracting("name","minimumBalance","penalty","transactionCharge","balance")
                .containsExactly("name",100.0,10.0,1.0,100.0);
    }

    @Test
    public void updateCheckingAccountHappyPath(){
        CheckingAccount account = new CheckingAccount();
        account.setName("name");
        account.setMinimumBalance(100);
        account.setPenalty(10);
        account.setTransactionCharge(1);
        CheckingAccount newAccount = repo.save(account);
        Assertions.assertThat(newAccount).extracting("name","minimumBalance","penalty","transactionCharge","balance")
                .containsExactly("name",100.0,10.0,1.0,100.0);
        newAccount.setBalance(90.0);
        CheckingAccount updatedAccount = repo.save(newAccount);
        Assertions.assertThat(updatedAccount).extracting("name","minimumBalance","penalty","transactionCharge","balance")
                .containsExactly("name",100.0,10.0,1.0,90.0);
    }

    @Test
    public void findById(){
        CheckingAccount account = new CheckingAccount();
        account.setId(Long.parseLong("1"));
        account.setName("name");
        account.setMinimumBalance(100);
        account.setPenalty(10);
        account.setTransactionCharge(1);
        CheckingAccount newAccount = repo.save(account);
        Optional<Account> existingAccountOption = repo.findById(newAccount.getId());
        Account account1 = existingAccountOption.get();
        Assertions.assertThat(account1).extracting("name","minimumBalance","penalty","transactionCharge","balance")
                .containsExactly("name",100.0,10.0,1.0,100.0);

    }
}
