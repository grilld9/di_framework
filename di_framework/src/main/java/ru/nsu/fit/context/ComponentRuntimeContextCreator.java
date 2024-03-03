package ru.nsu.fit.context;

import java.util.Arrays;
import java.util.List;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import ru.nsu.fit.annotation.Component;
import ru.nsu.fit.annotation.Scope;
import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.model.LifeCycle;
import ru.nsu.fit.utility.BeanUtils;

public class ComponentRuntimeContextCreator implements ContextCreator {

    @Override
    public List<BeanDefinition> createContext(String packageName) {
        return new Reflections(packageName, new SubTypesScanner(false))
            .getSubTypesOf(Object.class)
            .stream()
            .filter(aClass -> BeanUtils.isAnnotatedWith(aClass, Component.class))
            .map(this::toBeanDefinition)
            .toList();
    }

    @Override
    public List<BeanDefinition> createContext() {
        return new Reflections("ru.nsu.fit.parsing", new SubTypesScanner(false))
            .getSubTypesOf(Object.class)
            .stream()
            .filter(aClass -> BeanUtils.isAnnotatedWith(aClass, Component.class))
            .map(this::toBeanDefinition)
            .toList();
    }

    private BeanDefinition toBeanDefinition(Class<?> aClass) {
        LifeCycle lifeCycle = Arrays.stream(aClass.getAnnotationsByType(Scope.class))
            .map(Scope::type)
            .findFirst()
            .orElse(LifeCycle.SINGLETON);

        return BeanDefinition.builder()
            .name(aClass.getSimpleName())
            .className(aClass.getName())
            .lifeCycle(lifeCycle)
            .build();
    }
}
