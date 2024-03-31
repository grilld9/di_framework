package ru.nsu.fit.context;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Named;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import ru.nsu.fit.LotusApplication;
import ru.nsu.fit.annotation.Bean;
import ru.nsu.fit.annotation.Configuration;
import ru.nsu.fit.annotation.Scope;
import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.model.LifeCycle;
import ru.nsu.fit.utility.BeanUtils;

public class ConfigurationRuntimeContextCreator implements ContextCreator {
    @Override
    public List<BeanDefinition> createContext() {
        return new Reflections(LotusApplication.class.getPackageName(), new SubTypesScanner(false))
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
        LifeCycle lifeCycle = Arrays.stream(method.getAnnotationsByType(Scope.class))
            .map(Scope::value)
            .findFirst()
            .orElse(LifeCycle.SINGLETON);

        String name = Arrays.stream(method.getAnnotationsByType(Named.class))
            .map(Named::value)
            .findFirst()
            .orElse(method.getName());

        return BeanDefinition.builder()
            .name(name)
            .className(method.getReturnType().getName())
            .lifeCycle(lifeCycle)
            .constructorParams(Arrays.stream(method.getParameters()).map(Parameter::getName).toList())
            .build();
    }
}
