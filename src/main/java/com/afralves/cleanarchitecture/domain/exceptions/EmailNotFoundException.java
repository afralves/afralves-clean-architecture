package com.afralves.cleanarchitecture.domain.exceptions;

public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException() {
        super("E-mail não encontrado: ");
    }
}
