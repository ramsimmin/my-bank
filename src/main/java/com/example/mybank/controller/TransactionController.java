package com.example.mybank.controller;

import com.example.mybank.dto.TransactionDTO;
import com.example.mybank.dto.TransactionRegisterDTO;
import com.example.mybank.openapi.TransactionControllerOpenApiSpec;
import com.example.mybank.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/transactions/")
@RequiredArgsConstructor
public class TransactionController implements TransactionControllerOpenApiSpec {

    private final TransactionService transactionService;


    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionDTO>> listTransactions() {
        return transactionService.getTransactions();
    }

    @GetMapping(value = "listByAccount", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionDTO>> listTransactions(@RequestParam(required = false) String sourceAccountId, @RequestParam(required = false) String targetAccountId) {
        return transactionService.getTransactions(sourceAccountId, targetAccountId);
    }

    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRegisterDTO transactionRegisterDTO) {
        return transactionService.createTransaction(transactionRegisterDTO);
    }
}
