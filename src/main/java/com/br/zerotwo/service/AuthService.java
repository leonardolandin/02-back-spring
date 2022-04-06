package com.br.zerotwo.service;

import com.br.zerotwo.domain.Token;
import com.br.zerotwo.domain.TransactionRemember;
import com.br.zerotwo.domain.User;
import com.br.zerotwo.dto.UserLoginDTO;
import com.br.zerotwo.dto.UserRegisterDTO;
import com.br.zerotwo.exception.EmailException;
import com.br.zerotwo.exception.RecaptchaException;
import com.br.zerotwo.exception.TokenException;
import com.br.zerotwo.exception.UserException;
import com.br.zerotwo.repository.RememberRepository;
import com.br.zerotwo.utils.CryptUtils;
import com.br.zerotwo.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final RememberRepository rememberRepository;
    private final CryptUtils cryptUtils;
    private final EmailUtils emailUtils;

    public Token userLogin(UserLoginDTO userLoginDTO) throws UserException, TokenException, RecaptchaException {
        return tokenService.create(userService.authorizeUser(userLoginDTO));
    }

    public User userLogged(String token) throws TokenException, UserException {
        return userService.getUserById(tokenService.decode(token));
    }

    public Token signUser(UserRegisterDTO userRegisterDTO) throws UserException, ParseException, TokenException {
        return tokenService.create(userService.create(userRegisterDTO));
    }

    public void forgotPassword(String email, String origin) throws EmailException {
        validateForgot(email);

        User user = userService.getUserByEmail(email);

        if(user != null) {
            String token = cryptUtils.randomBytes(16);
            Date date = new Date();
            date.setTime(date.getTime() + 1200000);

            TransactionRemember transactionRemember = TransactionRemember.builder()
                    .token(token).user(user.getId()).expire(date).active(true)
                    .build();

            transactionRemember = rememberRepository.insert(transactionRemember);

            if(transactionRemember != null) {
                emailUtils.sendEmail(emailUtils.getSession(), email, "Recuperação de senha - 02",
                        "Acesse para recuperar a sua senha: " + origin + "/recuperar-senha/" + token);
            }
        }
    }

    public void validateForgot(String email) throws EmailException {
        if(email == null || !email.contains("@"))
            throw new EmailException("E-mail inválido");
    }
}
