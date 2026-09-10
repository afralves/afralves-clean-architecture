package com.afralves.cleanarchitecture.infrastructure.adapter.exception;

import com.afralves.cleanarchitecture.application.exceptions.EmailAlreadyExistsException;
import com.afralves.cleanarchitecture.application.exceptions.EmailNotFoundException;
import com.afralves.cleanarchitecture.application.exceptions.UserNotFoundException;
import com.afralves.cleanarchitecture.domain.exception.DomainException;
import com.afralves.cleanarchitecture.domain.exception.DomainValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponse> handleDomainValidation(DomainValidationException exception) {
        return build(ErrorCode.DOMAIN_VALIDATION, exception.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException exception) {
        log.error("Unhandled domain exception", exception);
        return build(ErrorCode.DOMAIN_ERROR, "Erro interno de domínio.");
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailConflict(EmailAlreadyExistsException exception) {
        return build(ErrorCode.EMAIL_ALREADY_EXISTS, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException exception) {
        return build(ErrorCode.USER_NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotFound(EmailNotFoundException exception) {
        return build(ErrorCode.EMAIL_NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        log.error("Unhandled exception", exception);
        return build(ErrorCode.INTERNAL_ERROR, "Ocorreu um erro inesperado.");
    }

    private ResponseEntity<ErrorResponse> build(ErrorCode code, String message) {
        return ResponseEntity.status(code.status())
                .body(new ErrorResponse(Instant.now(), code.status().value(), code.name(), message));
    }

}
