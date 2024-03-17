package ru.nsu.fit.injection;

import ru.nsu.fit.annotation.Inject;
import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.utility.BeanUtils;
import ru.nsu.fit.utility.InjectingUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FieldInjectionProvider implements InjectionProvider {
    @Override
    public void inject(ApplicationContext context) {
        Map<Class<?>, Object> newContext = context.getBeans().values()
            .stream()
            .map(obj -> injectAllFields(obj, context))
            .collect(Collectors.toMap(Object::getClass, Function.identity()));
        context.setBeans(newContext);
    }

    private Object injectAllFields(Object obj, ApplicationContext context) {
        Class<?> aClass = obj.getClass();
        Arrays.stream(aClass.getDeclaredFields())
            .filter(field -> BeanUtils.isAnnotatedWith(field, Inject.class))
            .forEach(field -> InjectingUtils.processInjecting(field, context, obj));
        return obj;
    }
}
