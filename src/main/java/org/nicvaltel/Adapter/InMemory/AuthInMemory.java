package org.nicvaltel.Adapter.InMemory;

import com.mifmif.common.regex.Generex;
import org.javatuples.Pair;
import org.multiverse.api.StmUtils;
import org.nicvaltel.Common.Empty;
import org.nicvaltel.Domain.*;
import org.nicvaltel.Domain.Types.*;
import software.amazon.awssdk.utils.Either;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.nicvaltel.Domain.Types.EmailVerificationError.*;
import static org.nicvaltel.Domain.Types.RegistrationError.*;

public class AuthInMemory extends AuthenticationAbstract {

    public static final AuthInMemory INSTANCE = new AuthInMemory();

    private State state;

    private Generex generexPattern = new Generex("[A-Za-z0-9]{16}");

    private AuthInMemory() {
        state = State.initialState();
    }


    @Override
    public Either<RegistrationError, VerificationCode> addAuth(Auth auth) {
        Either<RegistrationError, VerificationCode> result = StmUtils.atomic(() -> {
            List<Pair<UserId, Auth>> auths = state.getAuth();
            Email email = auth.getAuthEmail();
            boolean isDuplicate = auths.stream()
                    .anyMatch(pair -> email.equals(pair.getValue1().getAuthEmail()));

            return isDuplicate ?
                    Either.left(RegistrationErrorEmailTaken) :
                    Either.right(makeVerificationCode(auths, auth, email));
        });

        return result;
    }

    private VerificationCode makeVerificationCode(List<Pair<UserId, Auth>> auths, Auth auth, Email email) {
        UserId newUserId = new UserId(state.incrementUserIdCounter());
        VerificationCode vCode = new VerificationCode(generexPattern.random());
        auths.add(new Pair<>(newUserId, auth));
        state.getUnverifiedEmails().put(vCode, email);
        return vCode;
    }

    @Override
    public Either<EmailVerificationError, Empty> setEmailAsVerified(VerificationCode vCode) {
        Either<EmailVerificationError, Empty> result = StmUtils.atomic(() -> {
            Map<VerificationCode, Email> unverifiedEmails = state.getUnverifiedEmails();
            Set<Email> verifiedEmails = state.getVerifiedEmails();

            Optional<Email> maybeEmail = Optional.ofNullable(unverifiedEmails.get(vCode));

            Optional<Empty> maybeVoid = maybeEmail.map((email) -> {
                unverifiedEmails.remove(email);
                verifiedEmails.add(email);
                return Empty.INSTANCE;
            });

            return maybeVoid.
                    map(Either::<EmailVerificationError, Empty>right).
                    orElseGet(() -> Either.left(EmailVerificationErrorInvalidCode));
        });

        return result;
    }

    @Override
    public Optional<Pair<UserId, Boolean>> findUserByAuth(Auth auth) {
        List<Pair<UserId, Auth>> auths = state.getAuth();
        Set<Email> verifiedEmails = state.getVerifiedEmails();

        Optional<UserId> maybeUserId = auths.stream()
                .filter(pair -> auth.equals(pair.getValue1()))
                .findAny()
                .map(Pair::getValue0);

        Optional<Pair<UserId, Boolean>> result = maybeUserId.
                map(uId -> new Pair<>(uId, verifiedEmails.contains(auth.getAuthEmail())));

        return result;
    }

    @Override
    public Optional<Email> findEmailFromUserId(UserId userId) {
        List<Pair<UserId, Auth>> auths = state.getAuth();

        Optional<Email> result = auths.stream()
                .filter(pair -> userId.equals(pair.getValue0()))
                .findAny()
                .map(pair -> pair.getValue1().getAuthEmail());
        return result;
    }


    @Override
    public SessionId newSession(UserId userId) {
        SessionId sId = new SessionId(userId.getUserId() + "-" + generexPattern.random());
        StmUtils.atomic(() -> {
            Map<SessionId, UserId> sessions = state.getSessions();
            sessions.put(sId, userId);
        });
        return sId;
    }

    @Override
    public Optional<UserId> findUserIdBySessionId(SessionId sessionId) {
        Map<SessionId, UserId> sessions = state.getSessions();
        return Optional.ofNullable(sessions.get(sessionId));
    }

    @Override
    public Empty notifyEmailVerification(Email email, VerificationCode verificationCode) {
        StmUtils.atomic(() -> {
            Map<Email, VerificationCode> notifications = state.getNotifications();
            notifications.put(email, verificationCode);
        });
        return Empty.INSTANCE;
    }

    public Optional<VerificationCode> getNotificationsForEmail(Email email) {
        Map<Email, VerificationCode> notifications = state.getNotifications();
        return Optional.ofNullable(notifications.get(email));
    }

}
