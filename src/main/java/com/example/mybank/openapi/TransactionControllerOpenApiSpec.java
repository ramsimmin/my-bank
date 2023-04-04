package com.example.mybank.openapi;

import com.example.mybank.dto.TransactionDTO;
import com.example.mybank.dto.TransactionRegisterDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Transaction Controller", description = "Api to manage transactions")
public interface TransactionControllerOpenApiSpec {


    @Operation(summary = "List transactions")
    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<TransactionDTO>> listTransactions();

    @Operation(summary = "List transactions by account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"
            ),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @GetMapping(value = "listByAccount", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<TransactionDTO>> listTransactions(@Parameter(name = "sourceAccountId", description = "The source account id", example = "26586569-708a-45e7-aef3-5a15c01e275b") @RequestParam(required = false) String sourceAccountId,
                                                          @Parameter(name = "targetAccountId", description = "The target account id", example = "20a54576-95f2-4e32-83c6-c477c0cd5db9") @RequestParam(required = false) String targetAccountId);

    @Operation(summary = "Create transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/TransactionDTO"))}
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(name = "Insufficient balance", value = "[\"Insufficient balance to process money transfer\"]"),
                                    @ExampleObject(name = "Transfer between same account", value = "[\"Source and target account ids must be different\"]"),
                                    @ExampleObject(name = "Invalid account", value = "[\"Source account id does not exist\", \"Target account id does not exist\"]"),
                                    @ExampleObject(name = "Invalid amount number", value = "[\"Amount must be a valid positive number\"]"),
                                    @ExampleObject(name = "Invalid currency", value = "[\"currency must be one of [EUR, USD, GBP]\"]"),

                            }
                    )})
    })
    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createTransaction(@RequestBody TransactionRegisterDTO transactionRegisterDTO);
}
