package com.masters.masters.exercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masters.masters.exercise.model.Account;
import com.masters.masters.exercise.model.RegularAccount;
import com.masters.masters.exercise.model.dto.AccountDto;
import com.masters.masters.exercise.model.dto.TransactionDto;
import com.masters.masters.exercise.services.AccountServiceImpl;
import com.masters.masters.exercise.services.TransactionImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionImpl transactionService;

    @MockBean
    private AccountServiceImpl accountService;


    @Test
    @DisplayName("Should be able to deposit to regular account")
    public void shouldeBeAbleToDepositToRegularAccount() throws Exception {

        Account dto = new RegularAccount();
        dto.setName("group 4");
        dto.setId(1L);

        when(transactionService.deposit(dto, 100d)).thenReturn(dto);

        TransactionDto transactRequest = new TransactionDto();
        transactRequest.setType("deposit");
        transactRequest.setAmount(100d);

        this.mockMvc.perform(post("/accounts/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(transactRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("regular"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("group 4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("600.0"));

    }

    @Test
    @DisplayName("Should be able to withdraw from regular account")
    public void shouldeBeAbleToWithdrawFromRegularAccount() throws Exception {

        Account dto = new RegularAccount();
        dto.setName("group 4");
        dto.setId(1L);

        when(transactionService.deposit(dto, 100d)).thenReturn(dto);

        TransactionDto transactRequest = new TransactionDto();
        transactRequest.setType("withdraw");
        transactRequest.setAmount(100d);

        this.mockMvc.perform(post("/accounts/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(transactRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("regular"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("group 4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("400.0"));
    }

}
