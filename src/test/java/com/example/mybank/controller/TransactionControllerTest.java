package com.example.mybank.controller;

import com.example.mybank.dto.AccountDTO;
import com.example.mybank.dto.AccountRegisterDTO;
import com.example.mybank.dto.TransactionDTO;
import com.example.mybank.dto.TransactionRegisterDTO;
import com.example.mybank.entity.Account;
import com.example.mybank.mapper.AccountMapper;
import com.example.mybank.repository.AccountRepository;
import com.example.mybank.repository.TransactionRepository;
import com.example.mybank.service.AccountService;
import com.example.mybank.service.TransactionService;
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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    AccountValidationService accountValidationService;

    @Autowired
    ObjectMapper mapper;

    private static String accountId1;
    private static String accountId2;


    @BeforeAll
    void setupData() throws Exception {
        accountId1 = uploadAccountAndGetId();
        accountId2 = uploadAccountAndGetId();
    }

    private String uploadAccountAndGetId() throws Exception {
        String requestBody = mapper.writeValueAsString(AccountRegisterDTO.builder().balance("1000").currency("EUR").build());
        MvcResult mvcResult = mockMvc.perform(post("/api/accounts/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        return mapper.readValue(response, AccountDTO.class).getId();
    }


    @Test
    @Order(1)
    void createTransaction() throws Exception {
        String requestBody = mapper.writeValueAsString(TransactionRegisterDTO.builder().sourceAccountId(accountId1).targetAccountId(accountId2).currency("EUR").amount("100").build());
        MvcResult mvcResult = mockMvc.perform(post("/api/transactions/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        TransactionDTO responseDto = mapper.readValue(response, TransactionDTO.class);

        Assertions.assertAll(
                () -> assertEquals(accountId1, responseDto.getSourceAccountId(), "Source account id does not match id of the request"),
                () -> assertEquals(accountId2, responseDto.getTargetAccountId(), "Target account id does not match id of the request"),
                () -> assertNotNull(responseDto.getCurrency(), "Currency must be present"),
                () -> assertNotNull(responseDto.getAmount(), "Amount must be present"),
                () -> assertNotNull(responseDto.getCreatedAt(), "Created date must be present")
        );
    }

    @Test
    @Order(2)
    void listTransactions() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/transactions/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        List<TransactionDTO> responseDtos = mapper.readValue(response, new TypeReference<>() {});

        Assertions.assertAll(
                () -> assertEquals(1, responseDtos.size(), "Transactions list size must be 1"),
                () -> assertEquals(accountId2, responseDtos.get(0).getTargetAccountId(), "Target account id does not match id of the request"),
                () -> assertNotNull(responseDtos.get(0).getCurrency(), "Currency must be present"),
                () -> assertNotNull(responseDtos.get(0).getAmount(), "Amount must be present"),
                () -> assertNotNull(responseDtos.get(0).getCreatedAt(), "Created date must be present")
        );
    }

    @Test
    @Order(3)
    void listAccountsTransactions() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        List<AccountDTO> accountDTOList = mapper.readValue(response, new TypeReference<>() {});
        assertEquals(2, accountDTOList.size(), "Accounts list size must be 2");

    }



    @AfterAll
    void cleanUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }
}