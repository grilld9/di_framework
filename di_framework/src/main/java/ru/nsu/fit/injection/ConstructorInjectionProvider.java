package ru.nsu.fit.injection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import ru.nsu.fit.annotation.Inject;
import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.utility.BeanUtils;

public class ConstructorInjectionProvider implements InjectionProvider {
    @Override
    public void inject(ApplicationContext context) {
        Map<Class<?>, Object> newContext = context.getBeans().values()
            .stream()
            .map(obj -> injectForConstructors(obj, context))
            .collect(Collectors.toMap(Object::getClass, Function.identity()));
        context.setBeans(newContext);
    }

    private Object injectForConstructors(Object object, ApplicationContext context) {
        Class<?> aClass = object.getClass();
        List<Constructor<?>> injectionConstructors = Arrays.stream(aClass.getDeclaredConstructors())
            .filter(constructor -> BeanUtils.isAnnotatedWith(constructor, Inject.class))
            .toList();
        for (Constructor<?> constructor : injectionConstructors) {
            for (Parameter parameter : constructor.getParameters()) {
                List<Field> neededFields = Arrays.stream(aClass.getFields())
                    .filter(field -> field.getClass().equals(parameter.getType()))
                    .toList();
                neededFields.forEach(field -> field.setAccessible(true));
                neededFields.forEach(field -> {
                    try {
                        field.set(object, context.getType(field.getClass()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        return object;
    }
}
