package ru.javarush.zakharov.island.game.services;

import lombok.experimental.UtilityClass;
import ru.javarush.zakharov.island.game.field.Cell;
import ru.javarush.zakharov.island.game.util.PropertiesUtil;

@UtilityClass
public class PlantService extends GameService {

    public static void multiply(ru.javarush.zakharov.island.game.entity.Plant plant) {
        if (Math.random() < Double.parseDouble(PropertiesUtil.get("MultiplyPercent")) / 100 && isCellNotFill(plant)) {
            Cell cell = plant.getCell();
            ru.javarush.zakharov.island.game.entity.Plant entity = (ru.javarush.zakharov.island.game.entity.Plant) createEntity(plant.getName());
            addAnimalOrPlantToNewCell(cell, entity);
            setDiamond(plant).start();
            Statistics.fieldStatisticsPlants.incrementAndGet();
        }
    }

    public static Thread setDiamond(Runnable plant) {
        final Thread thread = new Thread(plant);
        thread.setDaemon(true);
        return thread;
    }
}
