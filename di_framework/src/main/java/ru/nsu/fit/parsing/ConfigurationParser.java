package ru.nsu.fit.parsing;

import static org.reflections.Reflections.log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.UncheckedIOException;
import ru.nsu.fit.model.BeanDefinition;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ConfigurationParser {

    public static List<BeanDefinition> parse(Path path) {
        Gson gson = new Gson();
        List<BeanDefinition> beans;
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
            TypeToken<List<BeanDefinition>> beanListType = new TypeToken<>(){};
            beans = gson.fromJson(reader, beanListType);
        } catch (IOException e) {
            log.error("Ошибка чтения файла конфигурации: {}", e.getMessage());
            throw new UncheckedIOException(e);
        }
        return beans;
    }
}
