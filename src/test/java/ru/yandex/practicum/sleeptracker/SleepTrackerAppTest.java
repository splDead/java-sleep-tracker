package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yandex.practicum.sleeptracker.functions.SessionCounter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SleepTrackerAppTest {

    private final PrintStream standardOut = System.out;
    private final PrintStream standardErr = System.err;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(errorStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
        System.setErr(standardErr);
    }

    @Test
    @DisplayName("Проверка запуска: без аргумента")
    void mainShouldPrintUsageMessageWhenNoArgumentsProvided() {
        SleepTrackerApp.main(new String[]{});
        String expectedMessage = "Использование: укажите путь к файлу логов в качестве аргумента";
        assertTrue(outputStreamCaptor.toString().trim().contains(expectedMessage));
    }

    @Test
    @DisplayName("Проверка запуска: несуществующий файл")
    void mainShouldPrintErrorMessageWhenFileNotFound() {
        SleepTrackerApp.main(new String[]{"non_existent_file.txt"});
        assertTrue(errorStreamCaptor.toString().trim().contains("Не удалось прочитать файл"));
    }

    @Test
    @DisplayName("Проверка запуска: файл с моковыми данными")
    void mainShouldProcessValidFileAndPrintResultsWhenValidDataProvided(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("sleep_log.txt");

        // мокаем данные с ошибками
        List<String> lines = List.of(
                "03.10.25 23:40;04.10.25 08:00;BAD",
                "",
                "10.10.25 23:55;11.10.25 06:10;GOOD",
                "invalid_line_without_semicolon"
        );
        Files.write(tempFile, lines);

        SleepTrackerApp.main(new String[]{tempFile.toAbsolutePath().toString()});

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Результаты анализа сна"));

        // создаем приложение и приверяем счетчик сессий
        SleepTrackerApp app = new SleepTrackerApp();
        app.register(new SessionCounter());

        List<String> sleepRawData = Files.readAllLines(tempFile);
        List<SleepingSession> sleepingSessions = sleepRawData.stream()
            .filter(line -> !line.isBlank())
            .filter(line -> line.contains(";"))
            .map(SleepingSession::parse)
            .toList();

        app.getAnalyzers().stream()
            .map(func -> func.apply(sleepingSessions))
            .forEach(System.out::println);

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("Общее количество сессий сна: 2"));
    }
}
