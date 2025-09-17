package com.moira.bingobackend.user.controller;

import com.moira.bingobackend.user.dto.request.LoginRequest;
import com.moira.bingobackend.user.dto.request.SignupRequest;
import com.moira.bingobackend.user.dto.response.TokenResponse;
import com.moira.bingobackend.user.service.LoginService;
import com.moira.bingobackend.user.service.SignupService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final LoginService loginService;
    private final SignupService signupService;

    private String getIpAddress(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    private void putRtkInCookie(HttpServletResponse response, String rtk) {
        Cookie cookie = new Cookie("refreshToken", rtk);

        // cookie.setSecure(true);         // HTTPS 연결에서만 전송 (운영 환경에서는 주석 해제)
        cookie.setHttpOnly(true);          // JavaScript로 접근 불가능
        cookie.setPath("/");               // 모든 경로에서 쿠키 사용 가능
        cookie.setMaxAge(60 * 60 * 24);    // 1일

        response.addCookie(cookie);
    }

    @GetMapping("/signup/check/email")
    ResponseEntity<Object> checkEmail(@RequestParam String email) {
        signupService.checkEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/signup")
    ResponseEntity<Object> signup(@RequestBody SignupRequest signupRequest) {
        signupService.signup(signupRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping("/login")
    ResponseEntity<String> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        // IP 추출
        String ipAddress = this.getIpAddress(httpServletRequest);

        // 로그인 성공 후 atk, rtk 반환
        TokenResponse tokens = loginService.login(loginRequest, ipAddress);

        // rtk는 쿠키에 넣어준다.
        this.putRtkInCookie(httpServletResponse, tokens.rtk());

        // atk는 요청 본문으로 반환한다.
        return ResponseEntity.status(HttpStatus.OK).body(tokens.atk());
    }
}
