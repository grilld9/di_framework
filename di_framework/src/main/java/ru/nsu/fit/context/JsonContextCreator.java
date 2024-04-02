package ru.nsu.fit.context;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import ru.nsu.fit.LotusApplication;
import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.parsing.ConfigurationParser;

@Slf4j
public class JsonContextCreator implements ContextCreator {
    @Override
    public List<BeanDefinition> createContext() {
        List<BeanDefinition> beans = ConfigurationParser.parse(Paths.get("src/main/resources/application.json"));
        if (beans == null) {
            return new ArrayList<>();
        }
        return beans;
    }

    @Override
    public List<BeanDefinition> createContext(String packageName) {
        List<BeanDefinition> beans = ConfigurationParser.parse(Paths.get("src/main/resources/application.json"));
        if (beans == null) {
            return new ArrayList<>();
        }
        return beans;
    }
}
