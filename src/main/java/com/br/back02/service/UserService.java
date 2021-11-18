package com.br.back02.service;

import com.br.back02.dto.UserLoginDTO;
import com.br.back02.exception.UserException;
import com.br.back02.repository.UserRepository;
import com.br.back02.domain.User;
import com.br.back02.utils.CryptUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authorizeUser(UserLoginDTO userLoginDTO) throws UserException {
        validate(userLoginDTO);

        Optional<User> user = userRepository.findByEmailAndPasswordAndActiveIsTrue(userLoginDTO.getEmail(),
                                                                                    new CryptUtils().encrypt(userLoginDTO.getPassword()));

        if(user.isEmpty()) {
            throw new UserException("Credenciais inválidas");
        }

        return user.get();
    }

    public void validate(UserLoginDTO userLoginDTO) throws UserException {
        if(userLoginDTO.getEmail().isEmpty() || userLoginDTO.getPassword().isEmpty()) {
            throw new UserException("kkkkkkkkkkkkkkkkk");
        } else if(!userLoginDTO.getEmail().contains("@")) {
            throw new UserException("E-mail inválido");
        }
    }
}
