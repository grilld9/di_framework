package ru.nsu.fit.context;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.parsing.ConfigurationParser;

@Slf4j
public class JsonContextCreator implements ContextCreator {
    @Override
    public List<BeanDefinition> createContext() {
        List<BeanDefinition> beans = ConfigurationParser.parse(Paths.get("src/main/resources/application.json"));
        for (BeanDefinition bean : beans) {
            Class<?> aClass = validateByClassName(bean.getClassName(), "ru.nsu.fit");
            validateConstructorParams(bean.getConstructorParams(), aClass);
        }
        return beans;
    }

    private void validateConstructorParams(List<String> constructorParams, Class<?> aClass) {
        Set<Class<?>> paramsTypes = constructorParams
            .stream()
            .map(param -> validateByClassName(param, aClass.getPackageName()))
            .collect(Collectors.toSet());
        if (Arrays.stream(aClass.getConstructors())
            .noneMatch(constructor ->
                Arrays.stream(constructor.getParameterTypes())
                .collect(Collectors.toSet())
                .equals(paramsTypes))) throw new RuntimeException("Конструктор "
            + "с параметрами " + constructorParams
            + " не найден в классе " + aClass.getName());
    }

    private Class<?> validateByClassName(String className, String packageName) {
        return new Reflections(packageName, new SubTypesScanner(false))
            .getSubTypesOf(Object.class)
            .stream()
            .filter(aClass -> aClass.getName().equals(className))
            .findAny().orElseThrow(() -> new RuntimeException("Класса " + className
                + " не существует"));
    }

    @Override
    public List<BeanDefinition> createContext(String packageName) {
        List<BeanDefinition> beans = ConfigurationParser.parse(Paths.get("src/main/resources/application.json"));
        for (BeanDefinition bean : beans) {
            Class<?> aClass = validateByClassName(bean.getClassName(), packageName);
            validateConstructorParams(bean.getConstructorParams(), aClass);
        }
        return beans;
    }
}
