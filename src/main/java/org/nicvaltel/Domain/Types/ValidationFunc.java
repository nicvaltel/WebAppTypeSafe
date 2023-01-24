package org.nicvaltel.Domain.Types;

import java.util.Optional;
import java.util.function.Function;

public class ValidationFunc<E,A> {
    public Function<A, Optional<E>> validation;

    public Optional<E> apply(A a) {
        return validation.apply(a);
    }

    public ValidationFunc(Function<A, Optional<E>> validation){
        this.validation = validation;
    }
}
