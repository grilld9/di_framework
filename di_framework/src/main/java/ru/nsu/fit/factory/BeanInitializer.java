package ru.nsu.fit.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.inject.Inject;

import ru.nsu.fit.exception.BeanDefinitionNotFoundException;
import ru.nsu.fit.exception.BeanInstantiationException;
import ru.nsu.fit.exception.CircularDependencyException;
import ru.nsu.fit.injection.BeanPostProcessor;
import ru.nsu.fit.injection.FieldInjectionPostProcessor;
import ru.nsu.fit.injection.SetterInjectionPostProcessor;
import ru.nsu.fit.injection.ValueInjectionPostProcessor;
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
    private final List<BeanPostProcessor> injectionProviderList = List.of(
        new FieldInjectionPostProcessor(this),
        new SetterInjectionPostProcessor(this),
        new ValueInjectionPostProcessor()
    );

    public BeanInitializer(Map<String, BeanDefinition> beanDefinitions) {
        this.beanDefinitions = beanDefinitions;
    }

    public <T> T getBean(Class<T> targetClass) {
        List<Class<?>> creationChain = new ArrayList<>();
        return doCreateBean(targetClass, creationChain);
    }

    public Object getBean(String name) {
        List<Class<?>> creationChain = new ArrayList<>();
        return doCreateBean(name, creationChain);
    }

    @SuppressWarnings("unchecked")
    public <T> T doCreateBean(Class<T> targetClass, List<Class<?>> creationChain) {
        BeanDefinition beanDefinition = getDefinitionByClass(targetClass)
            .orElseThrow(() -> new BeanDefinitionNotFoundException(targetClass));
        return (T) createWithDefinition(beanDefinition, creationChain);
    }

    public Object doCreateBean(String name, List<Class<?>> creationChain) {
        if (!beanDefinitions.containsKey(name)) {
            throw new IllegalStateException("Cannot find bean definition for name " + name);
        }
        BeanDefinition beanDefinition = beanDefinitions.get(name);
        return createWithDefinition(beanDefinition, creationChain);
    }

    public Object createWithDefinition(BeanDefinition beanDefinition, List<Class<?>> creationChain) {
        LifeCycle lifeCycle = beanDefinition.getLifeCycle();
        return beanFactories.stream()
            .filter(factory -> factory.isApplicable(lifeCycle))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Life cycle %s is not supported".formatted(lifeCycle)))
            .doCreateBean(beanDefinition.getTargetClass(), creationChain);
    }

    public Object initBean(
        Class<?> targetClass,
        List<Class<?>> creationChain
    ) {
        Class<?> creationClass = getCreationType(targetClass);
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
                .map(parameter -> doCreateBean(parameter.getType(), creationChain))
                .toArray();
            Object createdBean = defaultConstructor.newInstance(args);
            injectionProviderList.forEach(provider -> provider.process(createdBean));
            return createdBean;
        } catch (ReflectiveOperationException | NoSuchElementException ex) {
            throw new BeanInstantiationException(creationClass);
        }
    }

    private Class<?> getCreationType(Class<?> targetClass) {
        Optional<BeanDefinition> optionalBeanDefinition = getDefinitionByClass(targetClass);
        if (optionalBeanDefinition.isEmpty()) {
            throw new BeanDefinitionNotFoundException(targetClass);
        }
        return optionalBeanDefinition.get().getTargetClass();
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
        if (targetClass.isInterface()) {
            return beanDefinitions.values().stream()
                .filter(def -> targetClass.isAssignableFrom(def.getTargetClass()))
                .findFirst();
        }
        return beanDefinitions.values().stream()
            .filter(def -> def.getTargetClass().equals(targetClass))
            .findFirst();
    }

    public void initializeBeans() {
        beanDefinitions.entrySet().stream()
            .filter(def -> def.getValue().getLifeCycle() != LifeCycle.PROTOTYPE)
            .forEach(def -> getBean(def.getKey()));
    }
}
