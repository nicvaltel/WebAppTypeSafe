package org.nicvaltel.Adapter.InMemory;

import com.mifmif.common.regex.Generex;
import org.javatuples.Pair;
import org.nicvaltel.Common.RWS;
import org.nicvaltel.Domain.*;
import org.nicvaltel.Domain.Types.*;
import software.amazon.awssdk.utils.Either;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.nicvaltel.Domain.Types.EmailVerificationError.*;
import static org.nicvaltel.Domain.Types.RegistrationError.*;

public class AuthInMemory extends AuthenticationAbstract implements RWS<Void,Void,State> {

    private State state;

    private Generex generexPattern = new Generex("[A-Za-z0-9]{16}");

    public AuthInMemory() {
        state = State.initialState();
    }


    @Override
    public Either<RegistrationError, VerificationCode> addAuth(Auth auth) {

        List<Pair<UserId, Auth>> auths = get().getAuth();
        Email email = auth.getAuthEmail();
        boolean isDuplicate = auths.stream()
                .anyMatch(pair -> email.equals(pair.getValue1().getAuthEmail()));

        Either<RegistrationError, VerificationCode> result  = isDuplicate ?
                Either.left(RegistrationErrorEmailTaken) :
                Either.right(makeVerificationCode(auths, auth, email));

        return result;
    }

    private VerificationCode makeVerificationCode(List<Pair<UserId, Auth>> auths, Auth auth, Email email){
        UserId newUserId = new UserId(get().incrementUserIdCounter());
        VerificationCode vCode  = new VerificationCode(generexPattern.random());
        auths.add(new Pair<>(newUserId, auth));
        get().getUnverifiedEmails().put(vCode, email);
        return vCode;
    }

    @Override
    public Either<EmailVerificationError, Void> setEmailAsVerified(VerificationCode vCode) {
        Map<VerificationCode, Email> unverifiedEmails = get().getUnverifiedEmails();
        Set<Email> verifiedEmails = get().getVerifiedEmails();

        Optional<Email> maybeEmail = Optional.ofNullable(unverifiedEmails.get(vCode));

        Optional<Void> maybeVoid = maybeEmail.map((email) -> {
            unverifiedEmails.remove(email);
            verifiedEmails.add(email);
            return null;
        });

        Either<EmailVerificationError, Void> result = maybeVoid.
                map(Either::<EmailVerificationError, Void>right).
                orElseGet(() -> Either.left(EmailVerificationErrorInvalidCode));

        return result;
    }

    @Override
    public Optional<Pair<UserId,Boolean>> findUserByAuth (Auth auth){
        List<Pair<UserId, Auth>> auths = get().getAuth();
        Set<Email> verifiedEmails = get().getVerifiedEmails();

        Optional<UserId> maybeUserId = auths.stream()
                .filter(pair -> auth.equals(pair.getValue1()))
                .findAny()
                .map(Pair::getValue0);

        Optional<Pair<UserId,Boolean>> result = maybeUserId.
                map(uId -> new Pair<>(uId, verifiedEmails.contains(auth.getAuthEmail())));

        return result;
    }

    @Override
    public Optional<Email> findEmailFromUserId(UserId userId) {
        List<Pair<UserId, Auth>> auths = get().getAuth();

        Optional<Email> result = auths.stream()
                .filter(pair -> userId.equals(pair.getValue0()))
                .findAny()
                .map(pair -> pair.getValue1().getAuthEmail());
        return result;
    }



    @Override
    public SessionId newSession(UserId userId) {
        SessionId sId = new SessionId(userId.getUserId() + "-" + generexPattern.random());
        Map<SessionId,UserId> sessions = get().getSessions();
        sessions.put(sId, userId);
        return sId;
    }

    @Override
    public Optional<UserId> findUserIdBySessionId(SessionId sessionId) {
        Map<SessionId,UserId> sessions = get().getSessions();
        return Optional.ofNullable(sessions.get(sessionId));
    }

    @Override
    public Void notifyEmailVerification(Email email, VerificationCode verificationCode) {
        Map<Email, VerificationCode> notifications = get().getNotifications();
        notifications.put(email, verificationCode);
        return null;
    }
    public Optional<VerificationCode> getNotificationsForEmail(Email email){
        Map<Email, VerificationCode> notifications = get().getNotifications();
        return Optional.ofNullable(notifications.get(email));
    }

    @Override
    public State get() {
        return state;
    }

    @Override
    public void put(State state) {
        this.state = state;
    }

    @Override
    public Void ask() {
        return null; // It's correct - Void Reader
    }

    @Override
    public void tell(Void writer) {
        // empty method, its correct - Void Writer
    }
}
