package ru.nsu.fit.injection;

import ru.nsu.fit.model.ApplicationContext;

public interface InjectionProvider {
    /**
     * Заинжектить зависимости, используя контекст приложения.
     *
     * @param context контекст
     */
    void inject(ApplicationContext context);
}
