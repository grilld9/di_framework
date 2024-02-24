package ru.nsu.fit.parsing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Bean {
    private String className;
    private String name;
    private LifeCycle lifeCycle;
    private List<String> constructorParams;
}
