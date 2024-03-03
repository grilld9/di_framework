package ru.nsu.fit.context;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import ru.nsu.fit.annotation.Bean;
import ru.nsu.fit.annotation.Configuration;
import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.model.LifeCycle;
import ru.nsu.fit.utility.BeanUtils;

public class ConfigurationRuntimeContextCreator implements ContextCreator {
    @Override
    public List<BeanDefinition> createContext() {
        return new Reflections("ru.nsu.fit", new SubTypesScanner(false))
            .getSubTypesOf(Object.class)
            .stream()
            .filter(aClass -> BeanUtils.isAnnotatedWith(aClass, Configuration.class))
            .map(Class::getDeclaredMethods)
            .map(methods -> Arrays.stream(methods).filter(method -> BeanUtils.isAnnotatedWith(method, Bean.class))
                .toList())
            .flatMap(Collection::stream)
            .map(this::toBeanDefinition)
            .toList();
    }

    @Override
    public List<BeanDefinition> createContext(String packageName) {
        return new Reflections(packageName, new SubTypesScanner(false))
            .getSubTypesOf(Object.class)
            .stream()
            .filter(aClass -> BeanUtils.isAnnotatedWith(aClass, Configuration.class))
            .map(Class::getDeclaredMethods)
            .map(methods -> Arrays.stream(methods).filter(method -> BeanUtils.isAnnotatedWith(method, Bean.class))
                .toList())
            .flatMap(Collection::stream)
            .map(this::toBeanDefinition)
            .toList();
    }

    private BeanDefinition toBeanDefinition(Method method) {
        return BeanDefinition.builder()
            .name(method.getName())
            .className(method.getReturnType().getName())
            .lifeCycle(LifeCycle.SINGLETON)
            .constructorParams(Arrays.stream(method.getParameters()).map(Parameter::getName).toList())
            .build();
    }
}
