package org.nicvaltel.Common;

public class Empty {
    // used for Either<A,Empty> instead of Optional<A> indicates the success of operation
    // Void doesn't work because of Either.right(Void) behaves as Either.right().isPresent = false
    public static final Empty INSTANCE = new Empty();

    private Empty(){
    }
}
