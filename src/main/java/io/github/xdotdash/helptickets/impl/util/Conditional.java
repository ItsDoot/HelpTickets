package io.github.xdotdash.helptickets.impl.util;

public class Conditional {

    public static <T, E extends Throwable> T checkNotNull(T reference, E ex) throws E {
        if (reference == null) {
            throw ex;
        }
        return reference;
    }

    private Conditional() {
    }
}
