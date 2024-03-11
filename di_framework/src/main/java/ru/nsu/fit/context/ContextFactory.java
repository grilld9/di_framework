package ru.nsu.fit.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import ru.nsu.fit.injection.ConstructorInjectionProvider;
import ru.nsu.fit.injection.InjectionProvider;
import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.model.BeanDefinition;

public class ContextFactory {
    private final List<ContextCreator> contextCreators = List.of(
        new ComponentRuntimeContextCreator(),
        new ConfigurationRuntimeContextCreator(),
        new JsonContextCreator()
    );

    private final List<InjectionProvider> injectionProviderList = List.of(
        new ConstructorInjectionProvider()
    );

    public ApplicationContext getApplicationContext() {
        List<List<BeanDefinition>> listOfListOfBeanDefinitions = contextCreators.stream()
            .map(ContextCreator::createContext)
            .toList();
        List<BeanDefinition> mergedContext = mergeContext(listOfListOfBeanDefinitions);
        validateContext(mergedContext);
        ApplicationContext applicationContext = instantiateContextByBeanDefinitions(mergedContext);
        injectionProviderList.forEach(injectionProvider -> injectionProvider.inject(applicationContext));
        return applicationContext;
    }

    private ApplicationContext instantiateContextByBeanDefinitions(List<BeanDefinition> mergedContext) {
        return new ApplicationContext(
            mergedContext.stream()
                .map(beanDefinition -> {
                    try {
                        return Class.forName(beanDefinition.getClassName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toMap(Function.identity(), this::initBean)));
    }

    private Object initBean(Class<?> aClass) {
        try {
            Constructor<?> constructor = Arrays.stream(aClass.getConstructors())
                .findFirst()
                .orElseThrow();
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void validateContext(List<BeanDefinition> mergedContext) {
        System.out.println("Everything is ok");
    }

    private List<BeanDefinition> mergeContext(List<List<BeanDefinition>> listOfListOfBeanDefinitions) {
        return listOfListOfBeanDefinitions.get(0);
    }
}
