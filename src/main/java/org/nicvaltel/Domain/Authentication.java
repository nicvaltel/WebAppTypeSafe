package org.nicvaltel.Domain;


import org.nicvaltel.Common.Empty;
import org.nicvaltel.Domain.Types.*;
import software.amazon.awssdk.utils.Either;

import java.util.List;
import java.util.Optional;

public interface Authentication {
    // Either<A,Empty> instead of Optional<A> indicates the success of operation
    // Void doesn't work because of Either.right(Void) behaves as Either.right().isPresent = false

    Either<List<String>, Email> mkEmail(String inputStr);

    public String rawEmail(Email email);

    String rawPassword(Password password);

    Either<List<String>, Password> mkPassword(String passwordStr);

    Either<RegistrationError, Empty> register(Auth auth);

    Either<EmailVerificationError, Empty> verifyEmail(VerificationCode vCode);

    Optional<Email> getUser(UserId userId);

    Either<LoginError, SessionId> login(Auth auth);

    Optional<UserId> resolveSessionId(SessionId sessionId);
}
