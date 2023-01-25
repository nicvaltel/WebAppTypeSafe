package org.nicvaltel.Domain.Types;

public class Email {
    private final String email;

    public Email(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Email {" + email + "}";
    }
}
