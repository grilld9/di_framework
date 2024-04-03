package ru.nsu.fit.utility;

import java.lang.reflect.Field;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InjectingUtils {
    public void processInjecting(Field field, Object bean, Object obj) {
        field.setAccessible(true);
        try {
            field.set(obj, bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
