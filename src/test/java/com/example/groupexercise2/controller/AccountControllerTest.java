package com.example.groupexercise2.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.groupexercise2.exeption.AccountNotFoundException;
import com.example.groupexercise2.exeption.InvalidAccountTypeException;
import com.example.groupexercise2.exeption.InvalidTransactionAmountException;
import com.example.groupexercise2.exeption.InvalidTransactionTypeException;
import com.example.groupexercise2.model.dto.AccountDto;
import com.example.groupexercise2.model.dto.AccountRequestDto;
import com.example.groupexercise2.model.dto.TransactionRequestDto;
import com.example.groupexercise2.service.AccountService;
import com.example.groupexercise2.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest({AccountController.class, TransactionController.class})
@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerTest {
	 
	 @Autowired
	 private MockMvc mockMvc;
	 
	 @MockBean
	 private AccountService accountService;
	 
	 @MockBean
	 private TransactionService transactionService;
	 
	 @MockBean
	 private UserDetailsService userService;
		
	 private ObjectMapper objectMapper = new ObjectMapper();
	 
	 @Test
	 @DisplayName("Should create a regular account and returns correct details and http status 200")
	 public void shouldCreateRegularAccount() throws Exception {
		 AccountDto expectedResponse = new AccountDto();
		 expectedResponse.setType("regular");
		 expectedResponse.setName("Juan Dela Cruz");
		 expectedResponse.setAcctNumber("123456");
		 expectedResponse.setMinimumBalance(500d);
			
		 AccountRequestDto accountRequest = new AccountRequestDto();
		 accountRequest.setName("Juan Dela Cruz");
		 accountRequest.setType("regular");

		 when(accountService.createAccount(accountRequest))
	         	.thenReturn(expectedResponse);
			
		 this.mockMvc.perform(post("/accounts").content(
		            	objectMapper.writeValueAsString(accountRequest)
		    		).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.type").value("regular"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Juan Dela Cruz"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.acctNumber").value("123456"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.minimumBalance").value("500.0"));

		  verify(accountService).createAccount(accountRequest);
	}
	  
	@Test
	@DisplayName("Should return Bad Request error when account type is not supported")
	public void shouldReturnBadRequestForInvalidAccountType() throws Exception {
		 AccountRequestDto accountRequest = new AccountRequestDto();
		 accountRequest.setName("Juan Dela Cruz");
		 accountRequest.setType("xxx");

		 when(accountService.createAccount(accountRequest))
	         	.thenThrow(new InvalidAccountTypeException());
			
		 this.mockMvc.perform(post("/accounts").content(
		            	objectMapper.writeValueAsString(accountRequest)
		    		).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		 verify(accountService).createAccount(accountRequest);  
	}
	
	@Test
	@DisplayName("Get all acounts with paging and sorting")
	public void getAllAccountsWithPagingAndSorting() throws Exception {
		 //expected returned objects
		 AccountDto regularAccount = new AccountDto();
		 regularAccount.setType("regular");
		 regularAccount.setName("Juan Dela Cruz");
		 regularAccount.setMinimumBalance(500d);
		     
		 AccountDto checkingAccount = new AccountDto();
		 checkingAccount.setType("checking");
		 checkingAccount.setName("Juan Dela Cruz I");
		 checkingAccount.setMinimumBalance(100d);
		
		 AccountDto interestAccount = new AccountDto();
		 interestAccount.setType("interest");
		 interestAccount.setName("Juan Dela Cruz II");
		 interestAccount.setMinimumBalance(0d);
		
		 Page<AccountDto> pagedAccounts = new PageImpl(Arrays.asList(interestAccount, checkingAccount, regularAccount));

		 Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
		 when(accountService.getAllAccounts(pageable))
		 	.thenReturn(pagedAccounts);

		 this.mockMvc.perform(get("/accounts?page=0&size=20&sort=createdAt,desc"))
		 	.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].type").value("interest"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].name").value("Juan Dela Cruz II"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].minimumBalance").value("0.0"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].type").value("checking"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].name").value("Juan Dela Cruz I"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].minimumBalance").value("100.0"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].type").value("regular"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].name").value("Juan Dela Cruz"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content.[2].minimumBalance").value("500.0"));
				
		 verify(accountService).getAllAccounts(pageable);
	}


	@Test
	@DisplayName("Should return one account with correct details and http status 200")
	public void shouldReturnOneAccountsWithCorrectDetails() throws Exception {

		AccountDto regularAcctDto = new AccountDto();
		regularAcctDto.setId(1L);
		regularAcctDto.setType("regular");
		regularAcctDto.setName("Juan Dela Cruz");
		regularAcctDto.setMinimumBalance(500d);

		when(accountService.getAccount(1L))
				.thenReturn(regularAcctDto);

		this.mockMvc.perform(get("/accounts/1"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.type").value("regular"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Juan Dela Cruz"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.minimumBalance").value("500.0"));

		verify(accountService).getAccount(1L);
	}

	@Test
	@DisplayName("Should return http 404 for non-existing account")
	public void shouldReturnHttp404forNonExistingAccount() throws Exception {

		when(accountService.getAccount(1L))
				.thenThrow(new AccountNotFoundException("Account not found"));

		this.mockMvc.perform(get("/accounts/1"))
				.andExpect(status().isNotFound());

		verify(accountService).getAccount(1L);
	}
		
	@Test
	@DisplayName("Should be able to deposit to regular account")
	public void shouldeBeAbleToDepositToRegularAccount() throws Exception {

		AccountDto regularAcctDto = new AccountDto();
		regularAcctDto.setId(1L);
		regularAcctDto.setType("regular");
		regularAcctDto.setName("Juan Dela Cruz");
		regularAcctDto.setAcctNumber("123456");
		regularAcctDto.setMinimumBalance(500d);
		regularAcctDto.setBalance(600d);

		TransactionRequestDto transactRequest = new TransactionRequestDto();
		transactRequest.setType("deposit");
		transactRequest.setAmount(100d);

		when(transactionService.createTransaction(1L, transactRequest))
				.thenReturn(regularAcctDto);

		this.mockMvc.perform(post("/accounts/1/transactions").content(
	            	objectMapper.writeValueAsString(transactRequest)
	    		).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.type").value("regular"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Juan Dela Cruz"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.acctNumber").value("123456"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.minimumBalance").value("500.0"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("600.0"))
		;

		verify(transactionService).createTransaction(1L, transactRequest);
	}
	
	@Test
	@DisplayName("Should be able to withdraw from regular account")
	public void shouldeBeAbleToWithdrawFromRegularAccount() throws Exception {

		AccountDto regularAcctDto = new AccountDto();
		regularAcctDto.setId(1L);
		regularAcctDto.setType("regular");
		regularAcctDto.setName("Juan Dela Cruz");
		regularAcctDto.setAcctNumber("123456");
		regularAcctDto.setMinimumBalance(500d);
		regularAcctDto.setBalance(500d);

		TransactionRequestDto transactRequest = new TransactionRequestDto();
		transactRequest.setType("withdraw");
		transactRequest.setAmount(500d);
		
		when(transactionService.createTransaction(1L, transactRequest))
				.thenReturn(regularAcctDto);
			
		this.mockMvc.perform(post("/accounts/1/transactions").content(
	            	objectMapper.writeValueAsString(transactRequest)
	    		).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.type").value("regular"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Juan Dela Cruz"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.acctNumber").value("123456"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.minimumBalance").value("500.0"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("500.0"));

		verify(transactionService).createTransaction(1L, transactRequest);
	}

	@Test
	@DisplayName("Should return Bad Request error for invalid transaction amount")
	public void shouldReturnBadRequestForInvalidTransactionAmount() throws Exception {

		TransactionRequestDto transactRequest = new TransactionRequestDto();
		transactRequest.setType("deposit");
		transactRequest.setAmount(-100d);

		when(transactionService.createTransaction(1L, transactRequest))
				.thenThrow(new InvalidTransactionAmountException());

		this.mockMvc.perform(post("/accounts/1/transactions").content(
		            	objectMapper.writeValueAsString(transactRequest)
		    		).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

		verify(transactionService).createTransaction(1L, transactRequest);
	}
	
	@Test
	@DisplayName("Should return Bad Request error for invalid transaction type")
	public void shouldReturnBadRequestForInvalidTransactionType() throws Exception {

		TransactionRequestDto transactRequest = new TransactionRequestDto();
		transactRequest.setType("xxx");
		transactRequest.setAmount(100d);

		when(transactionService.createTransaction(1L, transactRequest))
				.thenThrow(new InvalidTransactionTypeException());


		this.mockMvc.perform(post("/accounts/1/transactions").content(
		            	objectMapper.writeValueAsString(transactRequest)
		    		).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

		verify(transactionService).createTransaction(1L, transactRequest);
	}



	@Test
	@DisplayName("Should update account name and return updated value and http status 200")
	public void updateAccountName() throws Exception {
		AccountDto expectedResponse = new AccountDto();
		expectedResponse.setType("regular");
		expectedResponse.setName("Juan Dela Cruz");
		expectedResponse.setAcctNumber("123456");
		expectedResponse.setMinimumBalance(500d);

		AccountRequestDto accountRequest = new AccountRequestDto();
		accountRequest.setId(1L);
		accountRequest.setName("Juan Dela Cruz");
		accountRequest.setType("regular");

		when(accountService.updateAccount(accountRequest))
				.thenReturn(expectedResponse);

		this.mockMvc.perform(post("/accounts").content(
						objectMapper.writeValueAsString(accountRequest)
				).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.type").value("regular"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Juan Dela Cruz"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.acctNumber").value("123456"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.minimumBalance").value("500.0"));

		verify(accountService).updateAccount(accountRequest);
	}
}
