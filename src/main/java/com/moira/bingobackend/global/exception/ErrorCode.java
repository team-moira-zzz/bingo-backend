package com.moira.bingobackend.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 유저 관련 에러코드
      NO_EMAIL("U001", "이메일은 필수 입력 항목입니다.")
    , NO_PASSWORD("U002", "비밀번호는 필수 입력 항목입니다.")
    , NO_NAME("U003", "이름은 필수 입력 항목입니다.")
    , NO_NICKNAME("U004", "닉네임은 필수 입력 항목입니다.")
    , NO_PHONE("U005", "휴대폰번호는 필수 입력 항목입니다.")
    , INVALID_EMAIL("U006", "이메일 형식이 올바르지 않습니다.")
    , INVALID_PASSWORD("U007", "비밀번호는 8~24자이며, 대소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.")
    , INVALID_PHONE("U008", "전화번호 형식이 올바르지 않습니다. (예: 01012345678)")
    , USING_EMAIL("U009", "이미 사용중인 이메일입니다.")
    , USING_NICKNAME("U010", "이미 사용중인 닉네임입니다.")
    , NOT_FOUND_USER("U011", "올바르지 않은 이메일 혹은 비밀번호입니다.")

    // 권한 관련 에러코드
    , INVALID_AUTHORIZATION_HEADER("A001", "Authorization 헤더에 토큰 정보가 포함되어 있지 않습니다.")
    , EXPIRED_ATK("A002", "Access Token이 만료되었습니다.")
    , INVALID_TOKEN("A003", "유효하지 않은 토큰입니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
