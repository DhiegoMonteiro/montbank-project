package com.montbank.ms.user.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UserInformationDTO(@NotBlank @Valid String name,
                                 @NotBlank @Valid String CPF,
                                 @NotBlank @Email String email,
                                 @NotNull @Valid BigDecimal balance ) {
}
