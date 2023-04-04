package com.example.mybank.common;

import com.example.mybank.enums.CurrencyCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
public class CommonValidation {

    public void validateCurrency(String currency, Set<String> errorMessages) {
        if (StringUtils.isBlank(currency)) {
            errorMessages.add("currency must be provided");
        } else {
            try {
                CurrencyCode.valueOf(currency);
            } catch (Exception e) {
                errorMessages.add("currency must be one of " + Arrays.stream(CurrencyCode.values()).toList());
            }
        }
    }

    public boolean validDecimalPlaces(String amountStr) {
        if (amountStr.indexOf(".") > -1) {
            String decimalPlaces = amountStr.substring(amountStr.indexOf(".") + 1, amountStr.length());
            return decimalPlaces.length() < 2 ;
        }
        return true;
    }

}
