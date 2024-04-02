import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.LotusApplication;
import ru.nsu.fit.Main;
import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.presentation.InjectionExamples.Car;

public class InjectionTest {

    @Test
    public void injectCarTest() {
        ApplicationContext applicationContext = LotusApplication.run(Main.class, new String[]{});
        Car car = applicationContext.getBean(Car.class);
        Assert.assertNotEquals(null, car.getEngine());
        Assert.assertNotEquals(null, car.getWheel());
        Assert.assertNotEquals(null, car.getRoof());
    }
}
