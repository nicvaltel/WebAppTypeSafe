package org.nicvaltel.playground;

import org.nicvaltel.Adapter.InMemory.AuthInMemory;
import org.nicvaltel.Domain.AuthenticationAbstract;
import org.nicvaltel.Domain.Types.*;
import org.nicvaltel.Domain.Validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.nicvaltel.Common.Common.println;

public class Test {

    final static AuthenticationAbstract authentication = AuthInMemory.INSTANCE;


    private static List<String> validateCheck() {
        final List<ValidationFunc<String, String>> checkRegex = Arrays.asList(
                Validation.regexMatches("A", "Must contain 'A'"),
                Validation.regexMatches("B", "Must contain 'B'")
        );

        List<Boolean> out = new ArrayList<>();
        out.add(Validation.lengthBetween(1, 5, "err").apply("12345").isEmpty());
        out.add(Validation.lengthBetween(1, 5, "err").apply("123456").equals(Optional.of("err")));
        out.add(Validation.regexMatches("^hello", "err").apply("hello world").isEmpty());
        out.add(Validation.regexMatches("^hello", "err").apply("failed world").equals(Optional.of("err")));
        out.add(Validation.validateEmail(Email::new, checkRegex, "abc").
                left().equals(Optional.of(Arrays.asList("Must contain 'A'", "Must contain 'B'"))));
        out.add(Validation.validateEmail(Email::new, checkRegex, "ABc").right().isPresent());

        return out.stream().map(b -> Boolean.toString(b)).collect(Collectors.toList());
    }

    private static List<String> mkEmailCheck() {
        List<Boolean> out = Arrays.asList(
                authentication.mkEmail("test").left().get().get(0).equals("Not a valid email"),
                authentication.mkEmail("test@example.org").right().isPresent()
        );
        return out.stream().map(b -> Boolean.toString(b)).collect(Collectors.toList());
    }

    private static List<String> mkPasswordCheck() {
        List<Boolean> out = Arrays.asList(
                authentication.mkPassword("ABC").left().get().
                        equals(Arrays.asList("Should between 5 and 50", "Should contain number", "Should contain lowercase letter")),
                authentication.mkEmail("test@example.org").right().isPresent()
        );
        return out.stream().map(b -> Boolean.toString(b)).collect(Collectors.toList());
    }


    private static void testAuth() {
        Email email = authentication.mkEmail("test@example.org").right().get();
        Password password = authentication.mkPassword("1234ABCdef").right().get();
        Auth auth = new Auth(email, password);
        println(authentication.addAuth(auth));
        println(authentication.findUserByAuth(auth));
        println(authentication.findEmailFromUserId(new UserId(1)));
        SessionId sessionId = authentication.newSession(new UserId(1));
        println(sessionId);
        println(authentication.findUserIdBySessionId(sessionId));

    }

    public static void main(String[] args) {
        validateCheck().forEach(System.out::println);
        mkEmailCheck().forEach(System.out::println);
        mkPasswordCheck().forEach(System.out::println);
        testAuth();
    }
}
