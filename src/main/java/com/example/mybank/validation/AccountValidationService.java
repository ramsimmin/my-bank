package com.example.mybank.validation;

import com.example.mybank.common.CommonValidations;
import com.example.mybank.dto.AccountRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountValidationService {
    private final CommonValidations commonValidations;

    public Set<String> validateAccountRegistration(AccountRegisterDTO accountRegisterDTO) {
        Set<String> errorMessages = new HashSet<>();
        validateBalance(accountRegisterDTO, errorMessages);
        commonValidations.validateCurrency(accountRegisterDTO.getCurrency(), errorMessages);
        return errorMessages;
    }

    public void validateBalance(AccountRegisterDTO accountRegisterDTO, Set<String> errorMessages) {
        if (StringUtils.isBlank(accountRegisterDTO.getBalance())) {
            errorMessages.add("balance must be provided");
        } else {
            try {
                double balance = Double.parseDouble(accountRegisterDTO.getBalance());
                if (balance <= 0) {
                    errorMessages.add("balance must be a positive number");
                } else if (!commonValidations.validDecimalPlaces(accountRegisterDTO.getBalance())) {
                    errorMessages.add("balance must be a number with maximum 2 decimal places");
                }
            } catch (Exception e) {
                errorMessages.add("balance must be a number with maximum 2 decimal places");
            }
        }
    }

}
