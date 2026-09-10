package com.afralves.cleanarchitecture.application.exceptions;

public class EmailAlreadyExistsException extends ApplicationException {

    public EmailAlreadyExistsException() {
        super("E-mail já cadastrado");
    }
}
