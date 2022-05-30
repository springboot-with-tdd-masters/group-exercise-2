package com.example.groupexercise2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.groupexercise2.exeption.AccountNotFoundException;
import com.example.groupexercise2.exeption.InsufficientBalanceException;
import com.example.groupexercise2.exeption.InvalidTransactionAmountException;
import com.example.groupexercise2.exeption.InvalidTransactionTypeException;
import com.example.groupexercise2.model.Account;
import com.example.groupexercise2.model.CheckingAccount;
import com.example.groupexercise2.model.InterestAccount;
import com.example.groupexercise2.model.RegularAccount;
import com.example.groupexercise2.model.Transaction;
import com.example.groupexercise2.model.dto.AccountDto;
import com.example.groupexercise2.model.dto.TransactionDto;
import com.example.groupexercise2.model.dto.TransactionRequestDto;
import com.example.groupexercise2.repository.AccountRepository;
import com.example.groupexercise2.repository.TransactionRepository;
import com.example.groupexercise2.service.TransactionService;
import com.example.groupexercise2.service.TransactionServiceImpl;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;
	
	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private TransactionService transactionService = new TransactionServiceImpl();

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Should return one transaction with correct details")
	public void shouldReturnOneTransactionsWithCorrectDetails() {

		RegularAccount mockedAccount = mock(RegularAccount.class);

		Transaction expectedResponseTransaction = new Transaction();
		expectedResponseTransaction.setTransactionType("withdraw");
		expectedResponseTransaction.setAmount(100d);
		expectedResponseTransaction.setAccount(mockedAccount);

		when(transactionRepository.findById(1L)).thenReturn(Optional.of(expectedResponseTransaction));

		TransactionDto actualResponse = transactionService.getTransactionById(1L);

		verify(transactionRepository).findById(1L);

		assertThat(actualResponse).extracting("id", "transactionType", "amount").containsExactly(actualResponse.getId(),
				actualResponse.getTransactionType(), actualResponse.getAmount());

	}
	@Test
	@DisplayName("Should be able to deposit to regular account with balance correctly updated")
	public void shouldeBeAbleToDepositToRegularAccount() {
		
		TransactionRequestDto request = new TransactionRequestDto();
		request.setType("deposit");
		request.setAmount(100d);
		
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
	
		AccountDto actualResponse = transactionService.createTransaction(1L, request);
		    
		//expected response
		AccountDto expectedResponse = new AccountDto();
		expectedResponse.setBalance(600d); //balance gets increased by 100
		
		verify(accountRepository).findById(1L);
			
		assertEquals(expectedResponse.getBalance(), actualResponse.getBalance());
	}
	
	@Test
	@DisplayName("Should be able to withdraw from regular account with no penalty")
	public void shouldeBeAbleToWithdrawFromRegularAccountWithNoPenalty() {
		
		TransactionRequestDto request = new TransactionRequestDto();
		request.setType("withdraw");
		request.setAmount(100d);
		
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

		AccountDto actualResponse = transactionService.createTransaction(1L, request);
		    
		//expected response
		AccountDto expectedResponse = new AccountDto();
		expectedResponse.setBalance(900d); //no penalty
		
		verify(accountRepository).findById(1L);
			
		assertEquals(expectedResponse.getBalance(), actualResponse.getBalance());
	}
	
	@Test
	@DisplayName("Should be able to withdraw from regular account with penalty")
	public void shouldeBeAbleToWithdrawFromRegularAccountWithPenalty() {
		
		TransactionRequestDto request = new TransactionRequestDto();
		request.setType("withdraw");
		request.setAmount(100d);
		
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

		AccountDto actualResponse = transactionService.createTransaction(1L, request);
		    
		//expected response
		AccountDto expectedResponse = new AccountDto();
		expectedResponse.setBalance(390d); //10.00 penalty
		
		verify(accountRepository).findById(1L);
			
		assertEquals(expectedResponse.getBalance(), actualResponse.getBalance());
	}
	
	@Test
	@DisplayName("Should throw InsufficientBalanceException when balance becomes negative")
	public void shouldThrowInsufficientBalanceExceptionWhenBalanceIsNegative() {
		
		TransactionRequestDto request = new TransactionRequestDto();
		request.setType("withdraw");
		request.setAmount(100d);
		
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
				  ()->transactionService.createTransaction(1L, request));	  
	}
	  
	@Test
	@DisplayName("Should throw InvalidTransactionTypeException when transaction type is not supported")
	public void shouldThrowInvalidTransactionTypeExceptionForInvalidActionType() {
		TransactionRequestDto request = new TransactionRequestDto();
		request.setType("withdrawww");
		request.setAmount(100d);
		
		Account account = new RegularAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(500d);
		account.setBalance(500d);
		account.setPenalty(10d);
	
		when(accountRepository.findById(1L))
		  	.thenReturn(Optional.of(account));
		  
		assertThrows(InvalidTransactionTypeException.class,
				  ()->transactionService.createTransaction(1L, request));	  
	}
	
	@Test
	@DisplayName("Should throw InvalidTransactionAmountException when transaction amount is invalid")
	public void shouldThrowInvalidTransactionAmountExceptionForInvalidAmount() {
		TransactionRequestDto request = new TransactionRequestDto();
		request.setType("withdraw");
		request.setAmount(-100d);
		
		Account account = new RegularAccount();
		account.setId(1L);
		account.setName("Juan Dela Cruz");
		account.setAcctNumber("123456");
		account.setMinimumBalance(500d);
		account.setBalance(500d);
		account.setPenalty(10d);
	
		when(accountRepository.findById(1L))
		  	.thenReturn(Optional.of(account));
		  
		assertThrows(InvalidTransactionAmountException.class,
				  ()->transactionService.createTransaction(1L, request));	  
	}

	@Test
	@DisplayName("Should be able to deposit to checking account and balance will be updated correctly")
	public void shouldeBeAbleToDepositToCheckingAccount() {
		
		TransactionRequestDto request = new TransactionRequestDto();
		request.setType("deposit");
		request.setAmount(100d);
		
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

		AccountDto actualResponse = transactionService.createTransaction(1L, request);

		double expectedBalance = 199d; //balance gets +100 for deposit and -1 for transaction charge

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);
	}

	@Test
	@DisplayName("Should be able to withdraw to checking account and balance will be updated correctly with no penalty")
	public void shouldeBeAbleToWithdrawToCheckingAccountNoPenalty() {

		TransactionRequestDto request = new TransactionRequestDto();
		request.setType("withdraw");
		request.setAmount(100d);
		
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

		AccountDto actualResponse = transactionService.createTransaction(1L, request);

		double expectedBalance = 399d; //balance gets -100 (withdraw) and -1 (transaction charge)

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);
	}

	@Test
	@DisplayName("Should be able to withdraw to checking account and balance will be updated correctly with penalty")
	public void shouldeBeAbleToWithdrawToCheckingAccountWithPenalty() {

		TransactionRequestDto request = new TransactionRequestDto();
		request.setType("withdraw");
		request.setAmount(100d);
		
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

		AccountDto actualResponse = transactionService.createTransaction(1L, request);

		//transaction charge first
		double expectedBalance = 89d; //balance gets -100 (withdraw) and -1 (transaction charge) and -10(penalty)

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);

	}
	
	@Test
	@DisplayName("Should be able to deposit to interest account and balance will be updated correctly")
	public void shouldeBeAbleToDepositToInterestAccount() {
		TransactionRequestDto request = new TransactionRequestDto();
		request.setType("deposit");
		request.setAmount(100d);
		
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

		AccountDto actualResponse = transactionService.createTransaction(1L, request);

		double expectedBalance = 200.0d; //balance gets +100 for deposit and -1 for transaction charge

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);
	}
	
	@Test
	@DisplayName("Should be able to deposit to interest account with monthly gain interest and balance will be updated correctly")
	public void shouldeBeAbleToDepositToInterestAccountWithMonthlyGainInterest() {

		TransactionRequestDto request = new TransactionRequestDto();
		request.setType("deposit");
		request.setAmount(100d);
		
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

		AccountDto actualResponse = transactionService.createTransaction(1L, request);

		double expectedBalance = 203.0d; //balance gets +100 for deposit and -1 for transaction charge

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);
	}
	
	@Test
	@DisplayName("Should be able to withdraw to interest account and balance will be updated correctly")
	public void shouldeBeAbleToWithdrawToInterestAccount() {

		TransactionRequestDto request = new TransactionRequestDto();
		request.setType("withdraw");
		request.setAmount(100d);
		
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

		AccountDto actualResponse = transactionService.createTransaction(1L, request);

		double expectedBalance = 0.0d; //balance gets +100 for deposit and -1 for transaction charge

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);
	}
	
	@Test
	@DisplayName("Should be able to withdraw to interest account with monthly gain interest and balance will be updated correctly")
	public void shouldeBeAbleToWithdrawToInterestAccountWithMonthlyGainInterest() {

		TransactionRequestDto request = new TransactionRequestDto();
		request.setType("withdraw");
		request.setAmount(100d);
		
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

		AccountDto actualResponse = transactionService.createTransaction(1L, request);

		double expectedBalance = 3.0d; //balance gets +100 for deposit and -1 for transaction charge

		verify(accountRepository).findById(1L);

		assertEquals(actualResponse.getBalance(),expectedBalance);
	}

	@Test
	@DisplayName("Should delete transaction")
	public void shouldDeleteTransaction_Test() {
		RegularAccount mockedAccount = mock(RegularAccount.class);

		Transaction expectedResponseTransaction = new Transaction();
		expectedResponseTransaction.setTransactionType("withdraw");
		expectedResponseTransaction.setAmount(500d);
		expectedResponseTransaction.setAccount(mockedAccount);

		when(transactionRepository.save(any())).thenReturn(expectedResponseTransaction);

		transactionRepository.delete(expectedResponseTransaction);

		verify(transactionRepository, times(1)).delete(expectedResponseTransaction);
	}

	@Test
	@DisplayName("Delete account should return exception error")
	public void deleteAccount_ShouldReturnError_Test() {
		Optional<Transaction> regulatransactiotransactionnrAccount = Optional.empty();
		
		when(transactionRepository.findById(1L)).thenReturn(regulatransactiotransactionnrAccount);
		assertThrows(AccountNotFoundException.class, () -> transactionService.delete(1L, 1L));
	}

}
