package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateUtilTest {

    @Test
    @DisplayName("Время до полудня должно возвращать полночь текущего дня")
    void getNightStartShouldReturnCurrentDayMidnightWhenTimeIsBeforeNoon() {
        // ровно полночь
        assertEquals(LocalDateTime.parse("2026-05-13T00:00:00"),
                DateUtil.getNightStart(LocalDateTime.parse("2026-05-13T00:00:00")));

        // раннее утро
        assertEquals(LocalDateTime.parse("2026-05-13T00:00:00"),
                DateUtil.getNightStart(LocalDateTime.parse("2026-05-13T05:30:00")));

        // за секунду до полудня
        assertEquals(LocalDateTime.parse("2026-05-13T00:00:00"),
                DateUtil.getNightStart(LocalDateTime.parse("2026-05-13T11:59:59")));
    }

    @Test
    @DisplayName("Время в полдень и после полудня должно возвращать полночь следующего дня")
    void getNightStartShouldReturnNextDayMidnightWhenTimeIsAtOrAfterNoon() {
        // ровно полдень
        assertEquals(LocalDateTime.parse("2026-05-14T00:00:00"),
                DateUtil.getNightStart(LocalDateTime.parse("2026-05-13T12:00:00")));

        // день
        assertEquals(LocalDateTime.parse("2026-05-14T00:00:00"),
                DateUtil.getNightStart(LocalDateTime.parse("2026-05-13T14:15:00")));

        // за секунду до конца дня
        assertEquals(LocalDateTime.parse("2026-05-14T00:00:00"),
                DateUtil.getNightStart(LocalDateTime.parse("2026-05-13T23:59:59")));
    }

    @Test
    @DisplayName("Должен корректно обрабатываться переход месяцев и лет при сдвиге на следующий день")
    void getNightStartShouldHandleMonthAndYearTransitionCorrectly() {
        LocalDateTime newYearEve = LocalDateTime.parse("2026-12-31T22:00:00");
        LocalDateTime expectedNewYear = LocalDateTime.parse("2027-01-01T00:00:00");

        LocalDateTime actualResult = DateUtil.getNightStart(newYearEve);

        assertEquals(expectedNewYear, actualResult, "Сдвиг даты при переходе года отработал неверно");
    }
}
