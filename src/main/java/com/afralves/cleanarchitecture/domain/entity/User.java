package com.afralves.cleanarchitecture.domain.entity;

public class User {

    private final Long id;
    private final String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this(null, email, password, name);
    }

    public User(Long id, String email, String password, String name) {
        validate(email, password, name);
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
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Nova senha deve ter no mínimo 6 caracteres.");
        }
        this.password = newPassword;
    }

    public void changeName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.name = newName;
    }

    private void validate(String email, String password, String name) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser vazio.");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
    }

}
