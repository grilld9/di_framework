package ru.nsu.fit.converter;

import java.lang.reflect.Type;

public class LongConverter implements ValueConverter {
    @Override
    public boolean isApplicable(Type type) {
        return type == Long.class;
    }

    @Override
    public Object convert(String value) {
        return Long.valueOf(value);
    }
}
