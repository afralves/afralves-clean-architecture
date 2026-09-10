package com.afralves.cleanarchitecture.application.exceptions;

public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException() {
        super("Usuário não encontrado");
    }
}
