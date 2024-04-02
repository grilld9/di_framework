package ru.nsu.fit.parsing;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.fit.model.JsonBeanDefinition;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ConfigurationParser {

    public static List<JsonBeanDefinition> parse(Path path) {
        Gson gson = new Gson();
        List<JsonBeanDefinition> beans = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
            TypeToken<List<JsonBeanDefinition>> beanListType = new TypeToken<>() {
            };
            beans = gson.fromJson(reader, beanListType);
        } catch (IOException e) {
            log.warn("Ошибка чтения файла конфигурации: {}", e.getMessage());
        }
        return beans;
    }
}
