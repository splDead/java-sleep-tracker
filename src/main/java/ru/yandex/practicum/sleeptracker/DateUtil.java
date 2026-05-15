package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtil {
    public static LocalDateTime getNightStart(LocalDateTime start) {
        LocalDateTime nightStart;

        // если начало сессии началось до 12, то считаем полночь текущего дня
        if (start.toLocalTime().isBefore(LocalTime.NOON)) {
            nightStart = start.toLocalDate().atStartOfDay();
        } else {
            // если после 12, то считаем полночь от следующего дня
            nightStart = start.toLocalDate().plusDays(1).atStartOfDay();
        }

        return nightStart;
    }
}
