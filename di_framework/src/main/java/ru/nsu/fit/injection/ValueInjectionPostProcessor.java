package ru.nsu.fit.injection;

import java.lang.reflect.Field;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import ru.nsu.fit.LotusApplication;
import ru.nsu.fit.annotation.Converter;
import ru.nsu.fit.annotation.Value;
import ru.nsu.fit.converter.BooleanConverter;
import ru.nsu.fit.converter.ByteConverter;
import ru.nsu.fit.converter.CharConverter;
import ru.nsu.fit.converter.DoubleConverter;
import ru.nsu.fit.converter.FloatConverter;
import ru.nsu.fit.converter.IntegerConverter;
import ru.nsu.fit.converter.LongConverter;
import ru.nsu.fit.converter.ShortConverter;
import ru.nsu.fit.converter.ValueConverter;
import ru.nsu.fit.utility.BeanUtils;

@Slf4j
public class ValueInjectionPostProcessor implements BeanPostProcessor {
    private final List<ValueConverter> converterList;

    public ValueInjectionPostProcessor() {
        this.converterList = new ArrayList<>();
        converterList.addAll(List.of(
            new IntegerConverter(),
            new ByteConverter(),
            new ShortConverter(),
            new LongConverter(),
            new FloatConverter(),
            new CharConverter(),
            new DoubleConverter(),
            new BooleanConverter()
        ));
        converterList.addAll(getAnnotated());
    }

    @Override
    public void process(Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
            .filter(field -> BeanUtils.isAnnotatedWith(field, Value.class))
            .forEach(field -> inject(field, bean));
    }

    private void inject(Field field, Object bean) {
        field.setAccessible(true);
        try {
            String value = field.getAnnotationsByType(Value.class)[0].value();
            if (field.getType() != String.class) {
                ValueConverter converter = converterList.stream()
                    .filter(valueConverter -> valueConverter.isApplicable(field.getType()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No converter was found for type " + field.getType()));
                field.set(bean, converter.convert(value));
            } else {
                field.set(bean, value);
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<? extends ValueConverter> getAnnotated() {
        return new Reflections(LotusApplication.class.getPackageName())
            .getSubTypesOf(ValueConverter.class)
            .stream()
            .filter(converter -> BeanUtils.isAnnotatedWith(converter, Converter.class))
            .map(converterClass -> {
                try {
                    return converterClass.getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    log.warn("Cannot instantiate custom mapper", e);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .toList();

    }
}
