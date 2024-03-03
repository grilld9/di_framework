package ru.nsu.fit.context;

import java.util.List;

import ru.nsu.fit.model.BeanDefinition;

public class ContextFactory {
    private final List<ContextCreator> contextCreators = List.of(
        new ComponentRuntimeContextCreator()
    );

    public List<BeanDefinition> getApplicationContext() {
        List<List<BeanDefinition>> listOfListOfBeanDefinitions = contextCreators.stream()
            .map(ContextCreator::createContext)
            .toList();
        List<BeanDefinition> mergedContext = mergeContext(listOfListOfBeanDefinitions);
        validateContext(mergedContext);
        return mergedContext;
    }

    private void validateContext(List<BeanDefinition> mergedContext) {
        System.out.println("Everything is ok");
    }

    private List<BeanDefinition> mergeContext(List<List<BeanDefinition>> listOfListOfBeanDefinitions) {
        return listOfListOfBeanDefinitions.get(0);
    }
}
