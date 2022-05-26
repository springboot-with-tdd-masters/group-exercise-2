package com.advancejava.groupexercise1.repository;

import com.advancejava.groupexercise1.entity.RegularAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testSave() {
        //arrange
        RegularAccount regularAccount = new RegularAccount();
        regularAccount.setName("John Doe");
        regularAccount.setMinimumBalance(500.00);
        //execute
        RegularAccount savedAccount = accountRepository.save(regularAccount);

        //test
        assertThat(savedAccount)
                .extracting("name", "minimumBalance")
                .containsExactly("John Doe", 500.00);
    }

    @Test
    public void testDeposit() {
        //arrange
        RegularAccount regularAccount = new RegularAccount();
        regularAccount.setId(1);
        regularAccount.setName("John Doe");
        regularAccount.setBalance(100.00 + 100.00);
        //execute
        RegularAccount savedAccount = accountRepository.save(regularAccount);

        //test
        assertThat(savedAccount)
                .extracting("id", "name", "balance")
                .containsExactly(3, "John Doe", 200.00);
    }

    @Test
    public void testWithdraw() {
        //arrange
        RegularAccount regularAccount = new RegularAccount();
        regularAccount.setId(1);
        regularAccount.setName("Will Smith");
        regularAccount.setBalance(500.00 - 200.00);
        //execute
        RegularAccount savedAccount = accountRepository.save(regularAccount);

        //test
        assertThat(savedAccount)
                .extracting("id", "name", "balance")
                .containsExactly(2, "Will Smith", 300.00);
    }
}
