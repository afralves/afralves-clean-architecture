package com.afralves.cleanarchitecture.infrastructure.adapter.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    DOMAIN_VALIDATION(HttpStatus.BAD_REQUEST),
    DOMAIN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus status() {
        return status;
    }

}
