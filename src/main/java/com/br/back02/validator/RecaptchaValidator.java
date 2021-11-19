package com.br.back02.validator;

import com.br.back02.exception.RecaptchaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Component
public class RecaptchaValidator {

    @Value("${02.recaptcha.secret}")
    private String secret;

    private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";


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
        java.net.URL obj = new URL(RECAPTCHA_URL);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", userAgent);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String postParams = "secret=" + secret + "&response="
                + recaptchaFrontendToken + "&remoteip=" + ip;

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;

        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        JSONObject myObject = new JSONObject(response.toString());

        return myObject;
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes != null ? requestAttributes.getRequest() : null;
    }
}
