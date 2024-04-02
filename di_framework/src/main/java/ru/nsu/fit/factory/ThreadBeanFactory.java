package ru.nsu.fit.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.nsu.fit.model.LifeCycle;

public class ThreadBeanFactory implements BeanFactory {
    private final BeanInitializer beanInitializer;
    private final Map<Long, Map<Class<?>, Object>> threadToBeanFactory = new HashMap<>();

    public ThreadBeanFactory(BeanInitializer beanInitializer) {
        this.beanInitializer = beanInitializer;
    }

    @Override
    public boolean isApplicable(LifeCycle lifeCycle) {
        return lifeCycle == LifeCycle.THREAD;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T doCreateBean(Class<T> tClass, List<Class<?>> creationChain) {
        long threadId = Thread.currentThread().getId();
        if (threadToBeanFactory.containsKey(threadId) && threadToBeanFactory.get(threadId).containsKey(tClass)) {
            return (T) threadToBeanFactory.get(threadId).get(tClass);
        }
        Map<Class<?>, Object> currentThreadFactory;
        if (!threadToBeanFactory.containsKey(threadId)) {
            currentThreadFactory = new HashMap<>();
        } else {
            currentThreadFactory = threadToBeanFactory.get(threadId);
        }
        T bean = (T) beanInitializer.initBean(tClass, creationChain);
        currentThreadFactory.put(tClass, bean);
        threadToBeanFactory.put(threadId, currentThreadFactory);
        return bean;
    }
}
