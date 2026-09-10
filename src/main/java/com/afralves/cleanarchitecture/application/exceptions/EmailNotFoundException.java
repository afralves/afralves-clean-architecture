package com.afralves.cleanarchitecture.application.exceptions;

public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException() {
        super("E-mail não encontrado: ");
    }
}
