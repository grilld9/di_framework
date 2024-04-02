package ru.nsu.fit.converter;

import java.lang.reflect.Type;

public class DoubleConverter implements ValueConverter {
    @Override
    public boolean isApplicable(Type type) {
        return type == Double.class;
    }

    @Override
    public Object convert(String value) {
        return Double.valueOf(value);
    }
}
