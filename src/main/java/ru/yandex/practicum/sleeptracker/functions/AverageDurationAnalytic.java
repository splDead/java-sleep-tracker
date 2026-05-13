package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class AverageDurationAnalytic implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> data) {
        double avg = data.stream()
                .mapToLong(s -> Duration.between(s.getStart(), s.getEnd()).toMinutes())
                .average()
                .orElse(0.0);
        return new SleepAnalysisResult("Средняя продолжительность сессии (в минутах)", String.format("%.0f", avg));
    }
}
