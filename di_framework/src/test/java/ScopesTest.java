import org.junit.Before;
import org.junit.Test;
import ru.nsu.fit.LotusApplication;
import ru.nsu.fit.Main;
import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.presentation.component.Student;
import ru.nsu.fit.presentation.configuration.CashDesk;
import ru.nsu.fit.test.TestThreadComponent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ScopesTest {
    private ApplicationContext applicationContext;

    @Before
    public void before() {
        applicationContext = LotusApplication.run(Main.class, new String[]{});
    }

    @Test
    public void singletonTest() {
        Student firstStudent = applicationContext.getBean(Student.class);
        Student secondStudent = applicationContext.getBean(Student.class);
        assert firstStudent == secondStudent;
    }

    @Test
    public void prototypeTest() {
        CashDesk firstCashDesk = applicationContext.getBean(CashDesk.class);
        CashDesk secondCashDesk = applicationContext.getBean(CashDesk.class);
        assert firstCashDesk != secondCashDesk;
        firstCashDesk.setCash(10000);
        secondCashDesk.setCash(5000);
        assert firstCashDesk.getCash() != secondCashDesk.getCash();
    }

    @Test
    public void threadTest() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(() -> {
                TestThreadComponent testThreadComponent = applicationContext.getBean(TestThreadComponent.class);
                System.out.println(testThreadComponent);
                testThreadComponent = applicationContext.getBean(TestThreadComponent.class);
                System.out.println(testThreadComponent);
            });
            thread.start();
            thread.join();
        }
    }
}
