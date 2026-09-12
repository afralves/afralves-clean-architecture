package com.afralves.cleanarchitecture.application.exceptions;

public class EmailAlreadyExistsException extends ApplicationException {

    public EmailAlreadyExistsException() {
        super("Email already registered.");
    }
}
