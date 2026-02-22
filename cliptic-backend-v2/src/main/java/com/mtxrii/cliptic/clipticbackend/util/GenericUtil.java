package com.mtxrii.cliptic.clipticbackend.util;

public final class GenericUtil {
    private GenericUtil() { }

    public static <A, B> boolean equals(A a, B b) {
        if ((a == null && b != null) || (a != null && b == null)) {
            return false;
        }
        if (a == null && b == null) {
            return true;
        }
        return a.equals(b);
    }
}
