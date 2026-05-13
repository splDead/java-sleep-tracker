package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.MaxDurationAnalytic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MaxDurationAnalyticTest {

    private final MaxDurationAnalytic analytic = new MaxDurationAnalytic();

    @Test
    @DisplayName("Должен возвращать 0, если список сессий пуст")
    void applyShouldReturnZeroWhenDataIsEmptyForMaxDurationAnalytic() {
        List<SleepingSession> emptyList = new ArrayList<>();
        SleepAnalysisResult result = analytic.apply(emptyList);

        assertNotNull(result);
        assertEquals("Максимальная продолжительность сессии (в минутах)", result.getDescription());
        assertEquals(0L, result.getValue());
    }

    @Test
    @DisplayName("Должен возвращать длительность единственной сессии")
    void applyShouldReturnDurationForSingleSessionForMaxDurationAnalytic() {
        LocalDateTime start = LocalDateTime.parse("2026-05-13T22:00:00");
        LocalDateTime end = LocalDateTime.parse("2026-05-14T06:00:00");
        SleepingSession session = new SleepingSession(start, end, Quality.GOOD);
        List<SleepingSession> data = List.of(session);
        SleepAnalysisResult result = analytic.apply(data);

        assertEquals(480L, result.getValue());
    }

    @Test
    @DisplayName("Должен находить максимальную длительность среди нескольких сессий")
    void applyShouldFindMaximumDurationAmongMultipleSessions() {
        List<SleepingSession> data = List.of(
            TestUtil.createSession("2026-05-11T23:00:00", "2026-05-12T06:00:00", Quality.NORMAL),
            TestUtil.createSession("2026-05-12T22:00:00", "2026-05-13T07:00:00", Quality.GOOD),
            TestUtil.createSession("2026-05-13T23:30:00", "2026-05-14T04:30:00", Quality.BAD)
        );
        SleepAnalysisResult result = analytic.apply(data);

        assertEquals(540L, result.getValue());
    }
}
