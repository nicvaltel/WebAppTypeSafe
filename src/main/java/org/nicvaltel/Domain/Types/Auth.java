package org.nicvaltel.Domain.Types;

public class Auth {
    private final Email authEmail;
    private final Password authPassword;

    public Auth(Email authEmail, Password authPassword) {
        this.authEmail = authEmail;
        this.authPassword = authPassword;
    }

    public Email getAuthEmail() {
        return authEmail;
    }

    public Password getAuthPassword() {
        return authPassword;
    }
}
