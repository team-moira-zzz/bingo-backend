package com.moira.bingobackend.user.dto.request;

public record LoginRequest(
          String email
        , String password
) {
}