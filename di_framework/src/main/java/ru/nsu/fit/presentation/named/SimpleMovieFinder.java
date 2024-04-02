package ru.nsu.fit.presentation.named;

import ru.nsu.fit.annotation.Component;

import javax.inject.Named;

@Named("movieFinder")
@Component
public class SimpleMovieFinder {

    public SimpleMovieFinder() {
    }
}
