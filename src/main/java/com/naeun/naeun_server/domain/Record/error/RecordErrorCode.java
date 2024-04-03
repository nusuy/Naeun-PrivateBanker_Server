package com.naeun.naeun_server.domain.Record.error;

import com.naeun.naeun_server.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RecordErrorCode implements ErrorCode {
    RECORD_ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN, "Record access failed."),
    RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "Record not found."),
    FAST_API_REQ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Fast API request failed."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "[GCS] File upload failed.");

    private final HttpStatus httpStatus;
    private final String message;

    RecordErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
