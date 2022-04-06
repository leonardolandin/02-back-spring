package com.br.zerotwo.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
    private String recaptchaToken;
}