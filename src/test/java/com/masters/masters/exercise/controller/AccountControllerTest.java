package com.masters.masters.exercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masters.masters.exercise.exception.GlobalExceptionHandler;
import com.masters.masters.exercise.model.CheckingAccount;
import com.masters.masters.exercise.model.InterestAccount;
import com.masters.masters.exercise.model.dto.AccountDto;
import com.masters.masters.exercise.services.AccountServiceImpl;
import com.masters.masters.exercise.services.TransactionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AccountServiceImpl service;

    @MockBean
    TransactionImpl transactionService;

   @Test
    public void testCreateOrUpdateCheckingAccount() throws Exception {
        AccountDto dto = new AccountDto();
        dto.setName("test");
        dto.setType("CHECKING");
        CheckingAccount response = new CheckingAccount();
        response.setName("test");
        when(service.createOrUpdateAccount(Mockito.any(AccountDto.class))).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minimumBalance").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.penalty").value(10.0));
    }
   
   @Test
   public void testCreateOrUpdateInterestAccount() throws Exception {
       AccountDto dto = new AccountDto();
       dto.setName("test");
       dto.setType("interest");
       InterestAccount response = new InterestAccount();
       response.setName("test");
       when(service.createOrUpdateAccount(Mockito.any(AccountDto.class))).thenReturn(response);
       mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON)
                       .content(new ObjectMapper().writeValueAsString(dto)))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.interestCharge").value(0.03));
   }

}
