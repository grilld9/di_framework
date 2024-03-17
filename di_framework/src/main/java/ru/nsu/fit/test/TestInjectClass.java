package ru.nsu.fit.test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nsu.fit.annotation.Component;

@Component
@Getter
@RequiredArgsConstructor
public class TestInjectClass {
    private String value;

    private final TestBeanClass bean;

    private TestInjectClass testInjectClass;

    private TestBeanClass secondBean;

    private TestBeanClass ololoBean;

}
