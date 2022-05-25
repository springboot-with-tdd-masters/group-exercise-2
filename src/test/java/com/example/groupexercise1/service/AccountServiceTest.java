package com.example.groupexercise1.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.groupexercise1.exeption.AccountNotFoundException;
import com.example.groupexercise1.exeption.InsufficientBalanceException;
import com.example.groupexercise1.exeption.InvalidTransactionAmountException;
import com.example.groupexercise1.exeption.InvalidTransactionTypeException;
import com.example.groupexercise1.model.Account;
import com.example.groupexercise1.model.CheckingAccount;
import com.example.groupexercise1.model.InterestAccount;
import com.example.groupexercise1.model.RegularAccount;
import com.example.groupexercise1.model.dto.AccountDto;
import com.example.groupexercise1.model.dto.AccountRequestDto;
import com.example.groupexercise1.repository.AccountRepository;

public class AccountServiceTest {

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private Account accountMock;

	@InjectMocks
	private AccountServiceImpl accountService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Should save regular account and return correct details")
	public void shouldSaveRegularAccountAndReturnDetails() {
		AccountRequestDto accountRequest = new AccountRequestDto();
		accountRequest.setName("Juan Dela Cruz");
		accountRequest.setType("regular");

		Account expectedResponse = new RegularAccount();
		expectedResponse.setName("Juan Dela Cruz");
		expectedResponse.setAcctNumber("123456");
		expectedResponse.setMinimumBalance(500d);
		expectedResponse.setBalance(expectedResponse.getMinimumBalance());
		expectedResponse.setPenalty(10d);
		expectedResponse.setTransactionCharge(0d);
		expectedResponse.setInterestCharge(0d);
		expectedResponse.setAcctNumber("12345678");

		when(accountRepository.save(any())).thenReturn(expectedResponse);

		AccountDto actualResponse = accountService.createAccount(accountRequest);

		verify(accountRepository).save(any());

		assertEquals(expectedResponse.getName(), actualResponse.getName());
		assertEquals(expectedResponse.getAcctNumber(), actualResponse.getAcctNumber());
		assertEquals(expectedResponse.getMinimumBalance(), actualResponse.getMinimumBalance());
		assertEquals(expectedResponse.getType(), actualResponse.getType());
	}

	@Test
	@DisplayName("Should save checking account and return correct details")
	public void shouldSaveCheckingAccountAndReturnDetails() {
		AccountRequestDto accountRequest = new AccountRequestDto();
		accountRequest.setName("Juan Dela Cruz");
		accountRequest.setType("checking");

		Account expectedResponse = new CheckingAccount();
		expectedResponse.setName("Juan Dela Cruz");
		expectedResponse.setAcctNumber("123456");
		expectedResponse.setMinimumBalance(100d);
		expectedResponse.setBalance(expectedResponse.getMinimumBalance());
		expectedResponse.setPenalty(10d);
		expectedResponse.setTransactionCharge(1d);
		expectedResponse.setInterestCharge(0d);


		when(accountRepository.save(any())).thenReturn(expectedResponse);

		AccountDto actualResponse = accountService.createAccount(accountRequest);

		verify(accountRepository).save(any());

		assertEquals(expectedResponse.getName(), actualResponse.getName());
		assertEquals(expectedResponse.getAcctNumber(), actualResponse.getAcctNumber());
		assertEquals(expectedResponse.getMinimumBalance(), actualResponse.getMinimumBalance());
		assertEquals(expectedResponse.getType(), actualResponse.getType());
	}
	
	@Test
	@DisplayName("Should save interest account and return correct details")
	public void shouldSaveInterestAccountAndReturnDetails() {
		AccountRequestDto accountRequest = new AccountRequestDto();
		accountRequest.setName("Juan Dela Cruz");
		accountRequest.setType("checking");

		Account expectedResponse = new InterestAccount();
		expectedResponse.setName("Juan Dela Cruz");
		expectedResponse.setAcctNumber("123456");
		expectedResponse.setMinimumBalance(0d);
		expectedResponse.setBalance(expectedResponse.getMinimumBalance());
		expectedResponse.setPenalty(0d);
		expectedResponse.setTransactionCharge(0d);
		expectedResponse.setInterestCharge(0.03d);
		expectedResponse.setCreatedDate(LocalDate.now());


		when(accountRepository.save(any())).thenReturn(expectedResponse);

		AccountDto actualResponse = accountService.createAccount(accountRequest);

		verify(accountRepository).save(any());

		assertEquals(expectedResponse.getName(), actualResponse.getName());
		assertEquals(expectedResponse.getAcctNumber(), actualResponse.getAcctNumber());
		assertEquals(expectedResponse.getMinimumBalance(), actualResponse.getMinimumBalance());
		assertEquals(expectedResponse.getType(), actualResponse.getType());
	}


	@Test
	@DisplayName("Should return all accounts with correct details")
	public void shouldReturnAllAccountsWithCorrectDetails() {
		RegularAccount regularAccount = new RegularAccount();
		regularAccount.setName("Juan Dela Cruz");
		regularAccount.setMinimumBalance(500d);

		CheckingAccount checkingAccount = new CheckingAccount();
		checkingAccount.setName("Juan Dela Cruz I");
		checkingAccount.setMinimumBalance(100d);

		InterestAccount interestAccount = new InterestAccount();
		interestAccount.setName("Juan Dela Cruz II");
		interestAccount.setMinimumBalance(0d);

		List<Account> accounts = Arrays.asList(regularAccount, checkingAccount, interestAccount);

		when(accountRepository.findAll()).thenReturn(accounts);

		List<AccountDto> accountDtos = accountService.getAllAccounts();

		// expected returned objects
		AccountDto regularAcctDto = new AccountDto();
		regularAcctDto.setType("regular");
		regularAcctDto.setName("Juan Dela Cruz");
		regularAcctDto.setMinimumBalance(500d);

		AccountDto checkingAcctDto = new AccountDto();
		checkingAcctDto.setType("checking");
		checkingAcctDto.setName("Juan Dela Cruz I");
		checkingAcctDto.setMinimumBalance(100d);

		AccountDto interestAcctDto = new AccountDto();
		interestAcctDto.setType("interest");
		interestAcctDto.setName("Juan Dela Cruz II");
		interestAcctDto.setMinimumBalance(0d);

		verify(accountRepository).findAll();

		assertThat(accountDtos).hasSize(3);
		assertThat(accountDtos).containsExactlyInAnyOrder(regularAcctDto, checkingAcctDto, interestAcctDto);
	}
	
	@Test
	@DisplayName("Should return one account with correct details")
	public void shouldReturnOneAccountsWithCorrectDetails() {

	    Account expectedResponse = new RegularAccount();
	    expectedResponse.setId(1L);
	    expectedResponse.setName("Juan Dela Cruz");
	    expectedResponse.setAcctNumber("123456");
	    expectedResponse.setMinimumBalance(500d);

	    when(accountRepository.findById(1L))
	        .thenReturn(Optional.of(expectedResponse));

	    AccountDto actualResponse = accountService.getAccount(1L);

	    verify(accountRepository).findById(1L);

	    assertThat(actualResponse)
	        .extracting("id", "name", "minimumBalance", "type", "acctNumber")
	        .containsExactly(expectedResponse.getId(), expectedResponse.getName(),
	            expectedResponse.getMinimumBalance(), expectedResponse.getType(),
	            expectedResponse.getAcctNumber());
	}

	@Test
	@DisplayName("Should delete account")
	public void shouldDeleteAccount_Test() {
		RegularAccount regularAccount = new RegularAccount();
		regularAccount.setName("Juan Dela Cruz");
		regularAccount.setMinimumBalance(500d);

		when(accountRepository.save(regularAccount)).thenReturn(regularAccount);

		accountRepository.delete(regularAccount);

		verify(accountRepository, times(1)).delete(regularAccount);
	}

	@Test
	@DisplayName("Delete account should return exception error")
	public void deleteAccount_ShouldReturnError_Test() {
		Optional<Account> regularAccount = Optional.empty();

		when(accountRepository.findById(0L)).thenReturn(regularAccount);
		assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(0L));
	}

  
	@Test
	@DisplayName("Should be able to deposit to regular account with balance correctly updated")
	public void shouldeBeAbleToDepositToRegularAccount() {
		Account account = new RegularAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(500d);
		account.setBalance(500d);
		account.setPenalty(10d);
		account.setTransactionCharge(0d);
		account.setInterestCharge(0d);
		  
		when(accountRepository.findById(1L))
			.thenReturn(Optional.of(account));
	
		AccountDto actualResponse = accountService.createTransaction("deposit", 1L, 100d);
		    
		//expected response
		AccountDto expectedResponse = new AccountDto();
		expectedResponse.setBalance(600d); //balance gets increased by 100
		
		verify(accountRepository).findById(1L);
			
		assertEquals(expectedResponse.getBalance(), actualResponse.getBalance());
	}
	
	@Test
	@DisplayName("Should be able to withdraw from regular account with no penalty")
	public void shouldeBeAbleToWithdrawFromRegularAccountWithNoPenalty() {
		Account account = new RegularAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(500d);
		account.setBalance(1000d);
		account.setPenalty(10d);
		account.setTransactionCharge(0d);
		account.setInterestCharge(0d);
		  
		when(accountRepository.findById(1L))
			.thenReturn(Optional.of(account));
	
		AccountDto actualResponse = accountService.createTransaction("withdraw", 1L, 500d);
		    
		//expected response
		AccountDto expectedResponse = new AccountDto();
		expectedResponse.setBalance(500d); //no penalty
		
		verify(accountRepository).findById(1L);
			
		assertEquals(expectedResponse.getBalance(), actualResponse.getBalance());
	}
	
	@Test
	@DisplayName("Should be able to withdraw from regular account with penalty")
	public void shouldeBeAbleToWithdrawFromRegularAccountWithPenalty() {
		Account account = new RegularAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(500d);
		account.setBalance(1000d);
		account.setPenalty(10d);
		account.setTransactionCharge(0d);
		account.setInterestCharge(0d);
		  
		when(accountRepository.findById(1L))
			.thenReturn(Optional.of(account));
	
		AccountDto actualResponse = accountService.createTransaction("withdraw", 1L, 600d);
		    
		//expected response
		AccountDto expectedResponse = new AccountDto();
		expectedResponse.setBalance(390d); //10.00 penalty
		
		verify(accountRepository).findById(1L);
			
		assertEquals(expectedResponse.getBalance(), actualResponse.getBalance());
	}
	
	@Test
	@DisplayName("Should throw InsufficientBalanceException when balance becomes negative")
	public void shouldThrowInsufficientBalanceExceptionWhenBalanceIsNegative() {
		Account account = new RegularAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(500d);
		account.setBalance(50d);
		account.setPenalty(10d);
	
		when(accountRepository.findById(1L))
		  	.thenReturn(Optional.of(account));
		  
		assertThrows(InsufficientBalanceException.class,
				  ()->accountService.createTransaction("withdraw", 1L, 100d));	  
	}
	  
	@Test
	@DisplayName("Should throw InvalidTransactionTypeException when transaction type is not supported")
	public void shouldThrowInvalidTransactionTypeExceptionForInvalidActionType() {
		Account account = new RegularAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(500d);
	
		when(accountRepository.findById(1L))
		  	.thenReturn(Optional.of(account));
		  
		assertThrows(InvalidTransactionTypeException.class,
				  ()->accountService.createTransaction("xxx", 1L, 100d));	  
	}
	
	@Test
	@DisplayName("Should throw InvalidTransactionAmountException when transaction amount is invalid")
	public void shouldThrowInvalidTransactionAmountExceptionForInvalidAmount() {
		Account account = new RegularAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(500d);
	
		when(accountRepository.findById(1L))
		  	.thenReturn(Optional.of(account));
		  
		assertThrows(InvalidTransactionAmountException.class,
				  ()->accountService.createTransaction("deposit", 1L, -100d));	  
	}

	@Test
	@DisplayName("Should be able to deposit to checking account and balance will be updated correctly")
	public void shouldeBeAbleToDepositToCheckingAccount() {
		Account account = new CheckingAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(100d);
		account.setBalance(100d);
		account.setPenalty(10d);
		account.setTransactionCharge(1d);
		account.setInterestCharge(0d);

		when(accountRepository.findById(1L))
				.thenReturn(Optional.of(account));

		AccountDto actualResponse = accountService.createTransaction("deposit", 1L, 100d);

		double expectedBalance = 199d; //balance gets +100 for deposit and -1 for transaction charge

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);
	}

	@Test
	@DisplayName("Should be able to withdraw to checking account and balance will be updated correctly with no penalty")
	public void shouldeBeAbleToWithdrawToCheckingAccountNoPenalty() {

		Account account = new CheckingAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(100d);
		account.setBalance(500d);
		account.setPenalty(10d);
		account.setTransactionCharge(1d);
		account.setInterestCharge(0d);

		when(accountRepository.findById(1L))
				.thenReturn(Optional.of(account));

		AccountDto actualResponse = accountService.createTransaction("withdraw", 1L, 100d);

		double expectedBalance = 399d; //balance gets -100 (withdraw) and -1 (transaction charge)

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);
	}

	@Test
	@DisplayName("Should be able to withdraw to checking account and balance will be updated correctly with penalty")
	public void shouldeBeAbleToWithdrawToCheckingAccountWithPenalty() {

		Account account = new CheckingAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(100d);
		account.setBalance(200d);
		account.setPenalty(10d);
		account.setTransactionCharge(1d);
		account.setInterestCharge(0d);

		when(accountRepository.findById(1L))
				.thenReturn(Optional.of(account));

		AccountDto actualResponse = accountService.createTransaction("withdraw", 1L, 100d);

		//transaction charge first
		double expectedBalance = 89d; //balance gets -100 (withdraw) and -1 (transaction charge) and -10(penalty)

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);

	}
	
	@Test
	@DisplayName("Should be able to deposit to interest account and balance will be updated correctly")
	public void shouldeBeAbleToDepositToInterestAccount() {
		Account account = new InterestAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(0d);
		account.setBalance(100d);
		account.setPenalty(0d);
		account.setTransactionCharge(0d);
		account.setInterestCharge(0.03d);
		account.setCreatedDate(LocalDate.now());

		when(accountRepository.findById(1L))
				.thenReturn(Optional.of(account));

		AccountDto actualResponse = accountService.createTransaction("deposit", 1L, 100d);

		double expectedBalance = 200.0d; //balance gets +100 for deposit and -1 for transaction charge

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);
	}
	
	@Test
	@DisplayName("Should be able to deposit to interest account with monthly gain interest and balance will be updated correctly")
	public void shouldeBeAbleToDepositToInterestAccountWithMonthlyGainInterest() {
		Account account = new InterestAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(0d);
		account.setBalance(100d);
		account.setPenalty(0d);
		account.setTransactionCharge(0d);
		account.setInterestCharge(0.03d);
		account.setCreatedDate(LocalDate.of(2022, 4, 18));

		when(accountRepository.findById(1L))
				.thenReturn(Optional.of(account));

		AccountDto actualResponse = accountService.createTransaction("deposit", 1L, 100d);

		double expectedBalance = 203.0d; //balance gets +100 for deposit and -1 for transaction charge

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);
	}
	
	@Test
	@DisplayName("Should be able to withdraw to interest account and balance will be updated correctly")
	public void shouldeBeAbleToWithdrawToInterestAccount() {
		Account account = new InterestAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(0d);
		account.setBalance(100d);
		account.setPenalty(0d);
		account.setTransactionCharge(0d);
		account.setInterestCharge(0.03d);
		account.setCreatedDate(LocalDate.now());

		when(accountRepository.findById(1L))
				.thenReturn(Optional.of(account));

		AccountDto actualResponse = accountService.createTransaction("withdraw", 1L, 100d);

		double expectedBalance = 0.0d; //balance gets +100 for deposit and -1 for transaction charge

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);
	}
	
	@Test
	@DisplayName("Should be able to withdraw to interest account with monthly gain interest and balance will be updated correctly")
	public void shouldeBeAbleToWithdrawToInterestAccountWithMonthlyGainInterest() {
		Account account = new InterestAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(0d);
		account.setBalance(100d);
		account.setPenalty(0d);
		account.setTransactionCharge(0d);
		account.setInterestCharge(0.03d);
		account.setCreatedDate(LocalDate.of(2022, 4, 18));

		when(accountRepository.findById(1L))
				.thenReturn(Optional.of(account));

		AccountDto actualResponse = accountService.createTransaction("withdraw", 1L, 100d);

		double expectedBalance = 3.0d; //balance gets +100 for deposit and -1 for transaction charge

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);
	}
}
