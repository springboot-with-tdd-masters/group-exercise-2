package com.group3.exercise.bankapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.exercise.bankapp.constants.TransactionTypes;
import com.group3.exercise.bankapp.exceptions.BankAppException;
import com.group3.exercise.bankapp.exceptions.BankAppExceptionCode;
import com.group3.exercise.bankapp.request.TransactionRequest;
import com.group3.exercise.bankapp.response.TransactionLogResponse;
import com.group3.exercise.bankapp.services.transaction.TransactionLogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(TransactionLogController.class)
@ExtendWith(MockitoExtension.class)
public class TransactionLogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    TransactionLogService transactionLogService;

    @Test
    @DisplayName("Should return all transaction log given the account id on params")
    void getAll_shouldReturnPaginatedTransactionLogResponse() throws Exception {
        // Arrange
        final Long accountId = 1L;

        final TransactionLogResponse firstTransactionLogResponse = new TransactionLogResponse();
        firstTransactionLogResponse.setId(1L);
        firstTransactionLogResponse.setAccountId(accountId);
        firstTransactionLogResponse.setAmount(100.0);
        firstTransactionLogResponse.setTransactionType(TransactionTypes.WITHDRAW.value());

        final List<TransactionLogResponse> transactionLogResponses = Arrays.asList(firstTransactionLogResponse);
        final PageRequest pageRequest = PageRequest.of(0, 2);
        Page<TransactionLogResponse> pageResponse = new PageImpl<>(transactionLogResponses, pageRequest, transactionLogResponses.size());

        when(transactionLogService.findAllByAccountId(accountId, pageRequest))
                .thenReturn(pageResponse);

        // Act
        final ResultActions resultActions = mockMvc.perform(get("/accounts/1/transactions?page=0&size=2")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(1)));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].transactionType", is(TransactionTypes.WITHDRAW.value())));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].amount", is(100.0)));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].accountId", is(1)));

        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber", is(0)));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize", is(2)));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.totalPages", is(1)));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(1)));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.size", is(2)));
    }

    @Test
    @DisplayName("Should return 404 if account doesn't exists")
    void getAll_shouldReturn404IfAccountDoesntExists() throws Exception {
        // Arrange
        when(transactionLogService.findAllByAccountId(1L, PageRequest.of(0, 2)))
                .thenThrow(new BankAppException(BankAppExceptionCode.ACCOUNT_NOT_FOUND_EXCEPTION));

        // Act
        final ResultActions resultActions = mockMvc.perform(get("/accounts/1/transactions?page=0&size=2")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.error", is("Unable to process your request. Account does not exists")));
    }

    @Test
    @DisplayName("Should return created transaction log")
    void create_shouldReturnTransactionLog() throws Exception {
        // Arrange
        final Long accountId = 1L;

        final TransactionLogResponse transactionLogResponse = new TransactionLogResponse();
        transactionLogResponse.setId(1L);
        transactionLogResponse.setAccountId(accountId);
        transactionLogResponse.setAmount(100.0);
        transactionLogResponse.setTransactionType(TransactionTypes.WITHDRAW.value());

        final TransactionRequest transactionRequest = TransactionRequest.of("deposit", 100.0);

        when(transactionLogService.createLogFor(anyLong(), any(TransactionRequest.class)))
                .thenReturn(transactionLogResponse);

        // Act
        final ResultActions resultActions = mockMvc.perform(post("/accounts/1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transactionRequest)));

        // Assert
        verify(transactionLogService)
                .createLogFor(anyLong(), any(TransactionRequest.class));

        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.transactionType", is(TransactionTypes.WITHDRAW.value())));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.amount", is(100.0)));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.accountId", is(1)));
    }

    @Test
    @DisplayName("Should return 404 if account doesn't exists")
    void create_shouldReturn404IfAccountDoesntExists() throws Exception {
        // Arrange

        final TransactionRequest transactionRequest = TransactionRequest.of("deposit", 100.0);

        when(transactionLogService.createLogFor(anyLong(), any(TransactionRequest.class)))
                .thenThrow(new BankAppException(BankAppExceptionCode.ACCOUNT_NOT_FOUND_EXCEPTION));

        // Act
        final ResultActions resultActions = mockMvc.perform(post("/accounts/1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transactionRequest)));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.error", is("Unable to process your request. Account does not exists")));
    }

    @Test
    @DisplayName("Should return 400 request has invalid transaction type")
    void create_shouldReturn400IfTransactionTypeIsInvalid() throws Exception {
        // Arrange

        final TransactionRequest transactionRequest = TransactionRequest.of("invalid transaction", 100.0);

        when(transactionLogService.createLogFor(anyLong(), any(TransactionRequest.class)))
                .thenThrow(new BankAppException(BankAppExceptionCode.TRANSACTION_TYPE_EXCEPTION));

        // Act
        final ResultActions resultActions = mockMvc.perform(post("/accounts/1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transactionRequest)));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.error", is("Invalid Transaction Type")));
    }

    @Test
    @DisplayName("Should return 200 and proper message after delete")
    void delete_shouldReturn200() throws Exception {
        // Arrange

        // Act
        final ResultActions resultActions = mockMvc.perform(delete("/accounts/1/transactions/1")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        resultActions.andExpect(MockMvcResultMatchers.content().string("Delete Successful"));
    }

    @Test
    @DisplayName("Should return 404 if id doesn't exists")
    void delete_shouldReturn404() throws Exception {
        // Arrange
        doThrow(new BankAppException(BankAppExceptionCode.TRANSACTION_LOG_NOT_FOUND_EXCEPTION))
                .when(transactionLogService)
                .delete(anyLong());

        // Act
        final ResultActions resultActions = mockMvc.perform(delete("/accounts/1/transactions/1")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
