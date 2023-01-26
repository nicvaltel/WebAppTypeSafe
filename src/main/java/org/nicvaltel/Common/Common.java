package org.nicvaltel.Common;

import software.amazon.awssdk.utils.Either;

import java.util.Optional;

public class Common {


    public static void println(Optional mb) {
        mb.ifPresentOrElse(
                val -> System.out.println("Just (" + val + ")"),
                () -> System.out.println("Nothing")
        );
    }

    public static void println(Either either) {
        String str = either.right().isPresent() ?
                "Right (" + either.right().get() + ")" :
                "Left (" + either.left().get() + ")";
        System.out.println(str);

    }

    public static void println(Object o) {
        System.out.println(o);
    }
}
