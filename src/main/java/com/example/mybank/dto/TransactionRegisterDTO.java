package com.example.mybank.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRegisterDTO {
    @Schema(type = "string", example = "a0d0a065-8706-4501-81b2-fa71be9957ef")
    private String sourceAccountId;
    @Schema(type = "string", example = "fcf9644c-93c3-4dfd-b2a8-26f9b3686d82")
    private String targetAccountId;
    @Schema(type = "double", example = "100.00")
    private String amount;
    @Schema(type = "string", example = "EUR")
    private String currency;

}
