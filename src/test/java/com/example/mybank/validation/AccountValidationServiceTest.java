package com.example.mybank.validation;

import com.example.mybank.dto.AccountRegisterDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class AccountValidationServiceTest {
    @MockBean
    AccountValidationService accountValidationService;

    @Test
    void validateAccountRegistration() {
    }

    @Test
    void testBalanceNotEmpty() {
        Set<String> expectedErrorMessages = Set.of("balance must be provided");
        Set<String> errorMessages = new HashSet<>();
        AccountRegisterDTO dto = AccountRegisterDTO.builder().balance("").currency("EUR").build();
        accountValidationService.validateBalance(dto, errorMessages);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }

    @Test
    void testBalanceNotBlank() {
        Set<String> expectedErrorMessages = Set.of("balance must be provided");
        Set<String> errorMessages = new HashSet<>();
        AccountRegisterDTO dto = AccountRegisterDTO.builder().balance(" ").currency("EUR").build();
        accountValidationService.validateBalance(dto, errorMessages);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }

    @Test
    void testBalanceIsValidNumber() {
        Set<String> expectedErrorMessages = Set.of("balance must be a valid number");
        Set<String> errorMessages = new HashSet<>();
        AccountRegisterDTO dto = AccountRegisterDTO.builder().balance("abc").currency("EUR").build();
        accountValidationService.validateBalance(dto, errorMessages);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }

    @Test
    void testBalanceIsValidNumber2() {
        Set<String> expectedErrorMessages = Set.of("balance must be a valid number");
        Set<String> errorMessages = new HashSet<>();
        AccountRegisterDTO dto = AccountRegisterDTO.builder().balance("10,15879").currency("EUR").build();
        accountValidationService.validateBalance(dto, errorMessages);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }


    @Test
    void testBalanceIsGreaterThanZero() {
        Set<String> expectedErrorMessages = Set.of("balance must be a positive number");
        Set<String> errorMessages = new HashSet<>();
        AccountRegisterDTO dto = AccountRegisterDTO.builder().balance("-0.01").currency("EUR").build();
        accountValidationService.validateBalance(dto, errorMessages);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }

    @Test
    void testValidBalance() {
        Set<String> errorMessages = new HashSet<>();
        AccountRegisterDTO dto = AccountRegisterDTO.builder().balance("100").currency("EUR").build();
        accountValidationService.validateBalance(dto, errorMessages);
        Assertions.assertTrue(errorMessages.isEmpty());
    }

    @Test
    void testValidAccountRegistration() {
        AccountRegisterDTO dto = AccountRegisterDTO.builder().balance("0.01").currency("EUR").build();
        Set<String> errorMessages = accountValidationService.validateAccountRegistration(dto);
        Assertions.assertTrue(errorMessages.isEmpty());
    }



}