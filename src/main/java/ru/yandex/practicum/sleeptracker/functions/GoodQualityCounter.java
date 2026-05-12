package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.Quality;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class GoodQualityCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> data) {
        long count = data.stream()
                .filter(s -> s.getQuality() == Quality.GOOD)
                .count();
        return new SleepAnalysisResult("Количество сессий с хорошим качеством сна", count);
    }
}
