package com.moira.bingobackend.user.mapper;

import com.moira.bingobackend.user.entity.User;
import com.moira.bingobackend.user.entity.UserLoginHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    // 회원가입
    int checkEmail(String email);

    int checkNickname(String nickname);

    void insertUser(User user);

    // 로그인
    User selectUserByEmail(String email);

    void updateUserLoginInfo(@Param("user") User user, @Param("rtk") String rtk);

    void insertUserLoginHistory(UserLoginHistory userLoginHistory);

    // 로그아웃
    void updateUserLogoutInfo(String userId);
}
