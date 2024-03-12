package ru.nsu.fit.injection;

import ru.nsu.fit.annotation.Inject;
import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.utility.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SetterInjectionProvider implements InjectionProvider {

    @Override
    public void inject(ApplicationContext context) {
        Map<Class<?>, Object> newContext = context.getBeans().values()
                .stream()
                .map(obj -> injectForSetters(obj, context))
                .collect(Collectors.toMap(Object::getClass, Function.identity()));
        context.setBeans(newContext);
    }

    private Object injectForSetters(Object object, ApplicationContext context) {
        Class<?> aClass = object.getClass();
        List<Method> injectionMethods = Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> BeanUtils.isAnnotatedWith(method, Inject.class))
                .toList();
        for (Method method : injectionMethods) {
            for (Parameter parameter : method.getParameters()) {
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
