package com.moira.bingobackend.user.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserLoginHistory {
    private String id;
    private String userId;
    private String ipAddress;
    private String userAgent;
    private String deviceId;
    private LocalDateTime loginAt;

    public UserLoginHistory(User user, String ipAddress, String userAgent, String deviceId) {
        this.id = UUID.randomUUID().toString();
        this.userId = user.getId();
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.deviceId = deviceId;
        this.loginAt = LocalDateTime.now();
    }
}
