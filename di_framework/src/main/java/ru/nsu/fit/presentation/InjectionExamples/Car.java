package ru.nsu.fit.presentation.InjectionExamples;

import ru.nsu.fit.annotation.Component;

import javax.inject.Inject;

@Component
public class Car {

    @Inject
    private Wheel wheel;
    private final Engine engine;
    @Inject
    public Car(Engine engine) {
        this.engine = engine;
    }

    private Roof roof;
    @Inject
    public void setRoof(Roof roof) {
        this.roof = roof;
    }

    public Wheel getWheel() {
        return wheel;
    }

    public Engine getEngine() {
        return engine;
    }

    public Roof getRoof() {
        return roof;
    }
}
