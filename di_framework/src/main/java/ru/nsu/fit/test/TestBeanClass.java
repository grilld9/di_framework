package ru.nsu.fit.test;

import javax.inject.Inject;

import lombok.Getter;

@Getter
public class TestBeanClass {
    int digit;

    @Inject
    public TestBeanClass() {
        this.digit = 4;
    }
}
