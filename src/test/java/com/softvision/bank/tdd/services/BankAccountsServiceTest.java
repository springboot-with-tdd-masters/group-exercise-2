package com.softvision.bank.tdd.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.softvision.bank.tdd.AccountMocks;
import com.softvision.bank.tdd.exceptions.BadRequestException;
import com.softvision.bank.tdd.model.Account;
import com.softvision.bank.tdd.model.RegularAccount;
import com.softvision.bank.tdd.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
public class BankAccountsServiceTest {

	@Mock
	AccountRepository accountRepository;
	
	@InjectMocks
	private BankAccountsService bankAccountsService = new BankAccountsServiceImpl();
	
	@Captor
	ArgumentCaptor<Account> saveAccountCaptor;
	
	@Test
	@DisplayName("Should Create Regular Account")
	void test_create_regular_account() {
		Account regularAccount = AccountMocks.getMockRegularAccount();
		regularAccount.setBalance(500.00);
		
		when(accountRepository.save(saveAccountCaptor.capture())).thenReturn(regularAccount);
		
		Account accountActual = bankAccountsService.createUpdate(regularAccount);
		
		assertAll(() -> assertEquals(saveAccountCaptor.getValue().getBalance(), accountActual.getBalance()),
				() -> assertEquals(saveAccountCaptor.getValue().getInterestCharge(), accountActual.getInterestCharge()),
				() -> assertEquals(saveAccountCaptor.getValue().getMinimumBalance(), accountActual.getMinimumBalance()),
				() -> assertEquals(saveAccountCaptor.getValue().getPenalty(), accountActual.getPenalty()),
				() -> assertEquals(saveAccountCaptor.getValue().getTransactionCharge(), accountActual.getTransactionCharge()));
	}
	
	@Test
	@DisplayName("Should Create Checking Account")
	void test_create_checking_account() {
		Account checkingAccount = AccountMocks.getMockCheckingAccount();
		checkingAccount.setBalance(100.00);
		
		when(accountRepository.save(saveAccountCaptor.capture())).thenReturn(checkingAccount);
		
		Account accountActual = bankAccountsService.createUpdate(checkingAccount);
		
		assertAll(() -> assertEquals(saveAccountCaptor.getValue().getBalance(), accountActual.getBalance()),
				() -> assertEquals(saveAccountCaptor.getValue().getInterestCharge(), accountActual.getInterestCharge()),
				() -> assertEquals(saveAccountCaptor.getValue().getMinimumBalance(), accountActual.getMinimumBalance()),
				() -> assertEquals(saveAccountCaptor.getValue().getPenalty(), accountActual.getPenalty()),
				() -> assertEquals(saveAccountCaptor.getValue().getTransactionCharge(), accountActual.getTransactionCharge()));
	}
	
	@Test
	@DisplayName("Should Create Interest Account")
	void test_create_interest_account() {
		Account interestAccount = AccountMocks.getMockInterestAccount();
		interestAccount.setBalance(0.00);
		
		when(accountRepository.save(saveAccountCaptor.capture())).thenReturn(interestAccount);
		
		Account accountActual = bankAccountsService.createUpdate(interestAccount);
		
		assertAll(() -> assertEquals(saveAccountCaptor.getValue().getBalance(), accountActual.getBalance()),
				() -> assertEquals(saveAccountCaptor.getValue().getInterestCharge(), accountActual.getInterestCharge()),
				() -> assertEquals(saveAccountCaptor.getValue().getMinimumBalance(), accountActual.getMinimumBalance()),
				() -> assertEquals(saveAccountCaptor.getValue().getPenalty(), accountActual.getPenalty()),
				() -> assertEquals(saveAccountCaptor.getValue().getTransactionCharge(), accountActual.getTransactionCharge()));
	}
	
	@Test
	@DisplayName("Should Throw BadRequestException")
	void test_throw_BadRequestException() {
		when(accountRepository.save(any())).thenThrow(BadRequestException.class);
		assertThrows(BadRequestException.class, ()-> bankAccountsService.createUpdate(new RegularAccount()));
		
	}
}
