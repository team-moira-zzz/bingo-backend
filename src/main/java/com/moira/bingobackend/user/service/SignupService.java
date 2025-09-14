package com.moira.bingobackend.user.service;

import com.moira.bingobackend.global.utility.Encryptor;
import com.moira.bingobackend.user.dto.SignupRequest;
import com.moira.bingobackend.user.entity.User;
import com.moira.bingobackend.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class SignupService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Encryptor encryptor;
    private final UserMapper userMapper;

    // 이메일 정규 표현식
    private static final Pattern EMAIL_PATTERN
            = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    // 비밀번호 정규 표현식 (8-24자, 대소문자, 숫자, 특수문자 각 1개 이상)
    private static final Pattern PASSWORD_PATTERN
            = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,24}$");
    // 전화번호 정규 표현식 (010으로 시작하고 11자리)
    private static final Pattern PHONE_PATTERN
            = Pattern.compile("^010\\d{8}$");

    private void validate(SignupRequest request) {
        if (!StringUtils.hasText(request.email())) {
            throw new IllegalArgumentException("이메일은 필수 입력 항목입니다.");
        }
        if (!StringUtils.hasText(request.password())) {
            throw new IllegalArgumentException("비밀번호는 필수 입력 항목입니다.");
        }
        if (!StringUtils.hasText(request.name())) {
            throw new IllegalArgumentException("이름은 필수 입력 항목입니다.");
        }
        if (!StringUtils.hasText(request.nickname())) {
            throw new IllegalArgumentException("닉네임은 필수 입력 항목입니다.");
        }
        if (!StringUtils.hasText(request.phone())) {
            throw new IllegalArgumentException("전화번호는 필수 입력 항목입니다.");
        }

        // 1. 이메일 형식 체크
        if (!EMAIL_PATTERN.matcher(request.email()).matches()) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        // 2. 비밀번호 형식 체크
        if (!PASSWORD_PATTERN.matcher(request.password()).matches()) {
            throw new IllegalArgumentException("비밀번호는 8~24자이며, 대소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.");
        }

        // 3. 전화번호 형식 체크
        if (!PHONE_PATTERN.matcher(request.phone()).matches()) {
            throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다. (예: 01012345678)");
        }
    }

    @Transactional(readOnly = true)
    public void checkEmail(String email) {
        // 이메일 존재 여부 확인
        int result = userMapper.checkEmail(email);

        if (result > 0) {
            throw new RuntimeException("이미 사용중인 이메일입니다."); // TODO
        }
    }

    @Transactional
    public void signup(SignupRequest request) {
        // 유효성 검사
        validate(request);

        // DTO -> 엔티티 변환
        User user = new User(request, bCryptPasswordEncoder, encryptor);

        // DB 저장
        userMapper.insertUser(user);
    }
}
