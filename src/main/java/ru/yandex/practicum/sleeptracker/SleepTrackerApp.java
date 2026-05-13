package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.functions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {
    // список функций анализаторов
    private final List<Function<List<SleepingSession>, SleepAnalysisResult>> analyzers = new ArrayList<>();

    public void register(Function<List<SleepingSession>, SleepAnalysisResult> function) {
        analyzers.add(function);
    }

    public List<Function<List<SleepingSession>, SleepAnalysisResult>> getAnalyzers() {
        return analyzers;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Использование: укажите путь к файлу логов в качестве аргумента");
            return;
        }

        String filePath = args[0];

        try {
            // создаем приложение
            SleepTrackerApp app = new SleepTrackerApp();

            // регистрируем функции анализаторы
            app.register(new SessionCounter());
            app.register(new GoodQualityCounter());
            app.register(new BadQualityCounter());
            app.register(new MinDurationAnalytic());
            app.register(new MaxDurationAnalytic());
            app.register(new AverageDurationAnalytic());
            app.register(new SleeplessNightAnalytic());
            app.register(new ChronotypeAnalytic());

            // чтение сразу всего файла для избежания использования цикла while
            List<String> sleepRawData = Files.readAllLines(Paths.get(filePath));

            // создаем список для анализа
            List<SleepingSession> sleepingSessions = sleepRawData.stream()
                .filter(line -> !line.isBlank())
                .filter(line -> line.contains(";"))
                .map(SleepingSession::parse)
                .toList();

            System.out.println("Результаты анализа сна");

            app.analyzers.stream()
                .map(func -> func.apply(sleepingSessions))
                .forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Не удалось прочитать файл: " + e.getMessage());
        }
    }
}