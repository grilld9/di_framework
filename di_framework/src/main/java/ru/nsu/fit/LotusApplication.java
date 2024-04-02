package ru.nsu.fit;

import ru.nsu.fit.context.ContextFactory;
import ru.nsu.fit.model.ApplicationContext;

public class LotusApplication {
    private final ContextFactory contextFactory = new ContextFactory();

    public static ApplicationContext run(Class<?> primaryClass, String[] args) {
        return SomeApplicationContext(primaryClass, args);
    }

    private static ApplicationContext SomeApplicationContext(Class<?> primaryClass, String[] args) {
        return new LotusApplication().run(args);
    }

    private ApplicationContext run(String[] args) {
        return contextFactory.getApplicationContext();
    }
}
