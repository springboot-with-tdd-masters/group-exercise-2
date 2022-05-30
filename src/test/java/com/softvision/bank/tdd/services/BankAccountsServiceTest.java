package com.softvision.bank.tdd.services;

import static com.softvision.bank.tdd.AccountMocks.getMockCheckingAccount;
import static com.softvision.bank.tdd.AccountMocks.getMockRegularAccount;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.softvision.bank.tdd.model.CheckingAccount;
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
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
	@DisplayName("Should get accounts with pageable sorted by Name")
	void test_get_account_sorted_by_name() {
		List<Account> accounts = new ArrayList<>();
		accounts.add(getMockRegularAccount());
		CheckingAccount mockCheckingAccount = getMockCheckingAccount();
		mockCheckingAccount.setName("Anderson Brooke");
		accounts.add(mockCheckingAccount);

		Pageable pageRequest = PageRequest.of(0, 3, Sort.by("name").ascending());
		List<Account> sortedAccounts = accounts.stream().sorted(Comparator.comparing(Account::getName)).collect(Collectors.toList());
		Page<Account> accountPage = new PageImpl<>(sortedAccounts);
		when(bankAccountsService.readAccounts(pageRequest)).thenReturn(accountPage);

		Page<Account> retrievedAccountPage = bankAccountsService.readAccounts(pageRequest);

		assertAll(() -> assertEquals("Anderson Brooke", retrievedAccountPage.getContent().get(0).getName()),
				() -> assertEquals(getMockRegularAccount().getName(), retrievedAccountPage.getContent().get(1).getName()));
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
