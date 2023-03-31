package ru.javarush.zakharov.island_game.services;

import lombok.extern.slf4j.Slf4j;
import ru.javarush.zakharov.island_game.entity.Animal;
import ru.javarush.zakharov.island_game.entity.AllCharactersAction;
import ru.javarush.zakharov.island_game.field.Cell;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public abstract class GameService {


    public static void start() {
        FieldService.fillFieldByCells();
        FieldService.fillFieldByGameCharacter();
        log.debug("Создано животных: " + StatisticsService.fieldStatisticsAnimals.get() + " и растений: " + StatisticsService.fieldStatisticsPlants.get());
        FieldService.threads.forEach(Thread::start);
    }

    public static AllCharactersAction createEntity(String className) {
        AllCharactersAction entity = null;
        try {
            entity = (AllCharactersAction) Class.forName("ru.javarush.zakharov.island_game.entity." + className).getConstructors()[0].newInstance();
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
        if (animalOrPlant instanceof Animal) StatisticsService.fieldStatisticsAnimals.decrementAndGet();
        else StatisticsService.fieldStatisticsPlants.decrementAndGet();
            log.info(StatisticsService.showStatisticsByCell(cell));
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
