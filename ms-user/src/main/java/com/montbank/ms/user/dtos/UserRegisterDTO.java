package com.montbank.ms.user.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record UserRegisterDTO(@NotBlank @Valid String name,
                              @NotBlank String CPF,
                              @NotBlank @Email String email,
                              @NotBlank String password) {
}