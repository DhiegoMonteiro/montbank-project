package com.montbank.ms.transaction.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequestDTO(@NotNull @Valid BigDecimal amount,
                                    @NotNull @Valid UUID receiver,
                                    @NotBlank @Valid String title) {
}
