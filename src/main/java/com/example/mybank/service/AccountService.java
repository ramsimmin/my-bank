package com.example.mybank.service;

import com.example.mybank.dto.AccountDTO;
import com.example.mybank.dto.AccountRegisterDTO;
import com.example.mybank.enums.CurrencyCode;
import com.example.mybank.repository.AccountDAO;
import com.example.mybank.validation.AccountValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountDAO accountDAO;
    private final AccountValidationService accountValidationService;

    public ResponseEntity<List<AccountDTO>> listAccounts() {
        List<AccountDTO> accountDTOList = accountDAO.findAll();
        return ResponseEntity.ok(accountDTOList);
    }

    public ResponseEntity<?> listAccount(String accountId) {
        Optional<AccountDTO> accountDto = accountDAO.findById(accountId);
        if (accountDto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account id not found");
        } else {
            return ResponseEntity.ok(accountDto);
        }
    }

    public ResponseEntity<?> createAccount(AccountRegisterDTO accountRegisterDTO) {
        Set<String> errorMessages = accountValidationService.validateAccountRegistration(accountRegisterDTO);
        if (!errorMessages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        } else {
            AccountDTO accountDTO = buildAccountDTO(accountRegisterDTO);
            AccountDTO savedAccountDTO = accountDAO.save(accountDTO);
            return ResponseEntity.ok(savedAccountDTO);
        }
    }

    public ResponseEntity<?> updateAccount(String accountId, AccountRegisterDTO accountRegisterDTO) {
        Set<String> errorMessages = new HashSet<>();
        Optional<AccountDTO> accountDTO = accountDAO.findById(accountId);
        if (accountDTO.isEmpty()) {
            errorMessages.add("Account id not found");
        } else {
            errorMessages.addAll(accountValidationService.validateAccountRegistration(accountRegisterDTO));
        }

        if (!errorMessages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        } else {
            AccountDTO updatedAccountDto = performAccountUpdate(accountDTO.get(), accountRegisterDTO);
            return ResponseEntity.ok(updatedAccountDto);
        }
    }

     synchronized AccountDTO performAccountUpdate(AccountDTO accountDTO, AccountRegisterDTO accountRegisterDTO) {
        accountDTO.setBalance(Double.parseDouble(accountRegisterDTO.getBalance()));
        accountDTO.setCurrency(CurrencyCode.valueOf(accountRegisterDTO.getCurrency()));
        return accountDAO.saveAndFlush(accountDTO);
    }


    public AccountDTO buildAccountDTO(AccountRegisterDTO accountRegisterDTO) {
        return AccountDTO.builder()
                .id(UUID.randomUUID().toString())
                .balance(Double.parseDouble(accountRegisterDTO.getBalance()))
                .currency(CurrencyCode.valueOf(accountRegisterDTO.getCurrency()))
                .build();
    }

}
