package ru.nsu.fit.context;

import java.util.List;

import ru.nsu.fit.model.BeanDefinition;

public interface ContextCreator {
    /**
     * Создать контекст, в зависимости от текущей директории.
     *
     * @return список определений бинов
     */
    List<BeanDefinition> createContext();

    /**
     * Создать контекст, опираясь на конкретную директорию.
     *
     * @param packageName название пакета, в котором нужно собирать контекст
     * @return список определений бинов
     */

    List<BeanDefinition> createContext(String packageName);
}
