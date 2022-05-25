package com.softvision.bank.tdd.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softvision.bank.tdd.ApplicationConstants;
import com.softvision.bank.tdd.model.CheckingAccount;
import com.softvision.bank.tdd.model.InterestAccount;
import com.softvision.bank.tdd.model.RegularAccount;
import com.softvision.bank.tdd.services.BankAccountsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.softvision.bank.tdd.exceptions.BadRequestException;
import com.softvision.bank.tdd.exceptions.RecordNotFoundException;
import com.softvision.bank.tdd.model.Account;

import static com.softvision.bank.tdd.AccountMocks.*;

@AutoConfigureMockMvc
@WebMvcTest(controllers = BankAccountsController.class)
class BankAccountsControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	BankAccountsService bankAccountsService;

	static final ObjectMapper objectMapper = new ObjectMapper();
	@Nested
	@DisplayName("Get Account By Id Tests")
	class GetAccountByIdTests {
		@Test
		@DisplayName("Should get Regular Account by Id")
		void test_regular_getById() throws Exception {
			when(bankAccountsService.get(REG_MOCK_ACCT_ID)).thenReturn(getMockRegularAccount());

			mockMvc.perform(get("/accounts/" + REG_MOCK_ACCT_ID).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("type").value("regular")).andExpect(jsonPath("name").value(MOCK_NAME))
					.andExpect(jsonPath("acctNumber").value(REG_MOCK_ACCT_NO))
					.andExpect(jsonPath("balance").value(REG_MOCK_BALANCE))
					.andExpect(jsonPath("minimumBalance").value(ApplicationConstants.REG_MIN_BALANCE))
					.andExpect(jsonPath("penalty").value(ApplicationConstants.REG_PENALTY))
					.andExpect(jsonPath("transactionCharge").value(REG_MOCK_TRANSACTION))
					.andExpect(jsonPath("interestCharge").value(REG_MOCK_INTEREST));

			verify(bankAccountsService, atMostOnce()).get(anyLong());
		}

		@Test
		@DisplayName("Should get Checking Account by Id")
		void test_checking_getById() throws Exception {
			when(bankAccountsService.get(CHK_MOCK_ACCT_ID)).thenReturn(getMockCheckingAccount());

			mockMvc.perform(get("/accounts/" + CHK_MOCK_ACCT_ID).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("type").value("checking")).andExpect(jsonPath("name").value(MOCK_NAME))
					.andExpect(jsonPath("acctNumber").value(CHK_MOCK_ACCT_NO))
					.andExpect(jsonPath("balance").value(CHK_MOCK_BALANCE))
					.andExpect(jsonPath("minimumBalance").value(ApplicationConstants.CHK_MIN_BALANCE))
					.andExpect(jsonPath("penalty").value(ApplicationConstants.CHK_PENALTY))
					.andExpect(jsonPath("transactionCharge").value(ApplicationConstants.CHK_CHARGE))
					.andExpect(jsonPath("interestCharge").value(CHK_MOCK_INTEREST));

			verify(bankAccountsService, atMostOnce()).get(anyLong());
		}
		
		@Test
		@DisplayName("Should get Interest Account by Id")
		void test_interest_getById() throws Exception {
			when(bankAccountsService.get(INT_MOCK_ACCT_ID)).thenReturn(getMockInterestAccount());

			mockMvc.perform(get("/accounts/" + INT_MOCK_ACCT_ID).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("acctNumber").value(INT_MOCK_ACCT_NO))
					.andExpect(jsonPath("balance").value(INT_MOCK_BALANCE))
					.andExpect(jsonPath("interestCharge").value(ApplicationConstants.INT_INTEREST));

			verify(bankAccountsService, atMostOnce()).get(anyLong());
		}

		@Test
		@DisplayName("Should throw RecordNotFoundException")
		void test_throw_RecordNotFoundException() throws Exception {
			when(bankAccountsService.get(0L)).thenThrow(RecordNotFoundException.class);
			mockMvc.perform(get("/accounts/0").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());

			verify(bankAccountsService, atMostOnce()).get(anyLong());
		}

	}

	@Nested
	@DisplayName("Get Accounts")
	class GetAccountsTests {
		@Test
		@DisplayName("Should get all accounts")
		void test_get_all_accounts() throws Exception {

			List<Account> accounts = new ArrayList<>();
			accounts.add(getMockRegularAccount());
			accounts.add(getMockCheckingAccount());

			when(bankAccountsService.get()).thenReturn(accounts);

			mockMvc.perform(get("/accounts").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
					.andExpect(jsonPath("$[0].type").value("regular"))
					.andExpect(jsonPath("$[0].name").value(MOCK_NAME))
					.andExpect(jsonPath("$[0].acctNumber").value(REG_MOCK_ACCT_NO))
					.andExpect(jsonPath("$[0].balance").value(REG_MOCK_BALANCE))
					.andExpect(jsonPath("$[0].minimumBalance").value(ApplicationConstants.REG_MIN_BALANCE))
					.andExpect(jsonPath("$[0].penalty").value(ApplicationConstants.REG_PENALTY))
					.andExpect(jsonPath("$[0].transactionCharge").value(REG_MOCK_TRANSACTION))
					.andExpect(jsonPath("$[0].interestCharge").value(REG_MOCK_INTEREST))
					.andExpect(jsonPath("$[1].type").value("checking"))
					.andExpect(jsonPath("$[1].name").value(MOCK_NAME))
					.andExpect(jsonPath("$[1].acctNumber").value(CHK_MOCK_ACCT_NO))
					.andExpect(jsonPath("$[1].balance").value(CHK_MOCK_BALANCE))
					.andExpect(jsonPath("$[1].minimumBalance").value(ApplicationConstants.CHK_MIN_BALANCE))
					.andExpect(jsonPath("$[1].penalty").value(ApplicationConstants.CHK_PENALTY))
					.andExpect(jsonPath("$[1].transactionCharge").value(ApplicationConstants.CHK_CHARGE))
					.andExpect(jsonPath("$[1].interestCharge").value(CHK_MOCK_INTEREST));

			verify(bankAccountsService, atMostOnce()).get();
		}

		@Test
		@DisplayName("Should return empty list when no record found")
		void test_get_all_accounts_empty_list() throws Exception {
			when(bankAccountsService.get()).thenReturn(new ArrayList<>());

			MvcResult mvcResult = mockMvc.perform(get("/accounts").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andReturn();

			assertThat("[]").isEqualTo(mvcResult.getResponse().getContentAsString());
			verify(bankAccountsService, atMostOnce()).get();
		}
	}

	@Nested
	@DisplayName("Create Account")
	class CreateAccountTests {
		@Test
		@DisplayName("Should create regular account")
		void test_create_regular_account() throws Exception {
			when(bankAccountsService.createUpdate(argThat(account -> MOCK_NAME.equals(account.getName())
					&& account instanceof RegularAccount)))
				.thenReturn(getMockRegularAccount());

			mockMvc.perform(
					post("/accounts").content(objectMapper.writeValueAsString(getMockRegularAccount()))
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated()).andExpect(jsonPath("type").value("regular"))
					.andExpect(jsonPath("name").value(MOCK_NAME))
					.andExpect(jsonPath("acctNumber").value(REG_MOCK_ACCT_NO))
					.andExpect(jsonPath("balance").value(REG_MOCK_BALANCE))
					.andExpect(jsonPath("minimumBalance").value(ApplicationConstants.REG_MIN_BALANCE))
					.andExpect(jsonPath("penalty").value(ApplicationConstants.REG_PENALTY))
					.andExpect(jsonPath("transactionCharge").value(REG_MOCK_TRANSACTION))
					.andExpect(jsonPath("interestCharge").value(REG_MOCK_INTEREST));
			verify(bankAccountsService, atMostOnce()).createUpdate(any());
		}

		@Test
		@DisplayName("Should create checking account")
		void test_create_checking_account() throws Exception {
			when(bankAccountsService.createUpdate(argThat(account -> MOCK_NAME.equals(account.getName())
					&& account instanceof CheckingAccount)))
				.thenReturn(getMockCheckingAccount());

			mockMvc.perform(
					post("/accounts").content(objectMapper.writeValueAsString(getMockCheckingAccount()))
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated()).andExpect(jsonPath("type").value("checking"))
					.andExpect(jsonPath("name").value(MOCK_NAME))
					.andExpect(jsonPath("acctNumber").value(CHK_MOCK_ACCT_NO))
					.andExpect(jsonPath("balance").value(CHK_MOCK_BALANCE))
					.andExpect(jsonPath("minimumBalance").value(ApplicationConstants.CHK_MIN_BALANCE))
					.andExpect(jsonPath("penalty").value(ApplicationConstants.CHK_PENALTY))
					.andExpect(jsonPath("transactionCharge").value(ApplicationConstants.CHK_CHARGE))
					.andExpect(jsonPath("interestCharge").value(CHK_MOCK_INTEREST));
			verify(bankAccountsService, atMostOnce()).createUpdate(any());
		}
		
		@Test
		@DisplayName("Should create interest account")
		void test_create_interest_account() throws Exception {
			when(bankAccountsService.createUpdate(argThat(account -> MOCK_NAME.equals(account.getName())
					&& account instanceof InterestAccount)))
				.thenReturn(getMockInterestAccount());

			mockMvc.perform(
					post("/accounts").content(objectMapper.writeValueAsString(getMockInterestAccount()))
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated()).andExpect(jsonPath("type").value("interest"))
					.andExpect(jsonPath("acctNumber").value(INT_MOCK_ACCT_NO))
					.andExpect(jsonPath("balance").value(INT_MOCK_BALANCE))
					.andExpect(jsonPath("interestCharge").value(ApplicationConstants.INT_INTEREST));
			verify(bankAccountsService, atMostOnce()).createUpdate(any());
		}

		@Test
		@DisplayName("Should throw bad requst exception ")
		void test_throw_BadRequestException() throws Exception {
			when(bankAccountsService.createUpdate(argThat(account ->
					MOCK_NAME.equals(account.getName())
			))).thenThrow(BadRequestException.class);
			mockMvc.perform(post("/accounts").content(objectMapper.writeValueAsString(getMockRegularAccount()))
					.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
			verify(bankAccountsService, atMostOnce()).createUpdate(any());
		}
	}

}
