package ru.nsu.fit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ru.nsu.fit.model.LifeCycle;

@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    LifeCycle value() default LifeCycle.SINGLETON;
}
