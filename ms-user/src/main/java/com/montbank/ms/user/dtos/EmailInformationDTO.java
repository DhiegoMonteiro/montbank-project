package com.montbank.ms.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter @Setter
public class EmailInformationDTO implements Serializable {

    private String email;
    private String name;
}
