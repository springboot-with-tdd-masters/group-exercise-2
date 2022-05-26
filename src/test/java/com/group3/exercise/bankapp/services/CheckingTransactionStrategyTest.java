package com.group3.exercise.bankapp.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.group3.exercise.bankapp.entities.Account;
import com.group3.exercise.bankapp.entities.CheckingAccount;
import com.group3.exercise.bankapp.services.transaction.impl.CheckingTransactionStrategy;

@ExtendWith(MockitoExtension.class)
public class CheckingTransactionStrategyTest {
	
	private final Double minimumBalance = 100.0;
	private final Double penalty = 10.0;
	private final Double transactionCharge = 1.0;
	private final Double interestCharge = 0.0;
	
	private CheckingTransactionStrategy checkingTxnStrategy;
	
	@BeforeEach
	void setUp() {
		checkingTxnStrategy = new CheckingTransactionStrategy(minimumBalance, penalty, transactionCharge, interestCharge);
	}
	
	@Test
	@DisplayName("should generate correct new checking account details")
	public void shouldGenerateNewCheckingAccountDetails() {
		//given
		String name = "Kobe Bryant";
	    String accountNumber = "123456789";
	    
	    //when
	    Account account = checkingTxnStrategy.generateNewAccountDetails(name, accountNumber);
	    
	    //then
		assertThat(account).extracting("minimumBalance", "penalty", "transactionCharge", "interestCharge")
				.containsExactly(minimumBalance, penalty, transactionCharge, interestCharge);
	}
	
	@Test
	@DisplayName("should withdraw and have the correct balance that is below minimum")
	public void shouldWithdrawAndHaveTheCorrectBalance_BelowMinimum() {
		//given
		CheckingAccount account = new CheckingAccount();
		account.setBalance(minimumBalance);
		account.setMinimumBalance(minimumBalance);
		account.setPenalty(penalty);
		account.setTransactionCharge(transactionCharge);
		
		//when
		CheckingAccount updatedAccount = checkingTxnStrategy.withdraw(account, 50.0);
		
		//then
		assertThat(updatedAccount).extracting("balance").isEqualTo(39.0);
	}
	
	@Test
	@DisplayName("should withdraw and have the correct balance that is below minimum")
	public void shouldWithdrawAndHaveTheCorrectBalance_AboveMinimum() {
		//given
		CheckingAccount account = new CheckingAccount();
		account.setBalance(200.0);
		account.setMinimumBalance(minimumBalance);
		account.setPenalty(penalty);
		account.setTransactionCharge(transactionCharge);
		
		//when
		CheckingAccount updatedAccount = checkingTxnStrategy.withdraw(account, 50.0);
		
		//then
		assertThat(updatedAccount).extracting("balance").isEqualTo(149.0);
	}
	
	@Test
	@DisplayName("should deposit and have the correct balance that is below minimum")
	public void shouldDepositAndHaveTheCorrectBalance_BelowMinimum() {
		//given
		CheckingAccount account = new CheckingAccount();
		account.setBalance(39.0);
		account.setMinimumBalance(minimumBalance);
		account.setPenalty(penalty);
		account.setTransactionCharge(transactionCharge);
		
		//when
		CheckingAccount updatedAccount = checkingTxnStrategy.deposit(account, 50.0);
		
		//then
		assertThat(updatedAccount).extracting("balance").isEqualTo(78.0);
	}
	
	@Test
	@DisplayName("should deposit and have the correct balance that is above minimum")
	public void shouldDepositAndHaveTheCorrectBalance_AboveMinimum() {
		//given
		CheckingAccount account = new CheckingAccount();
		account.setBalance(minimumBalance);
		account.setMinimumBalance(minimumBalance);
		account.setPenalty(penalty);
		account.setTransactionCharge(transactionCharge);
		
		//when
		CheckingAccount updatedAccount = checkingTxnStrategy.deposit(account, 50.0);
		
		//then
		assertThat(updatedAccount).extracting("balance").isEqualTo(149.0);
	}

}
