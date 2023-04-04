package com.example.mybank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegisterDTO {
    @Schema(type = "double", example = "5000.00")
    private String balance;
    @Schema(type = "string", example = "EUR")
    private String currency;
}
