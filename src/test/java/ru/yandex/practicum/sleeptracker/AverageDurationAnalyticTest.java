package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.AverageDurationAnalytic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AverageDurationAnalyticTest {

    private final AverageDurationAnalytic analytic = new AverageDurationAnalytic();

    @Test
    @DisplayName("Должен возвращать 0, если список сессий пуст")
    void applyShouldReturnZeroWhenDataIsEmptyForAverageDurationAnalytic() {
        List<SleepingSession> emptyList = new ArrayList<>();
        SleepAnalysisResult<?> result = analytic.apply(emptyList);

        assertNotNull(result);
        assertEquals("Средняя продолжительность сессии (в минутах)", result.getDescription());
        assertEquals("0", result.getValue());
    }

    @Test
    @DisplayName("Должен корректно считать среднее значение для одной сессии")
    void applyShouldCalculateCorrectlyForSingleSession() {
        LocalDateTime start = LocalDateTime.parse("2026-05-13T22:00:00");
        LocalDateTime end = LocalDateTime.parse("2026-05-14T06:00:00");
        SleepingSession session = new SleepingSession(start, end, Quality.GOOD);
        List<SleepingSession> data = List.of(session);
        SleepAnalysisResult<?> result = analytic.apply(data);

        assertEquals("480", result.getValue());
    }

    @Test
    @DisplayName("Должен корректно округлять среднее значение по правилам String.format(\"%.0f\")")
    void applyShouldRoundAverageValueWhenResultIsFractional() {
        // 30 минут
        SleepingSession s1 = new SleepingSession(
            LocalDateTime.parse("2026-05-13T12:00:00"),
            LocalDateTime.parse("2026-05-13T12:30:00"),
            Quality.BAD
        );
        // 31 минута
        SleepingSession s2 = new SleepingSession(
            LocalDateTime.parse("2026-05-13T14:00:00"),
            LocalDateTime.parse("2026-05-13T14:31:00"),
            Quality.NORMAL
        );

        // среднее: (30 + 31) / 2 = 30.5 -> String.format("%.0f") округлит до "31"
        List<SleepingSession> data = List.of(s1, s2);
        SleepAnalysisResult<?> result = analytic.apply(data);

        assertEquals("31", result.getValue());
    }
}
