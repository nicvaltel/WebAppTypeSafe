package org.nicvaltel.Domain;


import org.javatuples.Pair;
import org.nicvaltel.Domain.Types.*;
import software.amazon.awssdk.utils.Either;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.function.UnaryOperator.identity;
import static org.nicvaltel.Domain.Types.LoginError.LoginErrorEmailNotVerified;
import static org.nicvaltel.Domain.Types.LoginError.LoginErrorInvalidAuth;

public abstract class AuthenticationAbstract implements Authentication, EmailVerificationNotif, AuthRepo, SessionRepo {

    @Override
    public String rawEmail(Email email) {
        return email.getEmail();
    }

    @Override
    public Optional<Email> getUser(UserId userId) {
        return findEmailFromUserId(userId);
    }


    @Override
    public Either<LoginError, SessionId> login(Auth auth) {
        Optional<Pair<UserId, Boolean>> user = findUserByAuth(auth);
        Either<LoginError, SessionId> result =
                user.isPresent() ? // Empty if user is not found
                        Either.left(LoginErrorInvalidAuth) :
                        user.get().getValue1() ? // email is verified bool flag
                                Either.right(newSession(user.get().getValue0())) :
                                Either.left(LoginErrorEmailNotVerified);
        return result;
    }

    @Override
    public Optional<UserId> resolveSessionId(SessionId sessionId) {
        return findUserIdBySessionId(sessionId);
    }

    @Override
    public Either<List<String>, Email> mkEmail(String inputStr) {
        final String regexPattern = "^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}$";

        return Validation.validateEmail(
                Email::new,
                List.of(Validation.regexMatches(regexPattern, "Not a valid email")),
                inputStr
        );
    }

    @Override
    public String rawPassword(Password password) {
        return password.getPassword();
    }

    @Override
    public Either<List<String>, Password> mkPassword(String passwordStr) {
        List<ValidationFunc<String, String>> passwdChecks = Arrays.asList(
                Validation.lengthBetween(5, 50, "Should between 5 and 50"),
                Validation.regexMatches("\\d", "Should contain number"),
                Validation.regexMatches("[A-Z]", "Should contain uppercase letter"),
                Validation.regexMatches("[a-z]", "Should contain lowercase letter")
        );

        return Validation.validatePassword(
                Password::new,
                passwdChecks,
                passwordStr
        );
    }

    @Override
    public Either<RegistrationError, Void> register(Auth auth) {
        Either<RegistrationError, VerificationCode> vCode = addAuth(auth);
        return vCode.
                mapRight(vc -> notifyEmailVerification(auth.getAuthEmail(), vc)).
                mapLeft(identity());
    }

    @Override
    public Either<EmailVerificationError, Void> verifyEmail(VerificationCode vCode) {
        return setEmailAsVerified(vCode);
    }

//    @Override
//    public Either<RegistrationError, VerificationCode> addAuth(Auth auth) {
//        System.out.println("adding auth: " + rawEmail(auth.getAuthEmail()));
//        return Either.right(new VerificationCode("fake verification code"));
//    }

//    @Override
//    public Void notifyEmailVerification(Email email, VerificationCode verificationCode) {
//        System.out.println("Notify " + rawEmail(email) + " - " + verificationCode);
//        return null; // It's correct - Void
//    }
}
