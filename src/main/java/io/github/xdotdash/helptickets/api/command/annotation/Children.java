package io.github.xdotdash.helptickets.api.command.annotation;

import io.github.xdotdash.helptickets.api.command.AbstractCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Children {

    /**
     * @return the children to register to this command
     */
    Class<? extends AbstractCommand>[] value();
}