package ru.nsu.fit.test;

import ru.nsu.fit.annotation.Bean;
import ru.nsu.fit.annotation.Configuration;

@Configuration
public class TestClassWithConfigurationAndBeans {

    @Bean
    TestBeanClass testBeanClass() {
        return new TestBeanClass();
    }
}
