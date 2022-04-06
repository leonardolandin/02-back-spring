package com.br.zerotwo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Token {
    private String token;
    private String idUser;

    public Token(String token, String idUser) {
        this.token = token;
        this.idUser = idUser;
    }
}
