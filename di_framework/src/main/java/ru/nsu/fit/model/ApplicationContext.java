package ru.nsu.fit.model;

import java.util.LinkedList;
import java.util.Map;

import lombok.AllArgsConstructor;
import ru.nsu.fit.context.ContextFactory;

@AllArgsConstructor
public class ApplicationContext {
    private Map<Class<?>, Object> beans;
    private Map<Class<?>, BeanDefinition> beanDefinitions;
    private final ContextFactory contextFactory;

    public Map<Class<?>, Object> getBeans() {
        return beans;
    }

    public Object getType(Class<?> objectClass) {
        if (beanDefinitions.get(objectClass).getLifeCycle() == LifeCycle.PROTOTYPE) {
            return contextFactory.initBean(objectClass, beanDefinitions, new LinkedList<>());
        }
        return beans.get(objectClass);
    }

    public void setBeans(Map<Class<?>, Object> newContext) {
        this.beans = newContext;
    }
}
