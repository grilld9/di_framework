package ru.nsu.fit.injection;

import ru.nsu.fit.utility.BeanUtils;
import ru.nsu.fit.utility.InjectingUtils;

import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;

public class FieldInjectionProvider implements InjectionProvider {
    @Override
    public void inject(Map<Class<?>, Object> beans) {
        beans.values().forEach(obj -> injectAllFields(obj, beans));
    }

    private void injectAllFields(Object obj, Map<Class<?>, Object> beans) {
        Class<?> aClass = obj.getClass();
        Arrays.stream(aClass.getDeclaredFields())
            .filter(field -> BeanUtils.isAnnotatedWith(field, Inject.class))
            .forEach(field -> InjectingUtils.processInjecting(field, beans, obj));
    }
}
