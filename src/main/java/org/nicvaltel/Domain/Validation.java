package org.nicvaltel.Domain;

import org.nicvaltel.Domain.Types.Email;
import org.nicvaltel.Domain.Types.Password;
import org.nicvaltel.Domain.Types.ValidationFunc;
import software.amazon.awssdk.utils.Either;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface Validation {

    static private Optional<String> rngBetween(int minRange, int maxRange, int val, String msg) {
        return (val >= minRange && val <= maxRange) ? Optional.empty() : Optional.of(msg);
    }

    static ValidationFunc<String, String> lengthBetween(int minLen, int maxLen, String errorMsg) {
        Function<String, Optional<String>> validation = (String val) ->
                rngBetween(minLen, maxLen, val.length(), errorMsg);
        return new ValidationFunc(validation);
    }

    static ValidationFunc<String, String> regexMatches(String regex, String errorMsg) {
        Function<String, Optional<String>> validation = s -> {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(s);
            return matcher.find() ? Optional.empty() : Optional.of(errorMsg);
        };
        return new ValidationFunc(validation);
    }

    static Either<List<String>, Email> validateEmail(Function<String, Email> constructor, List<ValidationFunc<String, String>> validations, String value) {
        List<String> errors = validations.stream().
                map(f -> f.apply(value)).
                filter(Optional::isPresent).
                map(Optional::get).
                collect(Collectors.toList());

        return errors.isEmpty() ? Either.right(constructor.apply(value)) : Either.left(errors);
    }

    static Either<List<String>, Password> validatePassword(Function<String, Password> constructor, List<ValidationFunc<String, String>> validations, String value) {
        List<String> errors = validations.stream().
                map(f -> f.apply(value)).
                filter(Optional::isPresent).
                map(Optional::get).
                collect(Collectors.toList());

        return errors.isEmpty() ? Either.right(constructor.apply(value)) : Either.left(errors);
    }

}
