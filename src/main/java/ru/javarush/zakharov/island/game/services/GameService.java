package ru.javarush.zakharov.island.game.services;

import lombok.extern.slf4j.Slf4j;
import ru.javarush.zakharov.island.game.entity.AllCharactersAction;
import ru.javarush.zakharov.island.game.entity.Animal;
import ru.javarush.zakharov.island.game.field.Cell;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public abstract class GameService {
    GameService() {
    }

    public static void start() {
        FieldService.fillFieldByCells();
        FieldService.fillFieldByGameCharacter();
        log.debug("Создано животных: " + Statistics.fieldStatisticsAnimals.get() + " и растений: " + Statistics.fieldStatisticsPlants.get());
        FieldService.threads.forEach(Thread::start);
    }

    public static AllCharactersAction createEntity(String className) {
        AllCharactersAction entity = null;
        try {
            entity = (AllCharactersAction) Class.forName("ru.javarush.zakharov.island.game.entity." + className).getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return entity;
    }

    static void removeEntityFromCell(AllCharactersAction animalOrPlant) {
        animalOrPlant.getCell().getList().remove(animalOrPlant);
        animalOrPlant.setCell(null);
    }


    /**
     * Метод удаляет все ссылки на выбранное животное или растение.
     *
     * @param animalOrPlant животное или растение, которое помечается как мертвое.
     */
    static void die(AllCharactersAction animalOrPlant) {
        Cell cell = animalOrPlant.getCell();
        removeEntityFromCell(animalOrPlant);
        animalOrPlant.setDead(true);
        if (animalOrPlant instanceof Animal) Statistics.fieldStatisticsAnimals.decrementAndGet();
        else Statistics.fieldStatisticsPlants.decrementAndGet();
        log.info(Statistics.showStatisticsByCell(cell));
    }

    static void addAnimalOrPlantToNewCell(Cell newCell, AllCharactersAction animalOrPlant) {
        newCell.getList().add(animalOrPlant);
        animalOrPlant.setCell(newCell);
    }

    static boolean isCellNotFill(AllCharactersAction animalOrPlant) {
        int count = (int) animalOrPlant.getCell().getList().stream()
                .filter(gameProcess -> gameProcess.equals(animalOrPlant))
                .count();
        return count < animalOrPlant.getMaxCountTypeOnOneCell();
    }
}
