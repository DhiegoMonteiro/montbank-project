package com.montbank.ms.transaction.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record TransactionUpdateDTO(@NotBlank @Valid String title) {
}
