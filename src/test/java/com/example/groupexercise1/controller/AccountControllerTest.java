package com.example.groupexercise1.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.groupexercise1.exeption.AccountNotFoundException;
import com.example.groupexercise1.exeption.InvalidAccountTypeException;
import com.example.groupexercise1.exeption.InvalidTransactionAmountException;
import com.example.groupexercise1.exeption.InvalidTransactionTypeException;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.groupexercise1.model.dto.AccountDto;
import com.example.groupexercise1.model.dto.AccountRequestDto;
import com.example.groupexercise1.model.dto.TransactionRequestDto;
import com.example.groupexercise1.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
@AutoConfigureMockMvc
public class AccountControllerTest {
	 
	 @Autowired
	 private MockMvc mockMvc;
	 
	 @MockBean
	 private AccountService accountService;
	 
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
	 @DisplayName("Should return all accounts with correct details and http status 200")
	 public void shouldReturnAllAccountsWithCorrectDetails() throws Exception {
		 //expected returned objects
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
		
		 List<AccountDto> accounts = Arrays.asList(regularAcctDto, checkingAcctDto, interestAcctDto);
		 
		 when(accountService.getAllAccounts())
	         	.thenReturn(accounts);
			
		 this.mockMvc.perform(get("/accounts"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("regular"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Juan Dela Cruz"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].minimumBalance").value("500.0"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].type").value("checking"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Juan Dela Cruz I"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].minimumBalance").value("100.0"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].type").value("interest"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Juan Dela Cruz II"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].minimumBalance").value("0.0"));
				
		 verify(accountService).getAllAccounts();
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

		when(accountService.createTransaction("deposit", 1L, 100d))
				.thenReturn(regularAcctDto);

		TransactionRequestDto transactRequest = new TransactionRequestDto();
		transactRequest.setType("deposit");
		transactRequest.setAmount(100d);
		
		this.mockMvc.perform(post("/accounts/1/transactions").content(
	            	objectMapper.writeValueAsString(transactRequest)
	    		).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.type").value("regular"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Juan Dela Cruz"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.acctNumber").value("123456"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.minimumBalance").value("500.0"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("600.0"));

		verify(accountService).createTransaction("deposit", 1L, 100d);
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

		when(accountService.createTransaction("withdraw", 1L, 500d))
				.thenReturn(regularAcctDto);

		TransactionRequestDto transactRequest = new TransactionRequestDto();
		transactRequest.setType("withdraw");
		transactRequest.setAmount(500d);
		
		this.mockMvc.perform(post("/accounts/1/transactions").content(
	            	objectMapper.writeValueAsString(transactRequest)
	    		).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.type").value("regular"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Juan Dela Cruz"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.acctNumber").value("123456"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.minimumBalance").value("500.0"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("500.0"));

		verify(accountService).createTransaction("withdraw", 1L, 500d);
	}
	
	@Test
	@DisplayName("Should return Bad Request error for invalid transaction amount")
	public void shouldReturnBadRequestForInvalidTransactionAmount() throws Exception {
		when(accountService.createTransaction("deposit", 1L, -100d))
				.thenThrow(new InvalidTransactionAmountException());
			
		TransactionRequestDto transactRequest = new TransactionRequestDto();
		transactRequest.setType("deposit");
		transactRequest.setAmount(-100d);

		this.mockMvc.perform(post("/accounts/1/transactions").content(
		            	objectMapper.writeValueAsString(transactRequest)
		    		).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

		verify(accountService).createTransaction("deposit", 1L, -100d);
	}
	
	@Test
	@DisplayName("Should return Bad Request error for invalid transaction type")
	public void shouldReturnBadRequestForInvalidTransactionType() throws Exception {
		when(accountService.createTransaction("xxx", 1L, 100d))
				.thenThrow(new InvalidTransactionTypeException());
			
		TransactionRequestDto transactRequest = new TransactionRequestDto();
		transactRequest.setType("xxx");
		transactRequest.setAmount(100d);

		this.mockMvc.perform(post("/accounts/1/transactions").content(
		            	objectMapper.writeValueAsString(transactRequest)
		    		).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

		verify(accountService).createTransaction("xxx", 1L, 100d);
	}
}
