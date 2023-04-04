package com.example.mybank.openapi;

import com.example.mybank.dto.AccountDTO;
import com.example.mybank.dto.AccountRegisterDTO;
import com.example.mybank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "Account Controller", description = "Api to manage accounts")
public interface AccountControllerOpenApiSpec {

    String responseAccountCreationExample = """
            {
                "id": "9b5089e2-cfd3-462e-909b-8145cce2c9bc",
                "balance": 1000.0,
                "currency": "EUR",
                "createdAt": "2023-04-02T16:33:36.148444Z"
            }""";

    @Operation(summary = "List accounts")
    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<AccountDTO>> getAccounts();


    @Operation(summary = "List account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/AccountDTO")
                    )}
            ),
            @ApiResponse(responseCode = "404", description = "Account id not found",
                    content = @Content)})
    @GetMapping(value = "list/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAccount(@Parameter(name = "id", example = "a0d0a065-8706-4501-81b2-fa71be9957ef", description = "The account id") @PathVariable(name = "id") String accountId);


    @Operation(summary = "Create an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/AccountDTO"),
                            examples = @ExampleObject(value = responseAccountCreationExample )
                    )}
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = @ExampleObject(name = "Invalid request body", value = "[\"balance must be a valid number\", \"currency must be one of [EUR, USD, GBP]\"]")

                    )})
    })
    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createAccount(@RequestBody AccountRegisterDTO accountRegisterDTO);


    @Operation(summary = "Update an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/AccountDTO")
                    )}
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = @ExampleObject(name = "Invalid request body", value = "[\"balance must be a valid number\", \"currency must be one of [EUR, USD, GBP]\"]")

                    )})
    })
    @PutMapping(value = "update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateAccount(@Parameter(name = "id", example = "a0d0a065-8706-4501-81b2-fa71be9957ef", description = "The account id to be updated") @PathVariable(name = "id") String accountId,
                                    @RequestBody AccountRegisterDTO accountRegisterDTO);

}
