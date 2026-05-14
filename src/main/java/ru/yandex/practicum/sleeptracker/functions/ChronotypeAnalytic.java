package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.Chronotype;
import ru.yandex.practicum.sleeptracker.DateUtil;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeAnalytic implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> data) {
        // считаем счетчики хронотипов
        Map<Chronotype, Long> counts = data.stream()
            .filter(this::isNightSession) // фильтруем дневной сон
            .map(this::determineChronotype) // определяем хронотип
            .collect(Collectors.groupingBy(type -> type, Collectors.counting()));

        long owls = counts.getOrDefault(Chronotype.OWL, 0L);
        long larks = counts.getOrDefault(Chronotype.LARK, 0L);
        long pigeons = counts.getOrDefault(Chronotype.PIGEON, 0L);

        Chronotype finalType;
        if (owls > larks && owls > pigeons) {
            finalType = Chronotype.OWL;
        } else if (larks > owls && larks > pigeons) {
            finalType = Chronotype.LARK;
        } else {
            finalType = Chronotype.PIGEON;
        }

        return new SleepAnalysisResult<>("Ваш хронотип", finalType);
    }

    private boolean isNightSession(SleepingSession s) {
        LocalDateTime start = s.getStart();
        LocalDateTime end = s.getEnd();

        // считаем ночью с полуночи до 6 утра
        LocalDateTime nightStart = DateUtil.getNightStart(start);
        LocalDateTime nightEnd = nightStart.plusHours(6);

        // сессия ночная, если она пересекается с интервалом 00:00 - 06:00:
        // 1. началась до полуночи, закончилась после 6 утра
        // 2. началась до полуночи, закончилась после полуночи и раньше 6 утра
        // 3. началась после полуночи, закончилась после 6 утра
        // 4. началась после полуночи, закончилась до 6 утра
        boolean fullNight = start.isBefore(nightStart) && (end.isAfter(nightEnd) || end.isEqual(nightEnd));
        boolean beforeMidnight = start.isBefore(nightStart) && end.isAfter(nightStart) && end.isBefore(nightEnd);
        boolean afterMidnight = (start.isAfter(nightStart) || start.isEqual(nightStart)) && (end.isAfter(nightEnd) || end.isEqual(nightEnd));
        boolean middleNight = (start.isAfter(nightStart) || start.isEqual(nightStart)) && end.isBefore(nightEnd);
        return fullNight || beforeMidnight ||  afterMidnight || middleNight;
    }

    private Chronotype determineChronotype(SleepingSession s) {
        LocalTime start = s.getStart().toLocalTime();
        LocalTime end = s.getEnd().toLocalTime();

        // сова: засыпание после 23 вечера и пробуждение после 9 утра
        if (start.isAfter(LocalTime.of(23, 0)) && end.isAfter(LocalTime.of(9, 0))) {
            return Chronotype.OWL;
        }

        // жаворонок: засыпание в интервале 18 - 22 и пробуждение раньше 7 утра
        if (start.isAfter(LocalTime.of(18, 0)) && start.isBefore(LocalTime.of(22, 0)) && end.isAfter(LocalTime.of(0, 0)) && end.isBefore(LocalTime.of(7, 0))) {
            return Chronotype.LARK;
        }

        return Chronotype.PIGEON;
    }
}
