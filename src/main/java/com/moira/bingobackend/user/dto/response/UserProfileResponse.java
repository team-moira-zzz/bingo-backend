package com.moira.bingobackend.user.dto.response;

import java.time.LocalDateTime;

public record UserProfileResponse(
        String userId,
        String email,
        String name,
        String nickname,
        String phone,
        String role,
        String status,
        LocalDateTime createdAt
) {
}
