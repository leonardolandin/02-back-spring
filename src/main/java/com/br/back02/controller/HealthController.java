package com.br.back02.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping(path = "/health",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> health() {
        return new ResponseEntity<>("{\"status\": \"ok\"}", HttpStatus.OK);
    }
}
