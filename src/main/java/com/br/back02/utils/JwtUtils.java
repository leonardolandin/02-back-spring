package com.br.back02.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.br.back02.exception.TokenException;
import org.springframework.beans.factory.annotation.Value;
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

    public Map<String, Object> verifyToken(String token) throws TokenException {
        Map<String, Object> payload = getTokenPayload(token);

        if(payload.get("email") == null || payload.get("email").toString().equals(""))
            throw new TokenException("Não foi possivel recuperar o token");

        return payload;
    }

    private Map<String, Object> getTokenPayload(String token) throws TokenExpiredException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptExpiresAt(10)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim(secret).asMap();
        } catch(TokenExpiredException e) {
            throw new TokenExpiredException("Token expirado");
        }
    }
}
