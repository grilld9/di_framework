package ru.nsu.fit.context;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import ru.nsu.fit.LotusApplication;
import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.model.JsonBeanDefinition;
import ru.nsu.fit.parsing.ConfigurationParser;

@Slf4j
public class JsonContextCreator implements ContextCreator {
    private static final String RESOURCE_PATH = "src/main/resources/application.json";

    @Override
    public List<BeanDefinition> createContext() {
        return ConfigurationParser.parse(Paths.get(RESOURCE_PATH))
            .stream()
            .map(jsonBeanDefinition -> validateAndEnrich(jsonBeanDefinition, LotusApplication.class.getPackageName()))
            .toList();
    }

    @Override
    public List<BeanDefinition> createContext(String packageName) {
        return ConfigurationParser.parse(Paths.get(RESOURCE_PATH))
            .stream()
            .map(jsonBeanDefinition -> validateAndEnrich(jsonBeanDefinition, packageName))
            .toList();
    }

    private BeanDefinition validateAndEnrich(JsonBeanDefinition jsonDef, String packageName) {
        Class<?> targetClass = getByClassName(jsonDef.getClassName(), packageName);
        return toDefinition(jsonDef, targetClass);
    }

    private BeanDefinition toDefinition(JsonBeanDefinition jsonDef, Class<?> targetClass) {
        return BeanDefinition.builder()
            .name(jsonDef.getName())
            .targetClass(targetClass)
            .lifeCycle(jsonDef.getLifeCycle())
            .constructorParams(jsonDef.getConstructorParams())
            .build();
    }

    private Class<?> getByClassName(String className, String packageName) {
        return new Reflections(packageName, new SubTypesScanner(false))
            .getSubTypesOf(Object.class)
            .stream()
            .filter(aClass -> aClass.getName().equals(className))
            .findAny()
            .orElseThrow(() -> new RuntimeException("Класса %s не существует".formatted(className)));
    }
}
