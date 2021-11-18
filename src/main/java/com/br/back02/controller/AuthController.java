package com.br.back02.controller;

import com.br.back02.exception.TokenException;
import com.br.back02.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.back02.domain.Token;
import com.br.back02.dto.UserLoginDTO;
import com.br.back02.exception.UserException;
import com.br.back02.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            Token token = authService.userLogin(userLoginDTO);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch(UserException e) {
            return new ResponseEntity<>(new ResponseUtils().response(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        } catch(TokenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
