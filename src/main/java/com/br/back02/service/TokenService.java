package com.br.back02.service;

import com.br.back02.dto.JwtResponseDTO;
import com.br.back02.exception.TokenException;
import com.br.back02.utils.JwtUtils;
import com.br.back02.domain.Token;
import com.br.back02.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtils jwtUtils;


    public Token create(User user) throws TokenException {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("email", user.getEmail());

            JwtResponseDTO tokenJwt = new JwtResponseDTO(jwtUtils.generateToken(data));

            return new Token(tokenJwt.getToken(), user.getId());
        } catch(TokenException e) {
            throw new TokenException(e.getMessage());
        }
    }
}
