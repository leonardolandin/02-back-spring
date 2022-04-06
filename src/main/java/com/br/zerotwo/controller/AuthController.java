package com.br.zerotwo.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.br.zerotwo.domain.User;
import com.br.zerotwo.dto.UserRegisterDTO;
import com.br.zerotwo.exception.EmailException;
import com.br.zerotwo.exception.RecaptchaException;
import com.br.zerotwo.exception.TokenException;
import com.br.zerotwo.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.br.zerotwo.domain.Token;
import com.br.zerotwo.dto.UserLoginDTO;
import com.br.zerotwo.exception.UserException;
import com.br.zerotwo.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final ResponseUtils responseUtils;

    @PostMapping(path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            Token token = authService.userLogin(userLoginDTO);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(responseUtils.response(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        } catch (TokenException e) {
            return new ResponseEntity<>(responseUtils.response(e.getMessage(), e), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecaptchaException e) {
            return new ResponseEntity<>(responseUtils.response(e.getMessage(), e), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path = "/user-logged",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> userLogged(@RequestBody Map<String, String> data) {
        try {
            User user = authService.userLogged(data.get("token"));
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (TokenException e) {
            return new ResponseEntity<>(responseUtils.response(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        } catch (UserException e) {
            return new ResponseEntity<>(responseUtils.response(e.getMessage(), e), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (TokenExpiredException e) {
            return new ResponseEntity<>(responseUtils.response(e.getMessage(), e), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path = "/register",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO user) {
        try {
            Token token = authService.signUser(user);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch(UserException e) {
            return new ResponseEntity<>(responseUtils.response(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        } catch (TokenException | ParseException e) {
            return new ResponseEntity<>(responseUtils.response(e.getMessage(), e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/forgot-password",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> data, HttpServletRequest request) {
        try {
            String host = request.getHeader(HttpHeaders.ORIGIN) != null ? request.getHeader(HttpHeaders.ORIGIN) :
                    InetAddress.getLocalHost().getHostAddress();
            authService.forgotPassword(data.get("email"), host);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(EmailException | UnknownHostException e) {
            return new ResponseEntity<>(responseUtils.response(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }
}
