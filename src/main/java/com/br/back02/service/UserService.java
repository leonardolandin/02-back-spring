package com.br.back02.service;

import com.br.back02.dto.UserLoginDTO;
import com.br.back02.exception.RecaptchaException;
import com.br.back02.exception.UserException;
import com.br.back02.repository.UserRepository;
import com.br.back02.domain.User;
import com.br.back02.utils.CryptUtils;
import com.br.back02.validator.RecaptchaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RecaptchaValidator recaptchaValidator;

    public UserService(UserRepository userRepository, RecaptchaValidator recaptchaValidator) {
        this.userRepository = userRepository;
        this.recaptchaValidator = recaptchaValidator;
    }

    public User authorizeUser(UserLoginDTO userLoginDTO) throws UserException, RecaptchaException {
        validate(userLoginDTO);

        Optional<User> user = userRepository.findByEmailAndPasswordAndActiveIsTrue
                (userLoginDTO.getEmail(), new CryptUtils().encrypt(userLoginDTO.getPassword()));

        if(user.isEmpty()) {
            throw new UserException("Credenciais inválidas");
        }

        return user.get();
    }

    public void validate(UserLoginDTO userLoginDTO) throws UserException, RecaptchaException {
        if(userLoginDTO.getEmail() == null || userLoginDTO.getEmail().isEmpty()
                || userLoginDTO.getPassword() == null || userLoginDTO.getPassword().isEmpty()) {
            throw new UserException("Credenciais inválidas");
        } else if(!userLoginDTO.getEmail().contains("@")) {
            throw new UserException("E-mail inválido");
        } else if(userLoginDTO.getRecaptchaToken() == null || userLoginDTO.getRecaptchaToken().isEmpty()) {
            throw new RecaptchaException("Recaptcha não existente");
        }

        if(!recaptchaValidator.validate(userLoginDTO.getRecaptchaToken())) {
            throw new RecaptchaException("Recaptcha inválido");
        }
    }

    public User getUserById(String id) throws UserException {
        if(id == null || id.equals(""))
            throw new UserException("Ocorreu um erro ao resgatar usuário");

        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty())
            throw new UserException("Usuário não encontrado");

        return user.get();
    }
}
