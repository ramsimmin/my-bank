package com.example.mybank.controller;

import com.example.mybank.dto.AccountDTO;
import com.example.mybank.dto.AccountRegisterDTO;
import com.example.mybank.mapper.AccountMapper;
import com.example.mybank.repository.AccountRepository;
import com.example.mybank.service.AccountService;
import com.example.mybank.validation.AccountValidationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    AccountValidationService accountValidationService;
    @Autowired
    ObjectMapper mapper;

    private static String accountId;


    @BeforeAll
    void setupData() throws Exception {
        String requestBody = mapper.writeValueAsString(AccountRegisterDTO.builder().balance("1000").currency("EUR").build());
        MvcResult mvcResult = mockMvc.perform(post("/api/accounts/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        accountId = mapper.readValue(response, AccountDTO.class).getId();
    }

    @Test
    void updateAccount() throws Exception {
        String requestBody = mapper.writeValueAsString(AccountRegisterDTO.builder().balance("2000").currency("EUR").build());
        mockMvc.perform(put("/api/accounts/update/{id}", accountId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    void updateNonExistingAccount() throws Exception {
        String requestBody = mapper.writeValueAsString(AccountRegisterDTO.builder().balance("2000").currency("EUR").build());
        MvcResult mvcResult = mockMvc.perform(put("/api/accounts/update/{id}", "abc")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assert.isTrue(response.contains("Account id not found"), "Error expected");
    }


    @Test
    void getAccounts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/list")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<AccountDTO> responseAccountsList = mapper.readValue(response, new TypeReference<>() {});
        Assertions.assertAll(
                () -> assertTrue(responseAccountsList.size() ==1, "Accounts list size must be 1"),
                () -> assertEquals(accountId, responseAccountsList.get(0).getId(), "Accounts id is not the one expected"),
                () -> assertNotNull(responseAccountsList.get(0).getCurrency(), "Currency must be present"),
                () -> assertNotNull(responseAccountsList.get(0).getBalance(), "Balance must be present")
        );

    }

    @Test
    void getAccount() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/list/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        AccountDTO responseAccount = mapper.readValue(response, AccountDTO.class);
        Assertions.assertAll(
                () -> assertEquals(accountId, responseAccount.getId(), "Accounts id is not the one expected"),
                () -> assertNotNull(responseAccount.getCurrency(), "Currency must be present"),
                () -> assertNotNull(responseAccount.getBalance(), "Balance must be present"),
                () -> assertNotNull(responseAccount.getCreatedAt(), "Date created must be present")
        );
    }

    @AfterAll
    void cleanUp () {
        accountRepository.deleteAll();
    }
}