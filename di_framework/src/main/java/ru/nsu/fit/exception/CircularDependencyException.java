package ru.nsu.fit.exception;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CircularDependencyException extends RuntimeException {
    public CircularDependencyException(List<Class<?>> dependencyChain, Class<?> creationClass) {
        super("Detect circular dependency while instantiation of class named %s:\n%s".formatted(
            creationClass.getName(),
            Stream.concat(Stream.of(creationClass.getName()), dependencyChain.stream()
                    .map(Class::getName))
                .collect(Collectors.joining(" -----> "))
        ));
    }
}
