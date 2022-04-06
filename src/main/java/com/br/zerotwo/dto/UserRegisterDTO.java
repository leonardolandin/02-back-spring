package com.br.zerotwo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserRegisterDTO {
    private String email;
    private String password;
    private String passwordConfirmed;
    private String name;
    private String recaptchaToken;
}
