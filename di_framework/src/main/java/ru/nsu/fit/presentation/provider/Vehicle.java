package ru.nsu.fit.presentation.provider;

import ru.nsu.fit.annotation.Component;
import ru.nsu.fit.annotation.Scope;
import ru.nsu.fit.model.LifeCycle;

@Component
@Scope(LifeCycle.PROTOTYPE)
public class Vehicle {

    public Vehicle() {
    }
}
