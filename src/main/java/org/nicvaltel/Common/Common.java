package org.nicvaltel.Common;

import software.amazon.awssdk.utils.Either;

import java.util.HashMap;
import java.util.Optional;

public class Common {

    public static void println(Optional mb){
        if(mb.isPresent())
            System.out.println("Just (" + mb.get().toString() + ")");
        else
            System.out.println("Nothing");
    }

    public static void println(Either either){
        if (either.right().isPresent() )
            System.out.println("Right (" + either.right().get().toString() + ")");
        else
            System.out.println("Left (" + either.left().get().toString() + ")");
    }

    public static void println(Object o){
        System.out.println(o);
    }



}
