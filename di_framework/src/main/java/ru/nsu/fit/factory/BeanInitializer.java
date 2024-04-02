package ru.nsu.fit.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.inject.Inject;

import ru.nsu.fit.exception.BeanDefinitionNotFoundException;
import ru.nsu.fit.exception.BeanInstantiationException;
import ru.nsu.fit.exception.BeanParameterException;
import ru.nsu.fit.exception.CircularDependencyException;
import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.model.LifeCycle;
import ru.nsu.fit.utility.BeanUtils;

public class BeanInitializer {
    private final Map<String, BeanDefinition> beanDefinitions;
    private final List<BeanFactory> beanFactories = List.of(
        new PrototypeBeanFactory(this),
        new SingletonBeanFactory(this),
        new ThreadBeanFactory(this)
    );

    public BeanInitializer(Map<String, BeanDefinition> beanDefinitions) {
        this.beanDefinitions = beanDefinitions;
    }

    public <T> T doCreateBean(Class<T> targetClass) {
        List<Class<?>> creationChain = new ArrayList<>();
        return doCreateBean(targetClass, creationChain);
    }

    public Object doCreateBean(String name) {
        List<Class<?>> creationChain = new ArrayList<>();
        return doCreateBean(name, creationChain);
    }

    public <T> T doCreateBean(Class<T> targetClass, List<Class<?>> creationChain) {
        BeanDefinition beanDefinition = getDefinitionByClass(targetClass)
            .orElseThrow(() -> new BeanDefinitionNotFoundException(targetClass));
        LifeCycle lifeCycle = beanDefinition.getLifeCycle();
        return beanFactories.stream()
            .filter(factory -> factory.isApplicable(lifeCycle))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Life cycle %s is not supported".formatted(lifeCycle)))
            .doCreateBean(targetClass, creationChain);
    }

    public Object doCreateBean(String name, List<Class<?>> creationChain) {
        if (!beanDefinitions.containsKey(name)) {
            throw new IllegalStateException("Cannot find bean definition for name " + name);
        }
        BeanDefinition beanDefinition = beanDefinitions.get(name);
        LifeCycle lifeCycle = beanDefinition.getLifeCycle();
        return beanFactories.stream()
            .filter(factory -> factory.isApplicable(lifeCycle))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Life cycle %s is not supported".formatted(lifeCycle)))
            .doCreateBean(beanDefinition.getTargetClass(), creationChain);
    }

    public Object initBean(
        Class<?> creationClass,
        List<Class<?>> creationChain
    ) {
        try {
            if (creationChain.contains(creationClass)) {
                throw new CircularDependencyException(creationChain, creationClass);
            }
            creationChain.add(creationClass);
            Optional<Constructor<?>> optionalConstructor = Arrays.stream(creationClass.getDeclaredConstructors())
                .filter(constructor -> BeanUtils.isAnnotatedWith(constructor, Inject.class))
                .findFirst();
            Constructor<?> defaultConstructor = optionalConstructor.isEmpty() ?
                getNoArgsConstructor(creationClass) : optionalConstructor.get();
            Object[] args = Arrays.stream(defaultConstructor.getParameters())
                .map(Parameter::getType)
                .map(parameter -> validateClass(parameter, creationClass))
                .map(parameter -> doCreateBean(parameter, creationChain))
                .toArray();
            return defaultConstructor.newInstance(args);
        } catch (ReflectiveOperationException | NoSuchElementException ex) {
            throw new BeanInstantiationException(creationClass);
        }
    }

    private Class<?> validateClass(Class<?> parameterType, Class<?> creationClass) {
        if (getDefinitionByClass(parameterType).isEmpty()) {
            throw new BeanParameterException(creationClass, parameterType);
        }
        return parameterType;
    }

    private Constructor<?> getNoArgsConstructor(Class<?> aClass) {
        long finalFieldCount = Arrays.stream(aClass.getDeclaredFields())
            .filter(field -> Modifier.isFinal(field.getModifiers()))
            .count();
        return Arrays.stream(aClass.getConstructors())
            .filter(constructor -> constructor.getParameters().length <= finalFieldCount)
            .findFirst()
            .orElseThrow();
    }

    private Optional<BeanDefinition> getDefinitionByClass(Class<?> targetClass) {
        return beanDefinitions.values().stream()
            .filter(def -> def.getTargetClass().equals(targetClass))
            .findFirst();
    }
}
