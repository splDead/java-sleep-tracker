package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.MinDurationAnalytic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MinDurationAnalyticTest {

    private final MinDurationAnalytic analytic = new MinDurationAnalytic();

    @Test
    @DisplayName("Должен возвращать 0, если список сессий пуст")
    void applyShouldReturnZeroWhenDataIsEmptyMinDurationAnalytic() {
        List<SleepingSession> emptyList = new ArrayList<>();
        SleepAnalysisResult result = analytic.apply(emptyList);

        assertNotNull(result);
        assertEquals("Минимальная продолжительность сессии (в минутах)", result.getDescription());
        assertEquals(0L, result.getValue());
    }

    @Test
    @DisplayName("Должен возвращать длительность единственной сессии")
    void applyShouldReturnDurationForSingleSessionForMinDurationAnalytic() {
        List<SleepingSession> data = List.of(TestUtil.createSession("2026-05-13T23:00:00", "2026-05-14T06:30:00", Quality.NORMAL));
        SleepAnalysisResult result = analytic.apply(data);

        assertEquals(450L, result.getValue());
    }

    @Test
    @DisplayName("Должен находить минимальную длительность среди нескольких сессий")
    void applyShouldFindMinimumDurationAmongMultipleSessions() {
        List<SleepingSession> data = List.of(
            TestUtil.createSession("2026-05-11T22:00:00", "2026-05-12T06:00:00", Quality.NORMAL),
            TestUtil.createSession("2026-05-12T23:00:00", "2026-05-13T04:00:00", Quality.BAD),
            TestUtil.createSession("2026-05-13T21:30:00", "2026-05-14T06:30:00", Quality.GOOD)
        );
        SleepAnalysisResult result = analytic.apply(data);

        assertEquals(300L, result.getValue());
    }
}
