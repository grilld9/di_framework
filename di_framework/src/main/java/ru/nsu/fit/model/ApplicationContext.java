package ru.nsu.fit.model;

import javax.inject.Provider;

import lombok.AllArgsConstructor;
import ru.nsu.fit.context.ContextFactory;

@AllArgsConstructor
public class ApplicationContext {
    private ContextFactory contextFactory;

    public <T> T getBean(Class<T> objectClass) {
        return contextFactory.getBean(objectClass);
    }

    public Object getBean(String name) {
        return contextFactory.getBean(name);
    }

    public <T> Provider<T> getProvider(Class<T> objectClass) {
        return () -> contextFactory.getBean(objectClass);
    }
}
