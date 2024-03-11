package ru.nsu.fit.model;

import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApplicationContext {
    private Map<Class<?>, Object> beans;

    public Map<Class<?>, Object> getBeans() {
        return beans;
    }

    public Object getType(Class<?> objectClass) {
        return beans.get(objectClass);
    }

    public void setBeans(Map<Class<?>, Object> newContext) {
        this.beans = newContext;
    }
}
