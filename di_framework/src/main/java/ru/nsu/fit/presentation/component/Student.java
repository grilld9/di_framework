package ru.nsu.fit.presentation.component;

import ru.nsu.fit.annotation.Component;
import ru.nsu.fit.presentation.component.Laptop;

@Component
public class Student {

    private final Laptop laptop;
    public Student(Laptop laptop) {
        this.laptop = laptop;
    }
}
