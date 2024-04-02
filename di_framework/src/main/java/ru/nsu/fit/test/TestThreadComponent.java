package ru.nsu.fit.test;

import javax.inject.Inject;

import lombok.Getter;
import ru.nsu.fit.annotation.Component;
import ru.nsu.fit.annotation.Scope;
import ru.nsu.fit.model.LifeCycle;

@Getter
@Component
@Scope(LifeCycle.THREAD)
public class TestThreadComponent {

    @Inject
    private SomeInterface someInterface;
}
