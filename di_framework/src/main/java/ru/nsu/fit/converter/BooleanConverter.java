package ru.nsu.fit.converter;

import java.lang.reflect.Type;

public class BooleanConverter implements ValueConverter {
    @Override
    public boolean isApplicable(Type type) {
        return type == Boolean.class;
    }

    @Override
    public Object convert(String value) {
        return Boolean.valueOf(value);
    }
}
