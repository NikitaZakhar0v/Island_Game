package ru.javarush.zakharov.island_game.services;

import ru.javarush.zakharov.island_game.field.Cell;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticsService {
    public static AtomicInteger fieldStatisticsAnimals = new AtomicInteger(0);
    public static AtomicInteger fieldStatisticsPlants = new AtomicInteger(0);

    public static HashMap<String, Integer> getStatisticsByCell(Cell cell) {
        final HashMap<String, Integer> map = new HashMap<>();

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
        HashMap<String, Integer> map = getStatisticsByCell(cell);
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
