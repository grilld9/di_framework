package ru.nsu.fit.converter;

import java.lang.reflect.Type;

public class ByteConverter implements ValueConverter {
    @Override
    public boolean isApplicable(Type type) {
        return type == Byte.class;
    }

    @Override
    public Object convert(String value) {
        return Byte.valueOf(value);
    }
}
