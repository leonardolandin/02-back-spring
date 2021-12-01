package com.br.back02.service;

import com.br.back02.domain.Token;
import com.br.back02.domain.User;
import com.br.back02.dto.UserLoginDTO;
import com.br.back02.dto.UserRegisterDTO;
import com.br.back02.exception.RecaptchaException;
import com.br.back02.exception.TokenException;
import com.br.back02.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;

    public Token userLogin(UserLoginDTO userLoginDTO) throws UserException, TokenException, RecaptchaException {
        return tokenService.create(userService.authorizeUser(userLoginDTO));
    }

    public User userLogged(String token) throws TokenException, UserException {
        return userService.getUserById(tokenService.decode(token));
    }

    public Token signUser(UserRegisterDTO userRegisterDTO) throws UserException, ParseException, TokenException {
        return tokenService.create(userService.create(userRegisterDTO));
    }
}
