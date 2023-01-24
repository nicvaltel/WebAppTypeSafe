package org.nicvaltel.test;

import org.nicvaltel.Domain.AuthenticationAbstract;
import org.nicvaltel.Domain.Types.Auth;
import org.nicvaltel.Domain.Types.Email;
import org.nicvaltel.Domain.Types.Password;
import org.nicvaltel.Domain.Types.ValidationFunc;
import org.nicvaltel.Domain.Validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Test {

    final static AuthenticationAbstract authentication = new org.nicvaltel.Adapter.InMemory.Auth();


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
        authentication.register(auth);

    }

    public static void main(String[] args) {
        validateCheck().forEach(System.out::println);
        mkEmailCheck().forEach(System.out::println);
        mkPasswordCheck().forEach(System.out::println);
        testAuth();
    }
}
