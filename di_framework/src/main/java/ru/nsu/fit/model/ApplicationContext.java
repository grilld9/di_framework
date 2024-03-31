package ru.nsu.fit.model;

import java.util.Map;

import javax.inject.Provider;

import lombok.AllArgsConstructor;
import ru.nsu.fit.context.ContextFactory;

@AllArgsConstructor
public class ApplicationContext {
    private Map<Class<?>, Object> beans;

    public Map<Class<?>, Object> getBeans() {
        return beans;
    }

    private ContextFactory contextFactory;

    public <T> T getBean(Class<T> objectClass) {
        return contextFactory.getBean(objectClass);
    }

    public <T> Provider<T> getProvider(Class<T> objectClass) {
        return () -> contextFactory.getBean(objectClass);
    }

    public void setBeans(Map<Class<?>, Object> newContext) {
        this.beans = newContext;
    }
}
