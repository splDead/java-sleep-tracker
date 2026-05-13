package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.Quality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;

public class TestUtil {
    // вспомогательный метод для создания тестовой сессии
    public static SleepingSession createSession(String start, String end, Quality quality) {
        return new SleepingSession(LocalDateTime.parse(start), LocalDateTime.parse(end), quality);
    }
}
