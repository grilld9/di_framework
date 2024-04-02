package ru.nsu.fit.exception;

public class BeanDefinitionNotFoundException extends RuntimeException {
    public BeanDefinitionNotFoundException(String name) {
        super("Cannot find bean definition for name %s".formatted(name));
    }

    public BeanDefinitionNotFoundException(Class<?> targetClass) {
        super("Cannot find bean definition for class %s".formatted(targetClass));
    }
}
