package com.br.zerotwo.service;

import com.br.zerotwo.dto.JwtResponseDTO;
import com.br.zerotwo.exception.TokenException;
import com.br.zerotwo.utils.JwtUtils;
import com.br.zerotwo.domain.Token;
import com.br.zerotwo.domain.User;
import lombok.RequiredArgsConstructor;
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

    public String decode(String token) throws TokenException {
        Map<String, Object> jwt = jwtUtils.verifyToken(token);

        return jwt.get("id").toString();
    }
}
