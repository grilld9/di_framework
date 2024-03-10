import org.junit.Before;
import org.junit.Test;
import ru.nsu.fit.LotusApplication;
import ru.nsu.fit.Main;
import ru.nsu.fit.model.ApplicationContext;

public class InjectionTest {
    private ApplicationContext applicationContext;

    @Before
    public void before() {
        applicationContext = LotusApplication.run(Main.class, new String[]{});
    }

    @Test
    public void injectionTest() {
        applicationContext.getType(Object.class);
    }
}
