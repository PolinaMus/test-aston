package com.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private int sourceAccountId;
    private int targetAccountId;
    private BigDecimal amount;
    private String pin;
}
