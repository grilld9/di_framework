package ru.nsu.fit.injection;

import lombok.RequiredArgsConstructor;
import ru.nsu.fit.factory.BeanInitializer;
import ru.nsu.fit.utility.BeanUtils;
import ru.nsu.fit.utility.InjectingUtils;

import java.util.Arrays;

import javax.inject.Inject;

@RequiredArgsConstructor
public class FieldInjectionProvider implements InjectionProvider {
    private final BeanInitializer beanInitializer;

    @Override
    public void inject(Object bean) {
        Class<?> aClass = bean.getClass();
        Arrays.stream(aClass.getDeclaredFields())
            .filter(field -> BeanUtils.isAnnotatedWith(field, Inject.class))
            .forEach(field -> InjectingUtils.processInjecting(field, beanInitializer.getBean(field.getType()), bean));

    }
}
