package ru.nsu.fit.injection;

import java.util.Map;

public interface InjectionProvider {
    /**
     * Заинжектить зависимости, используя контекст приложения.
     *
     * @param beans бины
     */
    void inject(Map<Class<?>, Object> beans);
}
