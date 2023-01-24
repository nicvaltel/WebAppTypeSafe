package org.nicvaltel.test;

import software.amazon.awssdk.utils.Either;

public class Try {

    public static void main(String[] args) {

        Either<Integer, String> eitherL = Either.left(5);
        Either<Integer, String> eitherR = Either.right("hello");
        System.out.println(eitherL.mapLeft(x -> x * 10).mapRight(s -> s + s).left().get());
        System.out.println(eitherR.mapLeft(x -> x * 10).mapRight(s -> s + s).right().get());



    }
}
