package ru.nsu.fit.converter;

import java.lang.reflect.Type;

public interface ValueConverter {
    boolean isApplicable(Type type);

    Object convert(String value);
}
