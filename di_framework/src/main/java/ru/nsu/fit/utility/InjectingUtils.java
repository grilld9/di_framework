package ru.nsu.fit.utility;

import java.lang.reflect.Field;

import lombok.experimental.UtilityClass;
import ru.nsu.fit.model.ApplicationContext;

@UtilityClass
public class InjectingUtils {
    public void processInjecting(Field field, ApplicationContext context, Object obj) {
        field.setAccessible(true);
        try {
            field.set(obj, context.getBean(field.getType()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
