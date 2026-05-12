package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

public class SleeplessNightAnalytic implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> data) {
        if (data.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }

        // период анализа бессонницы
        // определяем первую ночь
        LocalDateTime firstStart = data.get(0).getStart();
        LocalDateTime firstSessionStart;

        // если первая сессия до 12, то считаем полночь текущего дня
        if (firstStart.toLocalTime().isBefore(LocalTime.NOON)) {
            firstSessionStart = firstStart.toLocalDate().atStartOfDay();
        } else {
            // если после 12, то считаем полночь от следующего дня
            firstSessionStart = firstStart.toLocalDate().plusDays(1).atStartOfDay();
        }

        LocalDateTime lastSessionEnd = data.get(data.size() - 1).getFinish();

        // считаем дни в анализируемом периоде
        long daysBetween = ChronoUnit.DAYS.between(firstSessionStart, lastSessionEnd);

        // проверяем ночи
        long sleeplessNightsCount = LongStream.rangeClosed(0, daysBetween)
            .mapToObj(firstSessionStart::plusDays) // превращаем число в полночь конкретного дня
            .filter(nightStart -> {
                LocalDateTime nightEnd = nightStart.plusHours(6); // конец ночи 6 утра

                // ищем не пересечения диапазона
                return data.stream().noneMatch(session ->
                        session.getStart().isBefore(nightEnd) && session.getFinish().isAfter(nightStart)
                );
            })
            .count();

        return new SleepAnalysisResult("Количество бессонных ночей (с 00:00 до 06:00)", sleeplessNightsCount);
    }
}
