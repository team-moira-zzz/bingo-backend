package com.moira.bingobackend.user.service;

import com.moira.bingobackend.global.auth.SimpleUserAuth;
import com.moira.bingobackend.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LogoutService {
    private final UserMapper userMapper;

    @Transactional
    public void logout(SimpleUserAuth simpleUserAuth) {
        String userId = simpleUserAuth.userId();

        // rtk 삭제
        userMapper.updateUserLogoutInfo(userId);
    }
}
