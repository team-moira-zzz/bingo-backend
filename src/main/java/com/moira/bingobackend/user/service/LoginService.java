package com.moira.bingobackend.user.service;

import com.moira.bingobackend.global.exception.ErrorCode;
import com.moira.bingobackend.global.exception.custom.BingoUserException;
import com.moira.bingobackend.global.utility.JwtProvider;
import com.moira.bingobackend.user.dto.request.LoginRequest;
import com.moira.bingobackend.user.dto.response.TokenResponse;
import com.moira.bingobackend.user.entity.User;
import com.moira.bingobackend.user.entity.UserLoginHistory;
import com.moira.bingobackend.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;

    public TokenResponse login(LoginRequest request, String ipAddress) {
        // 유저 조회
        String email = request.email();
        User user = userMapper.selectUserByEmail(email);

        if (user == null || !bCryptPasswordEncoder.matches(request.password(), user.getPassword())) {
            throw new BingoUserException(ErrorCode.NOT_FOUND_USER);
        }

        // 토큰 생성
        String atk = jwtProvider.createAtk(user);
        String rtk = jwtProvider.createRtk(user);

        // 로그인 기록 저장
        // TODO: 추후 userAgent와 DeviceId를 추출하는 로직 추가
        UserLoginHistory userLoginHistory = new UserLoginHistory(user, ipAddress, "", "");
        userMapper.updateUserLoginInfo(user, rtk);
        userMapper.insertUserLoginHistory(userLoginHistory);

        return new TokenResponse(atk, rtk);
    }
}
