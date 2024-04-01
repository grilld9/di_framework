package ru.nsu.fit.test;

import ru.nsu.fit.annotation.Component;
import ru.nsu.fit.annotation.Scope;
import ru.nsu.fit.model.LifeCycle;

@Component
@Scope(LifeCycle.PROTOTYPE)
public class TestPrototypeComponent {
}
