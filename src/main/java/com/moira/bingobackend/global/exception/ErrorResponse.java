package com.moira.bingobackend.global.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
          String message
        , ErrorCode errorCode
        , HttpStatus status
        , LocalDateTime time
) {
}
