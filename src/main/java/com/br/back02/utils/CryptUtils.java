package com.br.back02.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.security.MessageDigest.getInstance;

@Component
@RequiredArgsConstructor
public class CryptUtils {

    private final MessageDigest digest = getDigest();

    public String encrypt(String data) {
        byte[] bytes = digest.digest(data.getBytes(UTF_8));
        String hash = byteArrayToHexString(bytes);

        return hash;
    }

    private static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    private static MessageDigest getDigest() {
        try {
            return getInstance("SHA-1");
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String randomBytes(Integer size) {
        Random rd = new Random();
        byte[] arr = new byte[size];
        rd.nextBytes(arr);

        return arr.toString();
    }
}
