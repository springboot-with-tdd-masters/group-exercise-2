package com.example.groupexercise2.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.groupexercise2.model.dto.AccountDto;
import com.example.groupexercise2.model.dto.TransactionDto;
import com.example.groupexercise2.service.AccountService;
import com.example.groupexercise2.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TransactionControllerTest {
	
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
	@DisplayName("Get all transactions with paging and sorting")
	public void getAllTransactionsWithPagingAndSorting() throws Exception {
		 AccountDto regAccount = new AccountDto();
		 regAccount.setAcctNumber("012345678");
			
		 TransactionDto  withdraw = new TransactionDto();
		 withdraw.setTransactionType("withdraw");
		 withdraw.setAmount(100d);
		 withdraw.setAccount(regAccount);
			 
		 TransactionDto deposit = new TransactionDto();
		 deposit.setTransactionType("deposit");
		 deposit.setAmount(1000d);
		 deposit.setAccount(regAccount);

		 Page<TransactionDto> transactions = new PageImpl(Arrays.asList(deposit, withdraw));
		 
		 Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
		 when(transactionService.getAllTransactions(1l, pageable))
		 	.thenReturn(transactions);

		 this.mockMvc.perform(get("/accounts/1/transactions?page=0&size=20&sort=createdAt,desc"))
			 .andExpect(status().isOk())
			 .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].transactionType").value("deposit"))
			 .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].amount").value("1000.0"))
			 .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].transactionType").value("withdraw"))
			 .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].amount").value("100.0"));
	 }	
}
