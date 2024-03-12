package ru.nsu.fit.injection;

import ru.nsu.fit.annotation.Inject;
import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.utility.BeanUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
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
        List<Field> fields = Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> BeanUtils.isAnnotatedWith(field, Inject.class))
                .toList();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                field.set(obj, context.getType(field.getClass()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return obj;
    }
}
