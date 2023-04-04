package com.example.mybank.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class CommonValidationsTest {

    @MockBean
    CommonValidations commonValidations;

    @Test
    void testCurrencyAcceptedValues() {
        Set<String> expectedErrorMessages = Set.of("currency must be one of [EUR, USD, GBP]");
        Set<String> errorMessages = new HashSet<>();
        commonValidations.validateCurrency("abc", errorMessages);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }

    @Test
    void testCurrencyNotEmpty() {
        Set<String> expectedErrorMessages = Set.of("currency must be provided");
        Set<String> errorMessages = new HashSet<>();
        commonValidations.validateCurrency("", errorMessages);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }

    @Test
    void testCurrencyNotBlank() {
        Set<String> expectedErrorMessages = Set.of("currency must be provided");
        Set<String> errorMessages = new HashSet<>();
        commonValidations.validateCurrency("    ", errorMessages);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }

    @Test
    void testCurrencyNotNull() {
        Set<String> expectedErrorMessages = Set.of("currency must be provided");
        Set<String> errorMessages = new HashSet<>();
        commonValidations.validateCurrency(null, errorMessages);
        Assertions.assertTrue(expectedErrorMessages.containsAll(errorMessages));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidAmountDecimalPlaces")
    public void testInvalidAmount(String input) {
        assertFalse(commonValidations.validDecimalPlaces(input) , "Input: " + input + " expected to be invalid");
    }

    private static List<String> provideInvalidAmountDecimalPlaces() {
        return List.of(
                "10.013",
                "10.000",
                "0.3145987"
        );
    }



}