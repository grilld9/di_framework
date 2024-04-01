package ru.nsu.fit;

import javax.inject.Provider;

import ru.nsu.fit.model.ApplicationContext;
import ru.nsu.fit.test.TestPrototypeComponent;
import ru.nsu.fit.test.TestSingletonComponent;
import ru.nsu.fit.test.TestSingletonWithPrototype;
import ru.nsu.fit.test.TestThreadComponent;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = LotusApplication.run(Main.class, args);
        TestSingletonComponent beanClass = context.getBean(TestSingletonComponent.class);
        System.out.println(beanClass);
        beanClass = context.getBean(TestSingletonComponent.class);
        System.out.println(beanClass);
        beanClass = context.getBean(TestSingletonComponent.class);
        System.out.println(beanClass);

        TestPrototypeComponent prototypeComponent = context.getBean(TestPrototypeComponent.class);
        System.out.println(prototypeComponent);
        prototypeComponent = context.getBean(TestPrototypeComponent.class);
        System.out.println(prototypeComponent);
        prototypeComponent = context.getBean(TestPrototypeComponent.class);
        System.out.println(prototypeComponent);

        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(() -> {
                TestThreadComponent testThreadComponent = context.getBean(TestThreadComponent.class);
                System.out.println(testThreadComponent);
                testThreadComponent = context.getBean(TestThreadComponent.class);
                System.out.println(testThreadComponent);
            });
            thread.start();
            thread.join();
        }

        TestSingletonWithPrototype testSingletonWithPrototype = context.getBean(TestSingletonWithPrototype.class);
        System.out.println(testSingletonWithPrototype);
        System.out.println(testSingletonWithPrototype.testPrototypeComponent());
        testSingletonWithPrototype = context.getBean(TestSingletonWithPrototype.class);
        System.out.println(testSingletonWithPrototype);
        System.out.println(testSingletonWithPrototype.testPrototypeComponent());

        Provider<TestSingletonComponent> singletonComponentProvider = context.getProvider(TestSingletonComponent.class);
        System.out.println(singletonComponentProvider.get());
        System.out.println(singletonComponentProvider.get());
    }
}
