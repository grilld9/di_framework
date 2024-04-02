package ru.nsu.fit.converter;

import java.lang.reflect.Type;

public class CharConverter implements ValueConverter {
    @Override
    public boolean isApplicable(Type type) {
        return type == Character.class;
    }

    @Override
    public Object convert(String value) {
        if (value.length() != 1) {
            throw new RuntimeException("Cannot cast String to Character");
        }
        return value.charAt(0);
    }
}
