package com.example.mybank.controller;

import com.example.mybank.dto.AccountDTO;
import com.example.mybank.dto.AccountRegisterDTO;
import com.example.mybank.openapi.AccountControllerOpenApiSpec;
import com.example.mybank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/accounts/")
@RequiredArgsConstructor
public class AccountController implements AccountControllerOpenApiSpec {

    private final AccountService accountService;

    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountDTO>> getAccounts() {
        return accountService.listAccounts();
    }

    @GetMapping(value = "list/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccount(@PathVariable(name = "id") String accountId) {
        return accountService.listAccount(accountId);
    }

    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAccount(@RequestBody AccountRegisterDTO accountRegisterDTO) {
        return accountService.createAccount(accountRegisterDTO);
    }

    @PutMapping(value = "update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAccount(@PathVariable(name = "id") String accountId,
                                           @RequestBody AccountRegisterDTO accountRegisterDTO) {
        return accountService.updateAccount(accountId, accountRegisterDTO);
    }

}
