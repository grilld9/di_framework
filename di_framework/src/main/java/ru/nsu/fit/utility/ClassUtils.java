package ru.nsu.fit.utility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ClassUtils {
    public Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
