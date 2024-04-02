package ru.nsu.fit.context;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import ru.nsu.fit.factory.BeanInitializer;
import ru.nsu.fit.injection.FieldInjectionProvider;
import ru.nsu.fit.injection.InjectionProvider;
import ru.nsu.fit.injection.SetterInjectionProvider;
import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.model.BeanDefinition;

public class ContextFactory {

    private final BeanInitializer beanInitializer;

    private final ApplicationContext applicationContext;

    private final Map<String, BeanDefinition> beanDefinitions;
    private final List<ContextCreator> contextCreators = List.of(
        new ComponentRuntimeContextCreator(),
        new ComponentRuntimeRecordContextCreator(),
        new ConfigurationRuntimeContextCreator(),
        new JsonContextCreator()
    );

    private final List<InjectionProvider> injectionProviderList = List.of(
        new FieldInjectionProvider(),
        new SetterInjectionProvider()
    );

    public ContextFactory() {
        List<List<BeanDefinition>> allDefinitions = contextCreators.stream()
            .map(ContextCreator::createContext)
            .toList();
        this.beanDefinitions = mergeContext(allDefinitions).stream()
            .collect(Collectors.toMap(BeanDefinition::getName, Function.identity()));
        this.beanInitializer = new BeanInitializer(beanDefinitions);
        this.applicationContext = new ApplicationContext(this);
        Map<Class<?>, Object> beans = beanDefinitions.keySet().stream().map(this::getBean)
            .collect(Collectors.toMap(Object::getClass, Function.identity()));
        injectionProviderList.forEach(provider -> provider.inject(beans));
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private List<BeanDefinition> mergeContext(List<List<BeanDefinition>> allDefinitions) {
        return allDefinitions.stream()
            .flatMap(Collection::stream)
            .toList();
    }

    public <T> T getBean(Class<T> objectClass) {
        return beanInitializer.doCreateBean(objectClass);
    }

    public Object getBean(String name) {
        return beanInitializer.doCreateBean(name);
    }
}
