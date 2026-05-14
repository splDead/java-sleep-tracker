package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.ChronotypeAnalytic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChronotypeAnalyticTest {

    private final ChronotypeAnalytic analytic = new ChronotypeAnalytic();

    @Test
    @DisplayName("Должен возвращать PIGEON для пустого списка сессий")
    void applyShouldReturnPigeonWhenDataIsEmpty() {
        SleepAnalysisResult<?> result = analytic.apply(new ArrayList<>());

        assertNotNull(result);
        assertEquals("Ваш хронотип", result.getDescription());
        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    @DisplayName("Должен определять OWL, если сессий совы строго больше остальных")
    void applyShouldReturnOwlWhenOwlsPredominate() {
        // сессия совы: засыпание после 23:00, подъем после 09:00
        SleepingSession owlSession1 = TestUtil.createSession("2026-05-12T23:30:00", "2026-05-13T09:30:00", Quality.GOOD);
        SleepingSession owlSession2 = TestUtil.createSession("2026-05-13T23:15:00", "2026-05-14T10:00:00", Quality.GOOD);
        // сессия жаворонка: засыпание до 22:00, подъем до 07:00
        SleepingSession larkSession = TestUtil.createSession("2026-05-12T21:00:00", "2026-05-13T06:00:00", Quality.BAD);
        SleepAnalysisResult<?> result = analytic.apply(List.of(owlSession1, owlSession2, larkSession));

        assertEquals(Chronotype.OWL, result.getValue());
    }

    @Test
    @DisplayName("Должен определять LARK, если сессий жаворонка строго больше остальных")
    void applyShouldReturnLarkWhenLarksPredominate() {
        SleepingSession larkSession1 = TestUtil.createSession("2026-05-12T21:00:00", "2026-05-13T06:00:00", Quality.GOOD);
        SleepingSession larkSession2 = TestUtil.createSession("2026-05-13T21:30:00", "2026-05-14T05:30:00", Quality.BAD);
        SleepingSession pigeonSession = TestUtil.createSession("2026-05-12T22:30:00", "2026-05-13T08:00:00", Quality.GOOD);
        SleepAnalysisResult<?> result = analytic.apply(List.of(larkSession1, larkSession2, pigeonSession));

        assertEquals(Chronotype.LARK, result.getValue());
    }

    @Test
    @DisplayName("Должен возвращать PIGEON при равном количестве сов и жаворонков")
    void applyShouldReturnPigeonWhenOwlsAndLarksAreEqual() {
        SleepingSession owlSession = TestUtil.createSession("2026-05-12T23:30:00", "2026-05-13T09:30:00", Quality.GOOD);
        SleepingSession larkSession = TestUtil.createSession("2026-05-12T21:00:00", "2026-05-13T06:00:00", Quality.NORMAL);
        SleepAnalysisResult<?> result = analytic.apply(List.of(owlSession, larkSession));

        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    @DisplayName("Должен игнорировать дневные сессии, не пересекающиеся с интервалом 00:00-06:00")
    void applyShouldIgnoreDaytimeSessions() {
        SleepingSession dayLarkSession = TestUtil.createSession("2026-05-13T13:00:00", "2026-05-13T16:00:00", Quality.GOOD);
        SleepingSession nightOwlSession = TestUtil.createSession("2026-05-12T23:30:00", "2026-05-13T09:30:00", Quality.GOOD);
        SleepAnalysisResult<?> result = analytic.apply(List.of(dayLarkSession, nightOwlSession));

        assertEquals(Chronotype.OWL, result.getValue());
    }

    @Test
    @DisplayName("Должен учитывать ночную сессию, если она началась после полуночи и закончилась до 6 утра")
    void applyShouldIncludeSessionWhenItIsInsideMiddleNightInterval() {
        SleepingSession middleNightSession = TestUtil.createSession("2026-05-13T01:00:00", "2026-05-13T05:00:00", Quality.BAD);
        SleepAnalysisResult<?> result = analytic.apply(List.of(middleNightSession));

        assertEquals(Chronotype.PIGEON, result.getValue());
    }
}
