package ru.nsu.fit.test;

import java.time.Instant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nsu.fit.annotation.Component;
import ru.nsu.fit.annotation.Value;

@Component
@Getter
@RequiredArgsConstructor
public class TestInjectClass {
    @Value("inject.value")
    private String value;

    @Value("inject.value-2")
    private Integer value2;

    @Value("inject.value-3")
    private Integer value3;

    @Value("inject.value-instant")
    private Instant instant;

    private final TestBeanClass bean;

    private TestInjectClass testInjectClass;

    private TestBeanClass secondBean;

    private TestBeanClass ololoBean;

}
