package com.softvision.bank.tdd.controllers;

import static com.softvision.bank.tdd.AccountMocks.CHK_MOCK_ACCT_ID;
import static com.softvision.bank.tdd.AccountMocks.CHK_MOCK_ACCT_NO;
import static com.softvision.bank.tdd.AccountMocks.CHK_MOCK_BALANCE;
import static com.softvision.bank.tdd.AccountMocks.CHK_MOCK_INTEREST;
import static com.softvision.bank.tdd.AccountMocks.INT_MOCK_ACCT_ID;
import static com.softvision.bank.tdd.AccountMocks.INT_MOCK_ACCT_NO;
import static com.softvision.bank.tdd.AccountMocks.INT_MOCK_BALANCE;
import static com.softvision.bank.tdd.AccountMocks.MOCK_NAME;
import static com.softvision.bank.tdd.AccountMocks.REG_MOCK_ACCT_ID;
import static com.softvision.bank.tdd.AccountMocks.REG_MOCK_ACCT_NO;
import static com.softvision.bank.tdd.AccountMocks.REG_MOCK_BALANCE;
import static com.softvision.bank.tdd.AccountMocks.REG_MOCK_INTEREST;
import static com.softvision.bank.tdd.AccountMocks.REG_MOCK_TRANSACTION;
import static com.softvision.bank.tdd.AccountMocks.getMockCheckingAccount;
import static com.softvision.bank.tdd.AccountMocks.getMockInterestAccount;
import static com.softvision.bank.tdd.AccountMocks.getMockRegularAccount;
import static com.softvision.bank.tdd.UserMocks.MOCK_USER1_USERNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softvision.bank.tdd.ApplicationConstants;
import com.softvision.bank.tdd.SecurityTestConfig;
import com.softvision.bank.tdd.exceptions.BadRequestException;
import com.softvision.bank.tdd.exceptions.RecordNotFoundException;
import com.softvision.bank.tdd.model.Account;
import com.softvision.bank.tdd.model.CheckingAccount;
import com.softvision.bank.tdd.model.InterestAccount;
import com.softvision.bank.tdd.model.RegularAccount;
import com.softvision.bank.tdd.services.BankAccountsService;

@AutoConfigureMockMvc
@SpringBootTest(classes = SecurityTestConfig.class)
class BankAccountsControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	BankAccountsService bankAccountsService;

	static final ObjectMapper objectMapper = new ObjectMapper();
	@Nested
	@DisplayName("Get Account By Id Tests")
	@WithUserDetails(MOCK_USER1_USERNAME)
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
	@WithUserDetails(MOCK_USER1_USERNAME)
	class GetAccountsTests {
		@Test
		@DisplayName("Should get accounts with pageable sorted by Name")
		void test_get_accounts_pageable_sort_by_name() throws Exception {
			List<Account> accounts = new ArrayList<>();
			accounts.add(getMockRegularAccount());
			CheckingAccount mockCheckingAccount = getMockCheckingAccount();
			mockCheckingAccount.setName("Anderson Brooke");
			accounts.add(mockCheckingAccount);

			Pageable pageRequest = PageRequest.of(0, 3, Sort.by("name").ascending());
			List<Account> sortedAccounts = accounts.stream().sorted(Comparator.comparing(Account::getName)).collect(Collectors.toList());
			Page<Account> accountPage = new PageImpl<>(sortedAccounts);
			when(bankAccountsService.get(pageRequest)).thenReturn(accountPage);

			mockMvc.perform(get("/accounts")
							.contentType(MediaType.APPLICATION_JSON)
							.param("page", "0")
							.param("size", "3")
							.param("sort", "name,asc")
					)
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.content.[0].name").value("Anderson Brooke"))
					.andExpect(jsonPath("$.content.[1].name").value(MOCK_NAME));

			verify(bankAccountsService, atMostOnce()).get(pageRequest);
		}

		@Test
		@DisplayName("Should return empty list when no record found")
		void test_get_all_accounts_empty_list() throws Exception {
			Pageable pageRequest = PageRequest.of(0, 3, Sort.by("name").ascending());
			when(bankAccountsService.get(pageRequest)).thenReturn(new PageImpl<>(new ArrayList<>()));

			mockMvc.perform(get("/accounts").contentType(MediaType.APPLICATION_JSON)
							.contentType(MediaType.APPLICATION_JSON)
							.param("page", "0")
							.param("size", "3")
							.param("sort", "name,asc")
					).andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.content").isEmpty());

			verify(bankAccountsService, atMostOnce()).get(pageRequest);
		}
	}

	@Nested
	@DisplayName("Create Account")
	@WithUserDetails(MOCK_USER1_USERNAME)
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
		@DisplayName("Should throw bad request exception ")
		void test_throw_BadRequestException() throws Exception {
			when(bankAccountsService.createUpdate(argThat(account ->
					MOCK_NAME.equals(account.getName())
			))).thenThrow(BadRequestException.class);
			mockMvc.perform(post("/accounts").content(objectMapper.writeValueAsString(getMockRegularAccount()))
					.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
			verify(bankAccountsService, atMostOnce()).createUpdate(any());
		}
	}

	@Test
	@DisplayName("Given an anonymous user, response should give http status 403 (forbidden).")
	@WithAnonymousUser()
	void test_getAll_fail_unauthorized() throws Exception {
		mockMvc.perform(get("/accounts")).andExpect(status().isForbidden());
	}

}
