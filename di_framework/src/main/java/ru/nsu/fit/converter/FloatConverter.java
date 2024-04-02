package ru.nsu.fit.converter;

import java.lang.reflect.Type;

public class FloatConverter implements ValueConverter {
    @Override
    public boolean isApplicable(Type type) {
        return type == Float.class;
    }

    @Override
    public Object convert(String value) {
        return Float.valueOf(value);
    }
}
