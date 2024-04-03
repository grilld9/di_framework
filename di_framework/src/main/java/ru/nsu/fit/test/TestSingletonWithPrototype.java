package ru.nsu.fit.test;

import ru.nsu.fit.annotation.Component;

@Component
public record TestSingletonWithPrototype(SomeInterface testPrototypeComponent) {
}
