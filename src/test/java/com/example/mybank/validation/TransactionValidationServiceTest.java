package com.example.mybank.validation;

import com.example.mybank.common.CommonValidation;
import com.example.mybank.dto.TransactionRegisterDTO;
import com.example.mybank.entity.Account;
import com.example.mybank.enums.CurrencyCode;
import com.example.mybank.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;

@SpringBootTest(classes = {TransactionValidationService.class, CommonValidation.class})
class TransactionValidationServiceTest {

    @Autowired
    TransactionValidationService transactionValidationService;

    @MockBean
    AccountRepository accountRepository;

    @Autowired
    CommonValidation commonValidation;

    private final String richId = "afb85a91-c64a-4f6d-bcd7-c6f6315c8015";
    private final String poorId = "47537a26-3c82-45b8-820b-47c8f50a31e8";

    @BeforeEach
    public void mockData() {
        Mockito.when(accountRepository.findById(richId)).thenReturn(
                Optional.of(Account.builder().id(richId).balance(1000.0).currency(CurrencyCode.EUR.name()).build())
        );
        Mockito.when(accountRepository.findById(poorId)).thenReturn(
                Optional.of(Account.builder().id(poorId).balance(0.0).currency(CurrencyCode.EUR.name()).build())
        );
    }

    @Test
    void testValidTransactionRegistration() {
        TransactionRegisterDTO dto = TransactionRegisterDTO.builder().sourceAccountId(richId).targetAccountId(poorId).amount("100.0").currency(CurrencyCode.EUR.name()).build();
        Set<String> errorMessages = transactionValidationService.validateTransactionRegistration(dto);
        Assertions.assertTrue(errorMessages.isEmpty());
    }

    @Test
    void testTransactionRegistrationTargetAccountIdInvalid() {
        Set<String> expectedErrorMessages = Set.of("Target account id does not exist");
        TransactionRegisterDTO dto = TransactionRegisterDTO.builder().sourceAccountId(richId).targetAccountId("abc").amount("100.0").currency(CurrencyCode.EUR.name()).build();
        Set<String> errorMessages = transactionValidationService.validateTransactionRegistration(dto);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }

    @Test
    void testTransactionRegistrationSourceAccountIdInvalid() {
        Set<String> expectedErrorMessages = Set.of("Source account id does not exist");
        TransactionRegisterDTO dto = TransactionRegisterDTO.builder().sourceAccountId("abc").targetAccountId(poorId).amount("100.0").currency(CurrencyCode.EUR.name()).build();
        Set<String> errorMessages = transactionValidationService.validateTransactionRegistration(dto);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }

    @Test
    void testTransactionRegistrationInvalidAmount() {
        Set<String> expectedErrorMessages = Set.of("amount must be a number with maximum 2 decimal places");
        TransactionRegisterDTO dto = TransactionRegisterDTO.builder().sourceAccountId(richId).targetAccountId(poorId).amount("-10").currency(CurrencyCode.EUR.name()).build();
        Set<String> errorMessages = transactionValidationService.validateTransactionRegistration(dto);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }

    @Test
    void testTransactionRegistrationInvalidAmountDecimal() {
        Set<String> expectedErrorMessages = Set.of("amount must be a number with maximum 2 decimal places");
        TransactionRegisterDTO dto = TransactionRegisterDTO.builder().sourceAccountId(richId).targetAccountId(poorId).amount("100.001234").currency(CurrencyCode.EUR.name()).build();
        Set<String> errorMessages = transactionValidationService.validateTransactionRegistration(dto);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }


    @Test
    void testTransactionRegistrationSameAccounts() {
        Set<String> expectedErrorMessages = Set.of("Source and target account ids must be different");
        TransactionRegisterDTO dto = TransactionRegisterDTO.builder().sourceAccountId(richId).targetAccountId(richId).amount("10").currency(CurrencyCode.EUR.name()).build();
        Set<String> errorMessages = transactionValidationService.validateTransactionRegistration(dto);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }

    @Test
    void testTransactionRegistrationInsufficientBalance() {
        Set<String> expectedErrorMessages = Set.of("Insufficient balance to process money transfer");
        TransactionRegisterDTO dto = TransactionRegisterDTO.builder().sourceAccountId(poorId).targetAccountId(richId).amount("100").currency(CurrencyCode.EUR.name()).build();
        Set<String> errorMessages = transactionValidationService.validateTransactionRegistration(dto);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }




}