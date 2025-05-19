package com.montbank.ms.user.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserInformationUpdateDTO(@NotBlank @Valid String name,
                                       @NotBlank @Valid String CPF,
                                       @NotBlank @Email String email) {
}
