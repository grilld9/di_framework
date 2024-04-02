package ru.nsu.fit.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.nsu.fit.model.LifeCycle;

public class SingletonBeanFactory implements BeanFactory {
    private final Map<Class<?>, Object> beanFactory = new HashMap<>();
    private final BeanInitializer beanInitializer;

    public SingletonBeanFactory(BeanInitializer beanInitializer) {
        this.beanInitializer = beanInitializer;
    }

    @Override
    public boolean isApplicable(LifeCycle lifeCycle) {
        return lifeCycle == LifeCycle.SINGLETON;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T doCreateBean(Class<T> tClass, List<Class<?>> creationChain) {
        if (beanFactory.containsKey(tClass)) {
            return (T) beanFactory.get(tClass);
        } else {
            T bean = (T) beanInitializer.initBean(tClass, creationChain);
            beanFactory.put(tClass, bean);
            return bean;
        }
    }
}
