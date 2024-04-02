package ru.nsu.fit.injection;

public interface InjectionProvider {
    /**
     * Заинжектить зависимости, используя контекст приложения.
     *
     * @param bean бин
     */
    void inject(Object bean);
}
