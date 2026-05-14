package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.BadQualityCounter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BadQualityCounterTest {

    private final BadQualityCounter analytic = new BadQualityCounter();

    @Test
    @DisplayName("Должен возвращать 0, если список сессий пуст")
    void applyShouldReturnZeroWhenDataIsEmptyForBadQualityCounter() {
        List<SleepingSession> emptyList = new ArrayList<>();
        SleepAnalysisResult<?> result = analytic.apply(emptyList);

        assertNotNull(result);
        assertEquals("Количество сессий с плохим качеством сна", result.getDescription());
        assertEquals(0L, result.getValue());
    }

    @Test
    @DisplayName("Должен возвращать 0, если в списке нет сессий с плохим качеством")
    void applyShouldReturnZeroWhenNoBadQualitySessions() {
        List<SleepingSession> data = List.of(
            TestUtil.createSession("2026-05-12T23:30:00", "2026-05-13T09:30:00", Quality.GOOD),
            TestUtil.createSession("2026-05-13T23:15:00", "2026-05-14T10:00:00", Quality.GOOD)
        );
        SleepAnalysisResult<?> result = analytic.apply(data);

        assertEquals(0L, result.getValue());
    }

    @Test
    @DisplayName("Должен корректно подсчитывать только сессии с качеством BAD")
    void applyShouldCountOnlyBadQualitySessions() {
        List<SleepingSession> data = List.of(
            TestUtil.createSession("2026-05-13T23:15:00", "2026-05-14T04:00:00", Quality.BAD),
            TestUtil.createSession("2026-05-13T23:15:00", "2026-05-14T10:00:00", Quality.GOOD),
            TestUtil.createSession("2026-05-13T23:15:00", "2026-05-14T05:00:00", Quality.BAD),
            TestUtil.createSession("2026-05-13T23:15:00", "2026-05-14T11:00:00", Quality.GOOD),
            TestUtil.createSession("2026-05-13T23:15:00", "2026-05-14T02:00:00", Quality.BAD)
        );
        SleepAnalysisResult<?> result = analytic.apply(data);

        assertEquals(3L, result.getValue());
    }
}
