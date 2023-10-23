package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountResponseDto {

    @NotNull
    private Long id;
    @NotNull
    private BigDecimal amount;
    @Pattern(regexp = "\\d{4}", message = "must be a 4-digit number")
    private String pin;
}
