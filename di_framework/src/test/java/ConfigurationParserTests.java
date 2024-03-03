import org.junit.Test;
import ru.nsu.fit.model.BeanDefinition;
import ru.nsu.fit.parsing.ConfigurationParser;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

public class ConfigurationParserTests {

    @Test
    public void simpleTest() throws IOException {
        Path path = FileSystems.getDefault().getPath("simple_test.json");
        ConfigurationParser parser = new ConfigurationParser();
        List<BeanDefinition> beans = parser.parse(path);
        for (BeanDefinition bean : beans) {
            System.out.println(bean);
        }
    }
}
