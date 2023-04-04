package com.example.mybank.validation;

import com.example.mybank.common.CommonValidations;
import com.example.mybank.dto.TransactionRegisterDTO;
import com.example.mybank.entity.Account;
import com.example.mybank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TransactionValidationService {

    private final AccountRepository accountRepository;
    private final CommonValidations commonValidations;

    public Set<String> validateTransactionRegistration(TransactionRegisterDTO transactionRegisterDTO) {
        Set<String> errorMessages = new LinkedHashSet<>();
        validateTransaction(errorMessages, transactionRegisterDTO);
        return errorMessages;
    }


    private void validateTransaction(Set<String> errorMessages, TransactionRegisterDTO transactionRegisterDTO) {
        Optional<Account> sourceAccount = accountRepository.findById(transactionRegisterDTO.getSourceAccountId());
        Optional<Account> targetAccount = accountRepository.findById(transactionRegisterDTO.getTargetAccountId());

        // AC 4: One or more of the accounts does not exist
        if (!sourceAccount.isPresent()) {
            errorMessages.add("Source account id does not exist");
        }
        if (!targetAccount.isPresent()) {
            errorMessages.add("Target account id does not exist");
        }

        // Validate Amount
        if (!isValidAmount(transactionRegisterDTO, errorMessages)) {
            errorMessages.add("amount must be a number with maximum 2 decimal places");
        }

        // Validate Currency
        commonValidations.validateCurrency(transactionRegisterDTO.getCurrency(), errorMessages);

        if (errorMessages.isEmpty() && sourceAccount.isPresent() && targetAccount.isPresent()) {
            if (sourceAccount.get().getId().equals(targetAccount.get().getId())) {
                // AC 3: Transfer between same account
                errorMessages.add("Source and target account ids must be different");
            } else if (!isSufficientBalance(sourceAccount.get(), Double.parseDouble(transactionRegisterDTO.getAmount()))) {
                // AC 2: Insufficient balance to process money transfer
                errorMessages.add("Insufficient balance to process money transfer");
            }
        }
    }

    private boolean isSufficientBalance(Account sourceAccount, Double amount) {
        return sourceAccount.getBalance() - amount >= 0;
    }

    private boolean isValidAmount(TransactionRegisterDTO transactionRegisterDTO, Set<String> errorMessages) {
        try {
            double amount = Double.parseDouble(transactionRegisterDTO.getAmount());
            if (amount <= 0) {
                return false;
            } else if (!commonValidations.validDecimalPlaces(transactionRegisterDTO.getAmount())) {
                errorMessages.add("amount must be a number with maximum 2 decimal places");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
