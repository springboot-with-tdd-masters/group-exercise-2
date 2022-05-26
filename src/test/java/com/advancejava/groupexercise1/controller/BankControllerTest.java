package com.advancejava.groupexercise1.controller;

import com.advancejava.groupexercise1.entity.Account;
import com.advancejava.groupexercise1.entity.RegularAccount;
import com.advancejava.groupexercise1.model.AccountRequest;
import com.advancejava.groupexercise1.service.BankService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
public class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankService bankService;


    @Test
    @DisplayName("Save endpoint should return 200 on successful service invocation.")
    public void save() throws Exception {

        String request = "{\n" +
                "    \"name\": \"John Doe\",\n" +
                "    \"type\": \"regular\"\n" +
                "    \n" +
                "}";
        //act
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/accounts").content(
                        request).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

    }
}
