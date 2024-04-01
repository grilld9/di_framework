package ru.nsu.fit.context;

import java.util.List;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import ru.nsu.fit.LotusApplication;
import ru.nsu.fit.annotation.Component;
import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.utility.BeanUtils;

public class ComponentRuntimeRecordContextCreator implements ContextCreator {
    @Override
    public List<BeanDefinition> createContext() {
        return new Reflections(LotusApplication.class.getPackageName(), new SubTypesScanner(false))
            .getSubTypesOf(Record.class)
            .stream()
            .filter(aClass -> BeanUtils.isAnnotatedWith(aClass, Component.class))
            .map(this::toBeanDefinition)
            .toList();
    }

    @Override
    public List<BeanDefinition> createContext(String packageName) {
        return new Reflections(packageName, new SubTypesScanner(false))
            .getSubTypesOf(Object.class)
            .stream()
            .filter(aClass -> BeanUtils.isAnnotatedWith(aClass, Component.class))
            .map(this::toBeanDefinition)
            .toList();
    }

    private BeanDefinition toBeanDefinition(Class<?> aClass) {
        return BeanDefinition.builder()
            .name(BeanUtils.getBeanName(aClass))
            .className(aClass.getName())
            .lifeCycle(BeanUtils.getBeanLifeCycle(aClass))
            .build();
    }
}
