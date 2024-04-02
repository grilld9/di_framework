import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.LotusApplication;
import ru.nsu.fit.Main;
import ru.nsu.fit.exception.CircularDependencyException;
import ru.nsu.fit.model.ApplicationContext;

public class CyclicDependencyTest {

    @Test
    public void exceptionThrowingTest() {
        Assert.assertThrows(CircularDependencyException.class, () -> {LotusApplication.run(Main.class, new String[]{});});
    }
}
