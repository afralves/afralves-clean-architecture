package com.afralves.cleanarchitecture.domain.entity;

public class UserTestBuilder {

    private Long id;
    private String email = "test@example.com";
    private String password = "password123";
    private String name = "Test User";

    public static UserTestBuilder aUser() {
        return new UserTestBuilder();
    }

    public UserTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserTestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public User build() {
        return new User(id, email, password, name);
    }

}
