package ru.nsu.fit.context;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import ru.nsu.fit.factory.BeanInitializer;
import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.model.BeanDefinition;

public class ContextFactory {

    private final BeanInitializer beanInitializer;
    private final ApplicationContext applicationContext;
    private final List<ContextCreator> contextCreators = List.of(
        new ComponentRuntimeContextCreator(),
        new ComponentRuntimeRecordContextCreator(),
        new ConfigurationRuntimeContextCreator(),
        new JsonContextCreator()
    );

    public ContextFactory() {
        List<List<BeanDefinition>> allDefinitions = contextCreators.stream()
            .map(ContextCreator::createContext)
            .toList();
        var beanDefinitions = mergeContext(allDefinitions).stream()
            .collect(Collectors.toMap(BeanDefinition::getName, Function.identity()));
        this.beanInitializer = new BeanInitializer(beanDefinitions);
        this.applicationContext = new ApplicationContext(this);
        beanInitializer.initializeBeans();
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
        return beanInitializer.getBean(objectClass);
    }

    public Object getBean(String name) {
        return beanInitializer.getBean(name);
    }
}
