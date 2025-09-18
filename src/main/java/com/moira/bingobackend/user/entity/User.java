package com.moira.bingobackend.user.entity;

import com.moira.bingobackend.global.utility.Encryptor;
import com.moira.bingobackend.user.dto.request.SignupRequest;
import com.moira.bingobackend.user.dto.response.UserProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class User {
    private String id;
    private UserRole role;
    private UserStatus status;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String rtk;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(SignupRequest request, BCryptPasswordEncoder bCryptPasswordEncoder, Encryptor encryptor) {
        this.id = UUID.randomUUID().toString();
        this.role = UserRole.USER;
        this.status = UserStatus.ACTIVE;
        this.email = request.email();
        this.password = bCryptPasswordEncoder.encode(request.password());
        this.name = request.name();
        this.nickname = request.nickname();
        this.phone = encryptor.encrypt(request.phone());
        this.rtk = null;
        this.lastLoginAt = null;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UserProfileResponse toUserProfileResponse(String decryptedPhone) {
        return new UserProfileResponse(
                this.id,
                this.email,
                this.name,
                this.nickname,
                decryptedPhone,
                this.role.name(),
                this.status.name(),
                this.createdAt
        );
    }
}
