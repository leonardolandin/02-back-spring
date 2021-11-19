package com.br.back02.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
    private String recaptchaToken;
}