package ru.nsu.fit.factory;

import java.util.List;

import ru.nsu.fit.model.LifeCycle;

public interface BeanFactory {
    boolean isApplicable(LifeCycle lifeCycle);

    <T> T doCreateBean(Class<T> tClass, List<Class<?>> creationChain);
}
