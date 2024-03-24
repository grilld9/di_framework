package ru.nsu.fit.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import ru.nsu.fit.annotation.Inject;
import ru.nsu.fit.exception.BeanInstantiationException;
import ru.nsu.fit.exception.BeanParameterException;
import ru.nsu.fit.exception.CircularDependencyException;
import ru.nsu.fit.injection.FieldInjectionProvider;
import ru.nsu.fit.injection.InjectionProvider;
import ru.nsu.fit.injection.SetterInjectionProvider;
import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.model.LifeCycle;
import ru.nsu.fit.utility.BeanUtils;
import ru.nsu.fit.utility.ClassUtils;

public class ContextFactory {
    private final List<ContextCreator> contextCreators = List.of(
        new ComponentRuntimeContextCreator(),
        new ConfigurationRuntimeContextCreator(),
        new JsonContextCreator()
    );

    private final List<InjectionProvider> injectionProviderList = List.of(
        new FieldInjectionProvider(),
        new SetterInjectionProvider()
    );

    private Map<Class<?>, BeanDefinition> beanDefinitions;
    private ApplicationContext applicationContext;

    public Object doCreateBean(Class<?> aClass) {
        if (beanDefinitions.get(aClass).getLifeCycle() == LifeCycle.SINGLETON) {
            return applicationContext.getBeans().get(aClass);
        } else if (beanDefinitions.get(aClass).getLifeCycle() == LifeCycle.PROTOTYPE) {
            return initBean(aClass, beanDefinitions, new LinkedList<>());
        } else {
            return null; //TODO
        }
    }

    public ApplicationContext getApplicationContext() {
        List<List<BeanDefinition>> listOfListOfBeanDefinitions = contextCreators.stream()
            .map(ContextCreator::createContext)
            .toList();
        List<BeanDefinition> mergedContext = mergeContext(listOfListOfBeanDefinitions);
        applicationContext = instantiateContextByBeanDefinitions(mergedContext);
        injectionProviderList.forEach(injectionProvider -> injectionProvider.inject(applicationContext));
        return applicationContext;
    }

    private ApplicationContext instantiateContextByBeanDefinitions(List<BeanDefinition> mergedContext) {
        beanDefinitions = mergedContext.stream()
            .collect(Collectors.toMap(beanDef -> ClassUtils.getClass(beanDef.getClassName()), Function.identity()));
        Map<Class<?>, Object> classToInstance = beanDefinitions.keySet().stream()
            .map(aClass -> initBean(aClass, beanDefinitions, new LinkedList<>()))
            .collect(Collectors.toMap(Object::getClass, Function.identity()));
        return new ApplicationContext(classToInstance, this);
    }

    private Object initBean(
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
                .map(parameter -> initBean(parameter, classToBeanDef, creationChain))
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
            .filter(constructor -> constructor.getParameters().length == finalFieldCount)
            .findFirst()
            .orElseThrow();
    }

    private List<BeanDefinition> mergeContext(List<List<BeanDefinition>> listOfListOfBeanDefinitions) {
        return listOfListOfBeanDefinitions.stream()
            .flatMap(Collection::stream)
            .toList();
    }
}
