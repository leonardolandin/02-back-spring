package com.br.zerotwo.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);

    public Object response(String message, Exception e) {
        Map<String, String> response = new HashMap<>();

        response.put("message", message);
        log.info(message, e);

        return response;
    }
}
