package com.example.study.security.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginDto {

    @NotBlank @Email
    private String email;

    @NotBlank
    private String password;
}
