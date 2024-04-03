package ru.nsu.fit.converter;

import java.lang.reflect.Type;

public class IntegerConverter implements ValueConverter {
    @Override
    public boolean isApplicable(Type type) {
        return type == Integer.class;
    }

    @Override
    public Object convert(String value) {
        return Integer.valueOf(value);
    }
}
