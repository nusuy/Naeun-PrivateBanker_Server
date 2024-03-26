package com.naeun.naeun_server.domain.User.error;

import com.naeun.naeun_server.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode {
    DEVICE_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "Device token required."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "Login failed."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    UserErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
