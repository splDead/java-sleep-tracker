package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.SessionCounter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SessionCounterTest {

    private final SessionCounter analytic = new SessionCounter();

    @Test
    @DisplayName("Должен возвращать 0, если список сессий пуст")
    void applyShouldReturnZeroWhenDataIsEmptyForSessionCounter() {
        List<SleepingSession> emptyList = new ArrayList<>();
        SleepAnalysisResult result = analytic.apply(emptyList);

        assertNotNull(result);
        assertEquals("Общее количество сессий сна", result.getDescription());
        assertEquals(0, result.getValue());
    }

    @Test
    @DisplayName("Должен возвращать корректный размер заполненного списка сессий")
    void applyShouldReturnCorrectSizeWhenDataIsNotEmpty() {
        List<SleepingSession> data = List.of(
            TestUtil.createSession("2026-05-11T22:00:00", "2026-05-12T06:00:00", Quality.NORMAL),
            TestUtil.createSession("2026-05-12T23:00:00", "2026-05-13T04:00:00", Quality.BAD),
            TestUtil.createSession("2026-05-13T21:30:00", "2026-05-14T06:30:00", Quality.GOOD)
        );
        SleepAnalysisResult result = analytic.apply(data);

        assertEquals(3, result.getValue());
    }
}
