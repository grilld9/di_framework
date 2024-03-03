import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.context.ConfigurationRuntimeContextCreator;
import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.model.LifeCycle;
import ru.nsu.fit.context.ComponentRuntimeContextCreator;

public class ConfigurationRuntimeTest {

    @Test
    public void getComponentBeans() {
        Assert.assertArrayEquals(new ComponentRuntimeContextCreator().createContext().toArray(), new BeanDefinition[]{
            new BeanDefinition(
                "ru.nsu.fit.test.TestClassWithRetentionPolicyRuntime",
                "ru.nsu.fit.test.TestClassWithRetentionPolicyRuntime",
                LifeCycle.SINGLETON,
                null
            )});
    }

    @Test
    public void getConfigurationBeans() {
        Assert.assertArrayEquals(
            new ConfigurationRuntimeContextCreator().createContext().toArray(),
            new BeanDefinition[]{
                new BeanDefinition(
                    "ru.nsu.fit.test.TestBeanClass",
                    "testBeanClass",
                    LifeCycle.SINGLETON,
                    Collections.emptyList()
                ),
                new BeanDefinition(
                    "ru.nsu.fit.test.TestBeanClass",
                    "testBeanClass",
                    LifeCycle.SINGLETON,
                    List.of("arg0")
                )}
        );
    }
}
