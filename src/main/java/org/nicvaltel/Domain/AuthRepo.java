package org.nicvaltel.Domain;

import org.javatuples.Pair;
import org.nicvaltel.Common.Empty;
import org.nicvaltel.Domain.Types.*;
import software.amazon.awssdk.utils.Either;

import java.util.Optional;

public interface AuthRepo {
    Either<RegistrationError, VerificationCode> addAuth(Auth auth);

    Either<EmailVerificationError, Empty> setEmailAsVerified(VerificationCode vCode);

    Optional<Pair<UserId, Boolean>> findUserByAuth(Auth auth);

    Optional<Email> findEmailFromUserId(UserId userId);
}
