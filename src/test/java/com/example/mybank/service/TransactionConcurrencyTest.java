package com.example.mybank.service;

import com.example.mybank.dto.AccountDTO;
import com.example.mybank.dto.AccountRegisterDTO;
import com.example.mybank.dto.TransactionDTO;
import com.example.mybank.dto.TransactionRegisterDTO;
import com.example.mybank.entity.Account;
import com.example.mybank.mapper.AccountMapper;
import com.example.mybank.repository.AccountRepository;
import com.example.mybank.repository.TransactionRepository;
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
class TransactionConcurrencyTest {
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
    void testPerformConcurrentTransaction() throws InterruptedException {
        final TransactionRegisterDTO transactionRegisterDTO1 = TransactionRegisterDTO.builder().sourceAccountId(accountId1).targetAccountId(accountId2).amount("1000.0").currency("EUR").build();
        final TransactionRegisterDTO transactionRegisterDTO2 = TransactionRegisterDTO.builder().sourceAccountId(accountId1).targetAccountId(accountId2).amount("1000.0").currency("EUR").build();

        final ExecutorService executor = Executors.newFixedThreadPool(100);

        executor.execute(() -> transactionService.performTransaction(transactionRegisterDTO1));
        executor.execute(() -> transactionService.performTransaction(transactionRegisterDTO2));

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        Account account1 = accountRepository.findById(accountId1).get();
        Account account2 = accountRepository.findById(accountId2).get();

        assertAll(
                () -> assertEquals(0.0, account1.getBalance()),
                () -> assertEquals(2000.0, account2.getBalance())
        );
    }


    @AfterAll
    void cleanUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }
}