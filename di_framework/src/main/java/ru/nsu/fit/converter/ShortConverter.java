package ru.nsu.fit.converter;

import java.lang.reflect.Type;

public class ShortConverter implements ValueConverter {
    @Override
    public boolean isApplicable(Type type) {
        return type == Short.class;
    }

    @Override
    public Object convert(String value) {
        return Short.valueOf(value);
    }
}
