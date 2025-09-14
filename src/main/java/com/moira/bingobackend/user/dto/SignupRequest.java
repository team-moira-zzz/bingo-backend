package com.moira.bingobackend.user.dto;

public record SignupRequest(
          String email
        , String password
        , String name
        , String nickname
        , String phone
) {
}