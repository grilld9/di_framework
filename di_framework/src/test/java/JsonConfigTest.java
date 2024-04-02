import org.junit.Test;
import ru.nsu.fit.LotusApplication;
import ru.nsu.fit.Main;
import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.presentation.fromJson.Hammer;
import ru.nsu.fit.presentation.fromJson.Tor;

public class JsonConfigTest {

    @Test
    public void jsonConfigTest() {
        ApplicationContext applicationContext = LotusApplication.run(Main.class, new String[]{});
        System.out.println(applicationContext.getBean(Tor.class));
        System.out.println(applicationContext.getBean(Hammer.class));
    }
}
