package ru.nsu.fit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class BeanDefinition {
    private Class<?> targetClass;
    private String name;
    private LifeCycle lifeCycle;
    private List<String> constructorParams;
}
