package com.naeun.naeun_server.domain.Record.error;

import com.naeun.naeun_server.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RecordErrorCode implements ErrorCode {
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "[GCS] File upload failed.");

    private final HttpStatus httpStatus;
    private final String message;

    RecordErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
