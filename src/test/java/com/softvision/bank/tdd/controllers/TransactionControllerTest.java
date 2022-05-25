package com.softvision.bank.tdd.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softvision.bank.tdd.services.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.softvision.bank.tdd.AccountMocks;
import com.softvision.bank.tdd.exceptions.BadRequestException;
import com.softvision.bank.tdd.model.Transaction;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WebMvcTest(controllers = TransactionController.class)
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
