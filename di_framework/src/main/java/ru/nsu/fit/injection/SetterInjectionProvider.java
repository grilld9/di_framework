package ru.nsu.fit.injection;

import lombok.RequiredArgsConstructor;
import ru.nsu.fit.factory.BeanInitializer;
import ru.nsu.fit.utility.BeanUtils;
import ru.nsu.fit.utility.InjectingUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

@RequiredArgsConstructor
public class SetterInjectionProvider implements InjectionProvider {
    private final BeanInitializer beanInitializer;

    @Override
    public void inject(Object bean) {
        Class<?> targetClass = bean.getClass();
        List<Method> injectionMethods = Arrays.stream(targetClass.getDeclaredMethods())
            .filter(method -> BeanUtils.isAnnotatedWith(method, Inject.class))
            .toList();
        for (Method method : injectionMethods) {
            for (Parameter parameter : method.getParameters()) {
                Arrays.stream(targetClass.getDeclaredFields())
                    .filter(field -> field.getType().equals(parameter.getType()))
                    .forEach(field -> InjectingUtils.processInjecting(
                        field,
                        beanInitializer.getBean(field.getType()),
                        bean
                    ));
            }
        }
    }
}
