import org.junit.Test;
import ru.nsu.fit.LotusApplication;
import ru.nsu.fit.Main;
import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.presentation.component.Laptop;
import ru.nsu.fit.presentation.configuration.CashDesk;
import ru.nsu.fit.presentation.provider.Driver;

import javax.inject.Provider;

public class ProviderTest {

    @Test
    public void providerTest() {
        ApplicationContext applicationContext = LotusApplication.run(Main.class, new String[]{});
        Driver driver = applicationContext.getBean(Driver.class);
        System.out.println(driver.getVehicleProvider().get());
        System.out.println(driver.getVehicleProvider().get());
        System.out.println(driver.getVehicleProvider().get());
        System.out.println(driver.getVehicleProvider().get());
    }
}
