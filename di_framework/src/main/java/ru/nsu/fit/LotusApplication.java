package ru.nsu.fit;

import java.util.Set;

import ru.nsu.fit.context.ContextFactory;
import ru.nsu.fit.model.ApplicationContext;

public class LotusApplication {
    private final Set<Class<?>> primarySources;
    private final ContextFactory contextFactory = new ContextFactory();

    public LotusApplication(Class<?> primaryClass) {
        this.primarySources = Set.of(primaryClass);
    }

    public static ApplicationContext run(Class<?> primaryClass, String[] args) {
        return SomeApplicationContext(primaryClass, args);
    }

    private static ApplicationContext SomeApplicationContext(Class<?> primaryClass, String[] args) {
        return new LotusApplication(primaryClass).run(args);
    }

    private ApplicationContext run(String[] args) {
        return contextFactory.getApplicationContext();
    }
}
