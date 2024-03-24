package ru.nsu.fit.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import ru.nsu.fit.context.ContextFactory;

@AllArgsConstructor
public class ApplicationContext {
    private Map<Class<?>, Object> beans;

    public Map<Class<?>, Object> getBeans() {
        return beans;
    }
    private ContextFactory contextFactory;

    public Object getBean(Class<?> objectClass) {
        return contextFactory.doCreateBean(objectClass);
    }

    public void setBeans(Map<Class<?>, Object> newContext) {
        this.beans = newContext;
    }
}
