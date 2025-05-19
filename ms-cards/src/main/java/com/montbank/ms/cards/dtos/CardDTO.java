package com.montbank.ms.cards.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record CardDTO(@NotBlank @Valid String cardName,
                      @NotBlank @Valid String type) {
}
