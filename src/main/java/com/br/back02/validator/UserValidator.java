package com.br.back02.validator;

import com.br.back02.domain.User;
import com.br.back02.dto.UserLoginDTO;
import com.br.back02.dto.UserRegisterDTO;
import com.br.back02.exception.RecaptchaException;
import com.br.back02.exception.UserException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public void validateLogin(UserLoginDTO userLoginDTO) throws UserException, RecaptchaException {
        if(userLoginDTO.getEmail() == null || userLoginDTO.getEmail().isEmpty()
                || userLoginDTO.getPassword() == null || userLoginDTO.getPassword().isEmpty()) {
            throw new UserException("Credenciais inválidas");
        } else if(!userLoginDTO.getEmail().contains("@")) {
            throw new UserException("E-mail inválido");
        } else if(userLoginDTO.getRecaptchaToken() == null || userLoginDTO.getRecaptchaToken().isEmpty()) {
            throw new RecaptchaException("Recaptcha não existente");
        }
    }

    public void validateSign(UserRegisterDTO user) throws UserException {
        if(user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new UserException("E-mail inválido");
        } else if(user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserException("É necessário inserir uma senha");
        } else if(!user.getPassword().equals(user.getPasswordConfirmed())) {
            throw new UserException("As senhas não conferem");
        } else if(user.getPassword().length() <= 6) {
            throw new UserException("A senha precisa conter mais de 6 caracteres");
        }
    }
}
