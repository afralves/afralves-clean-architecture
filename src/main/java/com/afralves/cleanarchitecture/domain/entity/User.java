package com.afralves.cleanarchitecture.domain.entity;

import com.afralves.cleanarchitecture.domain.exception.DomainValidationException;

public class User {

    private final Long id;
    private final String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this(null, email, password, name);
    }

    public User(Long id, String email, String password, String name) {
        validateEmail(email);
        validatePassword(password);
        validateName(name);
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void changePassword(String newPassword) {
        validatePassword(newPassword);
        this.password = newPassword;
    }

    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new DomainValidationException("Email não pode ser vazio.");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new DomainValidationException("Senha deve ter no mínimo 6 caracteres.");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("Nome não pode ser vazio.");
        }
    }

}
