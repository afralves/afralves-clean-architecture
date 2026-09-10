package com.afralves.cleanarchitecture.application.exceptions;

public class EmailNotFoundException extends ApplicationException {

    public EmailNotFoundException() {
        super("E-mail não encontrado: ");
    }
}
