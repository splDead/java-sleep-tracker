package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

// класс обертка для именования функции
public class AnalysisFunction {
    public final String title;
    public final Function<List<SleepingSession>, Object> function;

    public AnalysisFunction(String title, Function<List<SleepingSession>, Object> function) {
        this.title = title;
        this.function = function;
    }
}
