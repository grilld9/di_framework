package ru.nsu.fit.injection;

import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.utility.BeanUtils;
import ru.nsu.fit.utility.InjectingUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

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
                Arrays.stream(aClass.getDeclaredFields())
                    .filter(field -> field.getType().equals(parameter.getType()))
                    .forEach(field -> InjectingUtils.processInjecting(field, context, object));
            }
        }
        return object;
    }
}
