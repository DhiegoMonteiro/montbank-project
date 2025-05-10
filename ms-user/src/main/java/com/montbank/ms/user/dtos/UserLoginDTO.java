package com.montbank.ms.user.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(@NotBlank @Valid @Email String email,
                           @NotBlank @Valid String password) {
}
