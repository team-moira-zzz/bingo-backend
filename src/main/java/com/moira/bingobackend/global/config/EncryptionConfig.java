package com.moira.bingobackend.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;

@Configuration
public class EncryptionConfig {
    @Value("${encryptor.symmetric}")
    private String symmetric;

    @Value("${encryptor.salt}")
    private String salt;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AesBytesEncryptor aesBytesEncryptor() {
        return new AesBytesEncryptor(symmetric, salt);
    }
}
