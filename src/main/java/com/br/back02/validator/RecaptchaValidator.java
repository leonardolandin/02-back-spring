package com.br.back02.validator;

import com.br.back02.exception.RecaptchaException;
import com.br.back02.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class RecaptchaValidator {

    @Value("${zerotwo.recaptcha.secret}")
    private String secret;

    private final RequestUtils requestUtils;

    private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";

    public RecaptchaValidator(RequestUtils requestUtils) {
        this.requestUtils = requestUtils;
    }


    public boolean validate(String token) throws RecaptchaException {
        if(tokenValidate(token))
            return false;

        try {
            String userAgent = getRequest().getHeader("User-Agent");
            String ip = getRequest().getRemoteAddr();

            JSONObject request = requestRecaptcha(token, userAgent, ip);

            return request.getBoolean("success");
        } catch (Exception e) {
            throw new RecaptchaException("Ocorreu um erro ao validar o recaptcha");
        }
    }

    private boolean tokenValidate(String token) {
        return token == null || "".equals(token);
    }

    private JSONObject requestRecaptcha(String recaptchaFrontendToken, String userAgent, String ip)
            throws IOException, JSONException {

        String postParams = "secret=" + secret + "&response="
                + recaptchaFrontendToken + "&remoteip=" + ip;

        JSONObject myObject = requestUtils.POST(RECAPTCHA_URL, postParams, userAgent);

        return myObject;
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes != null ? requestAttributes.getRequest() : null;
    }
}
