package ru.nsu.fit.injection;

import ru.nsu.fit.utility.BeanUtils;
import ru.nsu.fit.utility.InjectingUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class SetterInjectionProvider implements InjectionProvider {

    @Override
    public void inject(Map<Class<?>, Object> beans) {
        beans.values().forEach(obj -> injectForSetters(obj, beans));
    }

    private void injectForSetters(Object object, Map<Class<?>, Object> context) {
        Class<?> aClass = object.getClass();
        List<Method> injectionMethods = Arrays.stream(aClass.getDeclaredMethods())
            .filter(method -> BeanUtils.isAnnotatedWith(method, Inject.class))
            .toList();
        for (Method method : injectionMethods) {
            for (Parameter parameter : method.getParameters()) {
                Arrays.stream(aClass.getDeclaredFields())
                    .filter(field -> field.getType().equals(parameter.getType()))
                    .forEach(field -> InjectingUtils.processInjecting(field, context, object));
            }
        }
    }
}
