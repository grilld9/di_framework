package ru.nsu.fit.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.inject.Inject;

import ru.nsu.fit.exception.BeanInstantiationException;
import ru.nsu.fit.exception.BeanParameterException;
import ru.nsu.fit.exception.CircularDependencyException;
import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.model.LifeCycle;
import ru.nsu.fit.utility.BeanUtils;

public class BeanInitializer {
    private final List<BeanFactory> beanFactories = List.of(
        new PrototypeBeanFactory(this),
        new SingletonBeanFactory(this),
        new ThreadBeanFactory(this)
    );

    public <T> T doCreateBean(
        Class<T> aClass,
        Map<Class<?>, BeanDefinition> beanDefinitions,
        List<Class<?>> creationChain
    ) {
        if (!beanDefinitions.containsKey(aClass)) {
            throw new IllegalStateException("Cannot find bean definition for class " + aClass);
        }
        LifeCycle lifeCycle = beanDefinitions.get(aClass).getLifeCycle();
        return beanFactories.stream()
            .filter(factory -> factory.isApplicable(lifeCycle))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Life cycle %s is not supported".formatted(lifeCycle)))
            .doCreateBean(aClass, beanDefinitions, creationChain);
    }

    public Object initBean(
        Class<?> creationClass,
        Map<Class<?>, BeanDefinition> classToBeanDef,
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
                .map(parameter -> validateClass(parameter, creationClass, classToBeanDef))
                .map(parameter -> doCreateBean(parameter, classToBeanDef, creationChain))
                .toArray();
            return defaultConstructor.newInstance(args);
        } catch (ReflectiveOperationException | NoSuchElementException ex) {
            throw new BeanInstantiationException(creationClass);
        }
    }

    private Class<?> validateClass(
        Class<?> parameterType,
        Class<?> creationClass,
        Map<Class<?>, BeanDefinition> classToBeanDef
    ) {
        if (!classToBeanDef.containsKey(parameterType)) {
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
}
