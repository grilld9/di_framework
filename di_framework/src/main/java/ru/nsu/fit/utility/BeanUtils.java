package ru.nsu.fit.utility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lombok.experimental.UtilityClass;

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
}
