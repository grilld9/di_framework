package ru.nsu.fit.context;

import java.util.Collection;
import java.util.LinkedList;
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
import ru.nsu.fit.utility.ClassUtils;

public class ContextFactory {
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
    private Map<Class<?>, BeanDefinition> beanDefinitions;
    private final BeanInitializer beanInitializer = new BeanInitializer();

    private ApplicationContext applicationContext;

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
            .map(thisClass -> beanInitializer.doCreateBean(thisClass, beanDefinitions, new LinkedList<>()))
            .collect(Collectors.toMap(Object::getClass, Function.identity()));
        return new ApplicationContext(classToInstance, this);
    }

    private List<BeanDefinition> mergeContext(List<List<BeanDefinition>> listOfListOfBeanDefinitions) {
        return listOfListOfBeanDefinitions.stream()
            .flatMap(Collection::stream)
            .toList();
    }

    public <T> T getBean(Class<T> objectClass) {
        return beanInitializer.doCreateBean(objectClass, beanDefinitions, new LinkedList<>());
    }
}
