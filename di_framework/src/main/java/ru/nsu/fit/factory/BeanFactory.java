package ru.nsu.fit.factory;

import java.util.List;
import java.util.Map;

import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.model.LifeCycle;

public interface BeanFactory {
    boolean isApplicable(LifeCycle lifeCycle);

    <T> T doCreateBean(Class<T> tClass, Map<Class<?>, BeanDefinition> beanDefinitions, List<Class<?>> creationChain);
}
