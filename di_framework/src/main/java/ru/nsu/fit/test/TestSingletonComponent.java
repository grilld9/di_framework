package ru.nsu.fit.test;

import ru.nsu.fit.annotation.Component;
import ru.nsu.fit.annotation.Scope;
import ru.nsu.fit.model.LifeCycle;

@Component
@Scope(LifeCycle.SINGLETON) // Необязательно, все бины по дефолту считаются синглтонами
public class TestSingletonComponent {
    private static final String name = "Singleton";

    public String getName() {
        return name;
    }
}
