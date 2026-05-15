package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SleepingSession {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Quality quality;

    public SleepingSession(LocalDateTime start, LocalDateTime finish, Quality quality) {
        this.start = start;
        this.end = finish;
        this.quality = quality;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Quality getQuality() {
        return quality;
    }

    public static SleepingSession parse(String line) {
        String[] parts = line.split(";"); // Или ",", смотря какой разделитель в файле

        return new SleepingSession(
                LocalDateTime.parse(parts[0].trim(), FORMATTER),
                LocalDateTime.parse(parts[1].trim(), FORMATTER),
                Quality.valueOf(parts[2].trim().toUpperCase())
        );
    }

    @Override
    public String toString() {
        return "SleepingSession{" +
                "start=" + start.format(FORMATTER) +
                ", finish=" + end.format(FORMATTER) +
                ", quality=" + quality +
                '}';
    }
}
