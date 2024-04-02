package ru.nsu.fit.utility;

import java.lang.reflect.Field;
import java.util.Map;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InjectingUtils {
    public void processInjecting(Field field, Map<Class<?>, Object> beans, Object obj) {
        field.setAccessible(true);
        try {
            field.set(obj, beans.get(field.getType()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
