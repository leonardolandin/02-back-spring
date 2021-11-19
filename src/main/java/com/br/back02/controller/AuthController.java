package com.br.back02.controller;

import com.br.back02.domain.User;
import com.br.back02.exception.RecaptchaException;
import com.br.back02.exception.TokenException;
import com.br.back02.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.br.back02.domain.Token;
import com.br.back02.dto.UserLoginDTO;
import com.br.back02.exception.UserException;
import com.br.back02.service.AuthService;

import java.util.Map;

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
            return new ResponseEntity<>(new ResponseUtils().response(e.getMessage(), e), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecaptchaException e) {
            return new ResponseEntity<>(new ResponseUtils().response(e.getMessage(), e), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path = "/userLogged",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> userLogged(@RequestBody Map<String, String> data) {
        try {
            User user = authService.userLogged(data.get("token"));
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch(TokenException e) {
            return new ResponseEntity<>(new ResponseUtils().response(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        } catch(UserException e) {
            return new ResponseEntity<>(new ResponseUtils().response(e.getMessage(), e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
