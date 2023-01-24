package org.nicvaltel.Domain;


import org.nicvaltel.Domain.Types.*;
import software.amazon.awssdk.utils.Either;

import java.util.List;
import java.util.Optional;

public interface Authentication {
    Either<List<String>, Email> mkEmail(String inputStr);

    public String rawEmail(Email email);

    String rawPassword(Password password);

    Either<List<String>, Password> mkPassword(String passwordStr);

    Either<RegistrationError, Void> register(Auth auth);

    Either<EmailVerificationError, Void> verifyEmail(VerificationCode vCode);

    Optional<Email> getUser(UserId userId);

    Either<LoginError, SessionId> login(Auth auth);

    Optional<UserId> resolveSessionId(SessionId sessionId);
}
