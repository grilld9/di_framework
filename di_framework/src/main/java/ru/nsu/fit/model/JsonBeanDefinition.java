package ru.nsu.fit.model;

import java.util.List;

import lombok.Data;

@Data
public class JsonBeanDefinition {
    private final String name;
    private final String className;
    private LifeCycle lifeCycle;
    private List<String> constructorParams;
}
