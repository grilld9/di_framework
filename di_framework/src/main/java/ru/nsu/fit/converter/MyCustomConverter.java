package ru.nsu.fit.converter;

import java.lang.reflect.Type;
import java.time.Instant;

import ru.nsu.fit.annotation.Converter;

@Converter
public class MyCustomConverter implements ValueConverter {
    @Override
    public boolean isApplicable(Type type) {
        return type == Instant.class;
    }

    @Override
    public Object convert(String value) {
        return Instant.parse(value);
    }
}
