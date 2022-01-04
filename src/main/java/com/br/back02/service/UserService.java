package com.br.back02.service;

import com.br.back02.dto.UserLoginDTO;
import com.br.back02.dto.UserRegisterDTO;
import com.br.back02.exception.RecaptchaException;
import com.br.back02.exception.UserException;
import com.br.back02.repository.UserRepository;
import com.br.back02.domain.User;
import com.br.back02.utils.CryptUtils;
import com.br.back02.utils.DateUtils;
import com.br.back02.validator.RecaptchaValidator;
import com.br.back02.validator.UserValidator;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RecaptchaValidator recaptchaValidator;
    private final UserValidator userValidator;

    public UserService(UserRepository userRepository,
                       RecaptchaValidator recaptchaValidator,
                       UserValidator userValidator) {
        this.userRepository = userRepository;
        this.recaptchaValidator = recaptchaValidator;
        this.userValidator = userValidator;
    }

    public User authorizeUser(UserLoginDTO userLoginDTO) throws UserException, RecaptchaException {
        userValidator.validateLogin(userLoginDTO);

        if(!recaptchaValidator.validate(userLoginDTO.getRecaptchaToken())) {
            throw new RecaptchaException("Recaptcha inválido");
        }

        Optional<User> user = userRepository.findByEmailAndPasswordAndActiveIsTrue
                (userLoginDTO.getEmail(), new CryptUtils().encrypt(userLoginDTO.getPassword()));

        if(user.isEmpty()) {
            throw new UserException("Credenciais inválidas");
        }

        return user.get();
    }

    public User getUserById(String id) throws UserException {
        if(id == null || id.equals(""))
            throw new UserException("Ocorreu um erro ao resgatar usuário");

        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty())
            throw new UserException("Usuário não encontrado");

        return user.get();
    }

    public User create(UserRegisterDTO userDTO) throws UserException, ParseException {
        try {
            userValidator.validateSign(userDTO);

            if(!recaptchaValidator.validate(userDTO.getRecaptchaToken())) {
                throw new RecaptchaException("Recaptcha inválido");
            }

            Optional<User> emailExist = userRepository.findByEmailAndActiveIsTrue(userDTO.getEmail());

            if(emailExist.isPresent())
                throw new UserException("Usuário já cadastrado");

            User user = User.builder()
                    .email(userDTO.getEmail()).name(userDTO.getName())
                    .password(new CryptUtils().encrypt(userDTO.getPassword()))
                    .active(true).completeRegister(false)
                    .created(new DateUtils().format(new Date()))
                    .build();

            user = userRepository.insert(user);

            if(user.getId() == null || user.getId().isEmpty())
                throw new UserException("Ocorreu um erro ao criar novo usuário");

            return user;
        } catch(UserException | RecaptchaException e) {
            throw new UserException(e.getMessage());
        }
    }

    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmailAndActiveIsTrue(email);

        if(user.isPresent())
            return user.get();

        return null;
    }

}
