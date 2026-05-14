package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.SleeplessNightAnalytic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SleeplessNightAnalyticTest {

    private final SleeplessNightAnalytic analytic = new SleeplessNightAnalytic();

    @Test
    @DisplayName("Должен возвращать 0 бессонных ночей для пустого списка")
    void applyShouldReturnZeroWhenDataIsEmptyForSleeplessNightAnalytic() {
        List<SleepingSession> emptyList = new ArrayList<>();
        SleepAnalysisResult<?> result = analytic.apply(emptyList);

        assertNotNull(result);
        assertEquals("Количество бессонных ночей", result.getDescription());
        assertEquals(0, result.getValue());
    }

    @Test
    @DisplayName("Должен возвращать 0 бессонных ночей, если во все ночи периода был сон")
    void applyShouldReturnZeroWhenEveryNightHasSleepSession() {
        List<SleepingSession> data = List.of(
            TestUtil.createSession("2026-05-11T23:00:00", "2026-05-12T07:00:00", Quality.GOOD),
            TestUtil.createSession("2026-05-12T23:00:00", "2026-05-13T07:00:00", Quality.GOOD)
        );
        SleepAnalysisResult<?> result = analytic.apply(data);

        assertEquals(0L, result.getValue());
    }

    @Test
    @DisplayName("Должен находить бессонную ночь, если между сессиями есть пропущенный интервал 00:00-06:00")
    void applyShouldCountSleeplessNightWhenGapExistsBetweenSessions() {
        List<SleepingSession> data = List.of(
            TestUtil.createSession("2026-05-11T23:00:00", "2026-05-12T07:00:00", Quality.GOOD),
            TestUtil.createSession("2026-05-13T23:00:00", "2026-05-14T07:00:00", Quality.GOOD)
        );
        SleepAnalysisResult<?> result = analytic.apply(data);

        assertEquals(1L, result.getValue());
    }

    @Test
    @DisplayName("Должен считать ночь бессонной, если сон не пересекался с интервалом 00:00-06:00 (дневной сон)")
    void applyShouldCountSleeplessNightWhenSleepIsOnlyDuringDaytime() {
        List<SleepingSession> data = List.of(
            TestUtil.createSession("2026-05-12T07:00:00", "2026-05-12T11:00:00", Quality.GOOD),
            TestUtil.createSession("2026-05-12T13:00:00", "2026-05-12T17:00:00", Quality.NORMAL)
        );
        SleepAnalysisResult<?> result = analytic.apply(data);

        assertEquals(1L, result.getValue());
    }

    @Test
    @DisplayName("Должен учитывать ночь как поспанную при частичном пересечении с интервалом (например, проснулся в 02:00)")
    void applyShouldNotCountAsSleeplessWhenSessionPartiallyOverlapsNightInterval() {
        List<SleepingSession> data = List.of(
            TestUtil.createSession("2026-05-11T22:00:00", "2026-05-12T02:00:00", Quality.BAD)
        );
        SleepAnalysisResult<?> result = analytic.apply(data);

        assertEquals(0L, result.getValue());
    }
}
