package com.moira.bingobackend.global.exception.custom;

import com.moira.bingobackend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BingoUserException extends RuntimeException {
    private final ErrorCode errorCode;

    public BingoUserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
