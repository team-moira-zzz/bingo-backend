package com.moira.bingobackend.user.mapper;

import com.moira.bingobackend.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int checkEmail(String email);

    void insertUser(User user);
}
