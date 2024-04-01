package ru.nsu.fit.utility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.inject.Named;

import lombok.experimental.UtilityClass;
import ru.nsu.fit.annotation.Scope;
import ru.nsu.fit.model.LifeCycle;

@UtilityClass
public class BeanUtils {
    public <A extends Annotation> boolean isAnnotatedWith(Class<?> aClass, Class<A> annotationClass) {
        Annotation[] annotations = aClass.getAnnotationsByType(annotationClass);
        return annotations.length != 0;
    }

    public <A extends Annotation> boolean isAnnotatedWith(Method method, Class<A> annotationClass) {
        Annotation[] annotations = method.getAnnotationsByType(annotationClass);
        return annotations.length != 0;
    }

    public <A extends Annotation> boolean isAnnotatedWith(Constructor<?> constructor, Class<A> annotationClass) {
        Annotation[] annotations = constructor.getAnnotationsByType(annotationClass);
        return annotations.length != 0;
    }

    public <A extends Annotation> boolean isAnnotatedWith(Field field, Class<A> annotationClass) {
        Annotation[] annotations = field.getAnnotationsByType(annotationClass);
        return annotations.length != 0;
    }

    public String getBeanName(Class<?> aClass) {
        return Arrays.stream(aClass.getAnnotationsByType(Named.class))
            .map(Named::value)
            .findFirst()
            .orElse(aClass.getSimpleName());
    }

    public LifeCycle getBeanLifeCycle(Class<?> aClass) {
        return Arrays.stream(aClass.getAnnotationsByType(Scope.class))
            .map(Scope::value)
            .findFirst()
            .orElse(LifeCycle.SINGLETON);

    }
}
