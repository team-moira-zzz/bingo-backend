package com.moira.bingobackend.global.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class Encryptor {
    private final AesBytesEncryptor encryptor;

    public String encrypt(String value) {
        byte[] encryptedBytes = encryptor.encrypt(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(byte[] value) {
        return new String(encryptor.decrypt(value), StandardCharsets.UTF_8);
    }
}
