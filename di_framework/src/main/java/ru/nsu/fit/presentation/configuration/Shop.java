package ru.nsu.fit.presentation.configuration;

import ru.nsu.fit.annotation.Bean;
import ru.nsu.fit.annotation.Configuration;
import ru.nsu.fit.annotation.Scope;
import ru.nsu.fit.model.LifeCycle;

@Configuration
public class Shop {

    @Bean
    public Seller seller(CashDesk cashDesk) {
        return new Seller(cashDesk);
    }

    @Bean
    @Scope(LifeCycle.PROTOTYPE)
    public CashDesk cashDesk() {
        return new CashDesk();
    }
}
