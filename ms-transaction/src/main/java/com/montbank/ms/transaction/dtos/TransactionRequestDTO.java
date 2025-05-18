package com.montbank.ms.transaction.dtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequestDTO(@NotNull BigDecimal amount,
                                    @NotNull UUID receiver) {
}
