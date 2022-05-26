package com.group3.exercise.bankapp.services;

import com.group3.exercise.bankapp.entities.RegularAccount;
import com.group3.exercise.bankapp.services.transaction.impl.RegularTransactionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class RegularTransactionStrategyTest {

    private final static Double EXPECTED_INTEREST_CHARGE = 0d;
    private final static Double EXPECTED_MINIMUM_BALANCE = 500d;
    private final static Double EXPECTED_PENALTY = 10d;

    private RegularTransactionStrategy regularTransactionStrategy;

    @BeforeEach
    void setUp() {
        regularTransactionStrategy = new RegularTransactionStrategy(
                EXPECTED_INTEREST_CHARGE,
                EXPECTED_MINIMUM_BALANCE,
                EXPECTED_PENALTY
        );
    }

    @Test
    @DisplayName("Should generate new account details with proper values")
    void shouldGenerateNewAccountWithProperValues() {
        // Arrange
        String name = "James";
        String accountNumber = "54321";

        // Act
        final RegularAccount regularAccount = regularTransactionStrategy.generateNewAccountDetails(name, accountNumber);

        // Assert
        assertThat(regularAccount)
                .isNotNull();
        assertThat(regularAccount)
                .extracting("name", "acctNumber")
                .containsExactly(name, accountNumber);
        assertThat(regularAccount)
                .extracting("interestCharge", "minimumBalance", "penalty", "balance")
                .containsExactly(EXPECTED_INTEREST_CHARGE, EXPECTED_MINIMUM_BALANCE, EXPECTED_PENALTY, 500.0);
    }

    @Test
    @DisplayName("Given amount to withdraw, should deduct amount to balance")
    void shouldDeductAmountToBalance() {
        // Arrange
        RegularAccount regularAccountSub = new RegularAccount();
        regularAccountSub.setPenalty(EXPECTED_PENALTY);
        regularAccountSub.setMinimumBalance(EXPECTED_MINIMUM_BALANCE);
        regularAccountSub.setBalance(1000.0);

        Double withdrawAmount = 100.0;

        // Act
        final RegularAccount regularAccount = regularTransactionStrategy.withdraw(regularAccountSub, withdrawAmount);

        // Assert
        assertThat(regularAccount)
                .isNotNull();
        assertThat(regularAccount)
                .extracting("balance")
                .isEqualTo(900.0);
    }

    @Test
    @DisplayName("Should apply penalty when balance reach below minimum balance")
    void shouldApplyPenaltyWhenBalanceReachBelowMinimumBalance() {
        // Arrange
        RegularAccount regularAccountSub = new RegularAccount();
        regularAccountSub.setPenalty(EXPECTED_PENALTY);
        regularAccountSub.setMinimumBalance(EXPECTED_MINIMUM_BALANCE);
        regularAccountSub.setBalance(500.0);

        Double withdrawAmount = 100.0;

        // Act
        final RegularAccount regularAccount = regularTransactionStrategy.withdraw(regularAccountSub, withdrawAmount);

        // Assert
        assertThat(regularAccount)
                .isNotNull();
        assertThat(regularAccount)
                .extracting("balance", "penalty", "minimumBalance")
                .containsExactly(390.0, EXPECTED_PENALTY, EXPECTED_MINIMUM_BALANCE);
    }

    @Test
    @DisplayName("Should add amount to balance")
    void shouldAddAmountToBalance() {
        // Arrange
        RegularAccount regularAccountSub = new RegularAccount();
        regularAccountSub.setBalance(200.0);

        Double depositAmount = 100.0;

        // Act
        final RegularAccount regularAccount = regularTransactionStrategy.deposit(regularAccountSub, depositAmount);

        // Assert
        assertThat(regularAccount)
                .isNotNull();
        assertThat(regularAccount)
                .extracting("balance")
                .isEqualTo(300.0);
    }
}
