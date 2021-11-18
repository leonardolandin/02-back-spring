package com.br.back02.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.br.back02.exception.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${02.jwt.secret}")
    private String secret;

    @Value("${02.jwt.expires}")
    private Long expires;

    public String generateToken(Map<String, Object> data) throws TokenException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withClaim(secret, data)
                    .withExpiresAt(new Date(System.currentTimeMillis() + expires))
                    .sign(algorithm);
        } catch(JWTCreationException e) {
               throw new TokenException(e.getMessage());
        }
    }
}
