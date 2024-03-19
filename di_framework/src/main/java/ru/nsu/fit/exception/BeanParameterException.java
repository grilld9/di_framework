package ru.nsu.fit.exception;

public class BeanParameterException extends RuntimeException {
    public BeanParameterException(Class<?> creationClass, Class<?> parameterType) {
        super("Parameter of constructor in %s required a bean of type '%s' that could not be found.".formatted(
            creationClass.getName(),
            parameterType.getName()
        ));
    }
}
