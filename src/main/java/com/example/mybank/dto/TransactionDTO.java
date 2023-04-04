package com.example.mybank.dto;


import com.example.mybank.enums.CurrencyCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    @Schema(type = "string", example = "dca13251-8375-4d21-8ebe-2db8ed47b8b4")
    private String id;
    @Schema(type = "string", example = "a0d0a065-8706-4501-81b2-fa71be9957ef")
    private String sourceAccountId;
    @Schema(type = "string", example = "fcf9644c-93c3-4dfd-b2a8-26f9b3686d82")
    private String targetAccountId;
    @Schema(type = "double", example = "100.00")
    private Double amount;
    private CurrencyCode currency;
    private Instant createdAt;

}
