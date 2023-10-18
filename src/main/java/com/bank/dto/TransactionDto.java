package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDto {

    private Long sourceAccountId;
    private Long targetAccountId;
    private BigDecimal amount;
    private String pin;
}
