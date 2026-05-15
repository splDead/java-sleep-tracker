package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.GoodQualityCounter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GoodQualityCounterTest {

    private final GoodQualityCounter analytic = new GoodQualityCounter();

    @Test
    @DisplayName("Должен возвращать 0, если список сессий пуст")
    void applyShouldReturnZeroWhenDataIsEmptyForGoodQualityCounter() {
        List<SleepingSession> emptyList = new ArrayList<>();
        SleepAnalysisResult<Long> result = analytic.apply(emptyList);

        assertNotNull(result);
        assertEquals("Количество сессий с хорошим качеством сна", result.getDescription());
        assertEquals(0L, result.getValue());
    }

    @Test
    @DisplayName("Должен возвращать 0, если в списке нет сессий с плохим качеством")
    void applyShouldReturnZeroWhenNoGoodQualitySessions() {
        List<SleepingSession> data = List.of(
            TestUtil.createSession("2026-05-12T23:30:00", "2026-05-13T09:30:00", Quality.NORMAL),
            TestUtil.createSession("2026-05-13T23:15:00", "2026-05-14T10:00:00", Quality.BAD)
        );
        SleepAnalysisResult<Long> result = analytic.apply(data);

        assertEquals(0L, result.getValue());
    }

    @Test
    @DisplayName("Должен корректно подсчитывать только сессии с качеством GOOD")
    void applyShouldCountOnlyGoodQualitySessions() {
        List<SleepingSession> data = List.of(
            TestUtil.createSession("2026-05-13T23:15:00", "2026-05-14T04:00:00", Quality.BAD),
            TestUtil.createSession("2026-05-13T23:15:00", "2026-05-14T10:00:00", Quality.GOOD),
            TestUtil.createSession("2026-05-13T23:15:00", "2026-05-14T05:00:00", Quality.BAD),
            TestUtil.createSession("2026-05-13T23:15:00", "2026-05-14T11:00:00", Quality.GOOD),
            TestUtil.createSession("2026-05-13T23:15:00", "2026-05-14T02:00:00", Quality.BAD)
        );
        SleepAnalysisResult<Long> result = analytic.apply(data);

        assertEquals(2L, result.getValue());
    }
}
