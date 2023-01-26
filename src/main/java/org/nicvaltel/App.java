package org.nicvaltel;

import org.javatuples.Pair;
import org.nicvaltel.Adapter.InMemory.AuthInMemory;
import org.nicvaltel.Common.Empty;
import org.nicvaltel.Domain.AuthRepo;
import org.nicvaltel.Domain.EmailVerificationNotif;
import org.nicvaltel.Domain.SessionRepo;
import org.nicvaltel.Domain.Types.*;
import software.amazon.awssdk.utils.Either;

import java.util.Optional;

import static org.nicvaltel.Common.Common.println;

public class App implements AuthRepo, EmailVerificationNotif, SessionRepo {
    final static AuthInMemory M = AuthInMemory.INSTANCE;

    @Override
    public Either<RegistrationError, VerificationCode> addAuth(Auth auth) {
        return M.addAuth(auth);
    }

    @Override
    public Either<EmailVerificationError, Empty> setEmailAsVerified(VerificationCode vCode) {
        return M.setEmailAsVerified(vCode);
    }

    @Override
    public Optional<Pair<UserId, Boolean>> findUserByAuth(Auth auth) {
        return M.findUserByAuth(auth);
    }

    @Override
    public Optional<Email> findEmailFromUserId(UserId userId) {
        return M.findEmailFromUserId(userId);
    }

    @Override
    public Empty notifyEmailVerification(Email email, VerificationCode verificationCode) {
        return M.notifyEmailVerification(email, verificationCode);
    }

    @Override
    public SessionId newSession(UserId userId) {
        return M.newSession(userId);
    }

    @Override
    public Optional<UserId> findUserIdBySessionId(SessionId sessionId) {
        return M.findUserIdBySessionId(sessionId);
    }

    public void action() {
        Email email = M.mkEmail("example@test.org").right().get();
        Password pass = M.mkPassword("1234ABCDefgh").right().get();
        Auth auth = new Auth(email, pass);
        M.register(auth);
        VerificationCode vCode = M.getNotificationsForEmail(email).get();
        M.verifyEmail(vCode);
        SessionId sessionId = M.login(auth).right().get();
        UserId userId = M.resolveSessionId(sessionId).get();
        Email registeredEmail = M.getUser(userId).get();
        println(sessionId);
        println(userId);
        println(registeredEmail);
    }
}
