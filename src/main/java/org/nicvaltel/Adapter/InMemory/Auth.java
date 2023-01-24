package org.nicvaltel.Adapter.InMemory;

import org.javatuples.Pair;
import org.nicvaltel.Common.RWS;
import org.nicvaltel.Domain.*;
import org.nicvaltel.Domain.Types.*;
import software.amazon.awssdk.utils.Either;

import java.util.Optional;

public class Auth extends AuthenticationAbstract {
    @Override
    public Either<RegistrationError, VerificationCode> addAuth(org.nicvaltel.Domain.Types.Auth auth) {
        throw new Error("not implemented");
    }

    @Override
    public Either<EmailVerificationError, Void> setEmailAsVerified(VerificationCode vCode) {
        throw new Error("not implemented");
    }

    @Override
    public Optional<Pair<UserId, Boolean>> findUserByAuth(org.nicvaltel.Domain.Types.Auth auth) {
        throw new Error("not implemented");
    }

    @Override
    public Optional<Email> findEmailFromUserId(UserId userId) {
        throw new Error("not implemented");
    }

    @Override
    public SessionId newSession(UserId userId) {
        throw new Error("not implemented");
    }

    @Override
    public Optional<UserId> findUserIdBySessionId(SessionId sessionId) {
        throw new Error("not implemented");
    }

    @Override
    public Void notifyEmailVerification(Email email, VerificationCode verificationCode) {
        throw new Error("not implemented");
    }

}
