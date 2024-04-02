package ru.nsu.fit.presentation.provider;

import ru.nsu.fit.annotation.Component;
import ru.nsu.fit.annotation.Scope;
import ru.nsu.fit.model.LifeCycle;

import javax.inject.Provider;

@Component
@Scope(LifeCycle.SINGLETON)
public class Driver {

    private final Provider<Vehicle> vehicleProvider;

    public Driver(Provider<Vehicle> vehicleProvider) {
        this.vehicleProvider = vehicleProvider;
    }

    public Provider<Vehicle> getVehicleProvider() {
        return vehicleProvider;
    }
}
