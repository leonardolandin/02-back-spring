package com.br.back02.service;

import com.br.back02.domain.Token;
import com.br.back02.domain.User;
import com.br.back02.dto.UserLoginDTO;
import com.br.back02.exception.TokenException;
import com.br.back02.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserService userService;

    private final TokenService tokenService;

    public Token userLogin(UserLoginDTO userLoginDTO) throws UserException, TokenException {
        User user = userService.authorizeUser(userLoginDTO);
        Token token = tokenService.create(user);
        return token;
    }
}
