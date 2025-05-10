package com.montbank.ms.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDTO(@NotNull UUID sender,
                             @NotNull BigDecimal amount,
                             @NotNull UUID receiver) {
}
