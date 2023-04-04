package com.example.mybank.dto;


import com.example.mybank.entity.Transaction;
import com.example.mybank.enums.CurrencyCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO {
    @Schema(type = "string", example = "a0d0a065-8706-4501-81b2-fa71be9957ef")
    private String id;
    @Schema(type = "double", example = "5000.00")
    private Double balance;
    private CurrencyCode currency;
    private Instant createdAt;

}
