import org.junit.Test;
import ru.nsu.fit.model.JsonBeanDefinition;
import ru.nsu.fit.parsing.ConfigurationParser;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

public class ConfigurationParserTests {

    @Test
    public void simpleTest() {
        Path path = FileSystems.getDefault().getPath("simple_test.json");
        List<JsonBeanDefinition> beans = ConfigurationParser.parse(path);
        for (JsonBeanDefinition bean : beans) {
            System.out.println(bean);
        }
    }
}
