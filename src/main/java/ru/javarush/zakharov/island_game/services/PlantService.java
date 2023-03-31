package ru.javarush.zakharov.island_game.services;

import ru.javarush.zakharov.island_game.entity.Plant;
import ru.javarush.zakharov.island_game.field.Cell;
import ru.javarush.zakharov.island_game.util.PropertiesUtil;


public class PlantService extends GameService {

    public static void multiply(Plant plant) {
        if (Math.random() < Double.parseDouble(PropertiesUtil.get("MultiplyPercent")) / 100 && isCellNotFill(plant)) {
            Cell cell = plant.getCell();
            Plant entity = (Plant) createEntity(plant.getName());
            addAnimalOrPlantToNewCell(cell, entity);
            setDiamond(plant).start();
            StatisticsService.fieldStatisticsPlants.incrementAndGet();
        }
    }

    public static Thread setDiamond(Runnable plant) {
        final Thread thread = new Thread(plant);
        thread.setDaemon(true);
        return thread;
    }

    public static void stub() {

    }
}
