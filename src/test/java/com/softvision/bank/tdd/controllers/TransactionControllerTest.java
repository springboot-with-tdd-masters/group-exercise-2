package com.softvision.bank.tdd.controllers;

import static com.softvision.bank.tdd.AccountMocks.*;
import static com.softvision.bank.tdd.UserMocks.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softvision.bank.tdd.SecurityTestConfig;
import com.softvision.bank.tdd.model.Account;
import com.softvision.bank.tdd.model.CheckingAccount;
import com.softvision.bank.tdd.model.RegularAccount;
import com.softvision.bank.tdd.services.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;

import com.softvision.bank.tdd.AccountMocks;
import com.softvision.bank.tdd.exceptions.BadRequestException;
import com.softvision.bank.tdd.model.Transaction;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AutoConfigureMockMvc
@SpringBootTest(classes = SecurityTestConfig.class)
class TransactionControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	TransactionService transactionService;

	static final ObjectMapper objectMapper = new ObjectMapper();

	private static final Long MOCK_ID = 1L;
	private static final String MOCK_TRANSACTION_TYPE = "mockTransactionType";
	private static final Double MOCK_AMOUNT = 150.0;
	@Nested
	@DisplayName("Should be relative which the service return")
	@WithUserDetails(MOCK_USER2_USERNAME)
	class Deposit {

		@Test
		@DisplayName("Regular Account")
		void test_regular_account() throws Exception {
			when(transactionService.transact(eq(MOCK_ID), argThat(transaction ->
					MOCK_TRANSACTION_TYPE.equals(transaction.getType()) && transaction.getAmount() == MOCK_AMOUNT)))
					.thenReturn(AccountMocks.getMockRegularAccount());

			mockMvc.perform(post("/accounts/" + MOCK_ID + "/transactions")
						.content(objectMapper.writeValueAsString(new Transaction(MOCK_TRANSACTION_TYPE, MOCK_AMOUNT)))
						.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("type").value("regular"))
					.andExpect(jsonPath("balance").value(AccountMocks.REG_MOCK_BALANCE));

			verify(transactionService, atMostOnce()).transact(anyLong(), any());
		}

		@Test
		@DisplayName("Checking Account")
		void test_checking_account() throws Exception {
			when(transactionService.transact(eq(MOCK_ID), argThat(transaction ->
					MOCK_TRANSACTION_TYPE.equals(transaction.getType()) && transaction.getAmount() == MOCK_AMOUNT)))
					.thenReturn(AccountMocks.getMockCheckingAccount());

			mockMvc.perform(post("/accounts/" + MOCK_ID + "/transactions")
						.content(objectMapper.writeValueAsString(new Transaction(MOCK_TRANSACTION_TYPE, MOCK_AMOUNT)))
						.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("type").value("checking"))
					.andExpect(jsonPath("balance").value(AccountMocks.CHK_MOCK_BALANCE));

			verify(transactionService, atMostOnce()).transact(anyLong(), any());
		}

		@Test
		@DisplayName("Interest Account")
		void test_interest_account() throws Exception {
			when(transactionService.transact(eq(MOCK_ID), argThat(transaction ->
					MOCK_TRANSACTION_TYPE.equals(transaction.getType()) && transaction.getAmount() == MOCK_AMOUNT)))
					.thenReturn(AccountMocks.getMockInterestAccount());

			mockMvc.perform(post("/accounts/" + MOCK_ID + "/transactions")
						.content(objectMapper.writeValueAsString(new Transaction(MOCK_TRANSACTION_TYPE, MOCK_AMOUNT)))
						.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("type").value("interest"))
					.andExpect(jsonPath("balance").value(AccountMocks.INT_MOCK_BALANCE));

			verify(transactionService, atMostOnce()).transact(anyLong(), any());
		}

		@Test
		@DisplayName("Should get transactions with pageable sorted by Amount")
		void test_get_transactions_pageable_sort_by_amount() throws Exception {
			List<Transaction> transactions = new ArrayList<>();
			transactions.add(buildTransaction(AccountMocks.getMockRegularAccount(), 100, "DEPOSIT"));
			transactions.add(buildTransaction(AccountMocks.getMockRegularAccount(), 200, "WITHDRAW"));

			Pageable pageRequest = PageRequest.of(0, 3, Sort.by("amount").descending());
			List<Transaction> sortedAccounts = transactions.stream().sorted(Comparator.comparing(Transaction::getAmount).reversed()).collect(Collectors.toList());
			Page<Transaction> accountPage = new PageImpl<>(sortedAccounts);
			when(transactionService.readTransactions(pageRequest)).thenReturn(accountPage);

			mockMvc.perform(get("/accounts/1/transactions/readByPage")
							.contentType(MediaType.APPLICATION_JSON)
							.param("page", "0")
							.param("size", "3")
							.param("sort", "amount,desc")
					)
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.content.[0].amount").value(200))
					.andExpect(jsonPath("$.content.[1].amount").value(100));

			verify(transactionService, atMostOnce()).readTransactions(pageRequest);
		}


		@Test
		@DisplayName("Should throw BadRequestException")
		void test_throw_BadRequestException() throws Exception {
			when(transactionService.transact(anyLong(), any())).thenThrow(BadRequestException.class);
			mockMvc.perform(post("/accounts/1/transactions").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());

			verify(transactionService, atMostOnce()).transact(anyLong(), any());
		}
	}

	private Transaction buildTransaction(RegularAccount mockRegularAccount, double amount, String type) {
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setType(type);
		transaction.setAccount(mockRegularAccount);
		return transaction;
	}

	@Test
	@DisplayName("Given an anonymous user, response should give http status 403 (forbidden).")
	@WithAnonymousUser()
	void test_getAll_fail_unauthorized() throws Exception {
		mockMvc.perform(get("/accounts")).andExpect(status().isForbidden());
	}
	
}
