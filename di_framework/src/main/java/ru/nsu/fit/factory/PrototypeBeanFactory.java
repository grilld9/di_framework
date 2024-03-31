package ru.nsu.fit.factory;

import java.util.List;
import java.util.Map;

import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.model.LifeCycle;

public class PrototypeBeanFactory implements BeanFactory {
    private final BeanInitializer beanInitializer;

    public PrototypeBeanFactory(BeanInitializer beanInitializer) {
        this.beanInitializer = beanInitializer;
    }

    @Override
    public boolean isApplicable(LifeCycle lifeCycle) {
        return lifeCycle == LifeCycle.PROTOTYPE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T doCreateBean(
        Class<T> tClass,
        Map<Class<?>, BeanDefinition> beanDefinitions,
        List<Class<?>> creationChain
    ) {
        return (T) beanInitializer.initBean(tClass, beanDefinitions, creationChain);
    }
}
