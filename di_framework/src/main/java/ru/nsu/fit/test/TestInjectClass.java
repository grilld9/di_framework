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
    @Value("OLolololo")
    private String value;

    @Value("32")
    private Integer value2;

    @Value("3")
    private Integer value3;

    @Value("2021-12-11T10:09:08.00Z")
    private Instant instant;

    private final TestBeanClass bean;

    private TestInjectClass testInjectClass;

    private TestBeanClass secondBean;

    private TestBeanClass ololoBean;

}
