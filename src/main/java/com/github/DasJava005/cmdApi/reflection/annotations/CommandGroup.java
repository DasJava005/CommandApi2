package com.github.DasJava005.cmdApi.reflection.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandGroup {
    String value();

    String description() default "A CommandAPI provided description.";
    String [] aliases() default {};
}
