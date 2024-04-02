package ru.nsu.fit.injection;

public interface BeanPostProcessor {
    /**
     * Заинжектить зависимости, используя контекст приложения.
     *
     * @param bean бин
     */
    void process(Object bean);
}
