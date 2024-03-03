package ru.nsu.fit.annotation;

import ru.nsu.fit.model.LifeCycle;

public @interface Scope {
    LifeCycle type() default LifeCycle.SINGLETON;
}
