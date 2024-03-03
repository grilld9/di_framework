package ru.nsu.fit.parsing;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.nsu.fit.model.BeanDefinition;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ConfigurationParser {

    private final Gson gson;

    public ConfigurationParser() {
        gson = new Gson();
    }

    public List<BeanDefinition> parse(Path path) throws IOException {
        List<BeanDefinition> beans;
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
            TypeToken<List<BeanDefinition>> beanListType = new TypeToken<>(){};
            beans = gson.fromJson(reader, beanListType);
        }
        return beans;
    }
}
