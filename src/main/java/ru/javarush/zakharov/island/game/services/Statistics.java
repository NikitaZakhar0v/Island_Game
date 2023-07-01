package ru.javarush.zakharov.island.game.services;

import lombok.experimental.UtilityClass;
import ru.javarush.zakharov.island.game.field.Cell;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class Statistics {
    public static final AtomicInteger fieldStatisticsAnimals = new AtomicInteger(0);
    public static final AtomicInteger fieldStatisticsPlants = new AtomicInteger(0);

    public static Map<String, Integer> getStatisticsByCell(Cell cell) {
        final Map<String, Integer> map = new HashMap<>();

        cell.getList().forEach(gameProcess -> {
            final String simpleName = gameProcess.getName();
            if (!map.containsKey(simpleName)) {
                map.put(simpleName, 1);
            } else map.put(simpleName, map.get(simpleName) + 1);
        });
        return map;
    }

    public static String showStatisticsByCell(Cell cell) {
        StringBuilder sb = new StringBuilder();
        Map<String, Integer> map = getStatisticsByCell(cell);
        sb.append("\nВ ячейке ")
                .append(cell)
                .append(" находится:\n");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            sb.append(entry.getKey())
                    .append(" | ")
                    .append(entry.getValue())
                    .append(" шт. \n");
        }
        return sb.toString();
    }
}
