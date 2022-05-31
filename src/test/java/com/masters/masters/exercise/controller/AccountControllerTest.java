package com.masters.masters.exercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masters.masters.exercise.config.OAuthHelper;
import com.masters.masters.exercise.model.CheckingAccount;
import com.masters.masters.exercise.model.InterestAccount;
import com.masters.masters.exercise.model.dto.AccountDto;
import com.masters.masters.exercise.services.AccountServiceImpl;
import com.masters.masters.exercise.services.TransactionImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AccountServiceImpl service;

    @MockBean
    TransactionImpl transactionService;
    
    @MockBean
	private OAuthHelper	helper;
    
    private static final String CLIENT_ID = "devglan-client";
    
    private static final String NO_CLIENT_ID = "";
    
    private static final String USERNAME = "Zaldy";

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
   
   @Test
   public void testRetrieveAccountsWithSecurity() throws Exception {
	   RequestPostProcessor bearerToken = helper.bearerToken(CLIENT_ID, USERNAME);
	   mockMvc.perform(get("/accounts").with(bearerToken))
	   	.andExpect(status().isOk());
   }
   
   @Test
	public void testHelloWithoutRole() throws Exception {
		RequestPostProcessor bearerToken = helper.bearerToken(NO_CLIENT_ID, USERNAME);
		mockMvc.perform(get("/accounts").with(bearerToken)).andExpect(status().isForbidden());
	}

}
