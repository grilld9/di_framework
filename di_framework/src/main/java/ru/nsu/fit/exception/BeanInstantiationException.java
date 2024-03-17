package ru.nsu.fit.exception;

public class BeanInstantiationException extends RuntimeException {
    public BeanInstantiationException(Class<?> aClass) {
        super("Failed to instantiate class [%s]: no default constructor found".formatted(aClass.getName()));
    }
}
