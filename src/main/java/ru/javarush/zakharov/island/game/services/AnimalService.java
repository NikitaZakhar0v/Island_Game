package ru.javarush.zakharov.island.game.services;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.javarush.zakharov.island.game.entity.AllCharactersAction;
import ru.javarush.zakharov.island.game.entity.Animal;
import ru.javarush.zakharov.island.game.field.Cell;
import ru.javarush.zakharov.island.game.field.Field;
import ru.javarush.zakharov.island.game.util.ConstructorUtil;
import ru.javarush.zakharov.island.game.util.PropertiesUtil;

import java.util.List;
import java.util.Random;

@Slf4j
@UtilityClass
public class AnimalService extends GameService {
     private static final Random random = new Random();
    /**
     * Передвижение животного на новую клетку
     *
     * @param animal животное, которое будет или не будет перемещаться
     */
    public static void move(ru.javarush.zakharov.island.game.entity.Animal animal) {
        if (isAnimalDead(animal)) return;
        Cell currentCell = animal.getCell();
        Cell newCell = chooseNewCell(animal.getMaxStepSpeed(), currentCell);

        removeEntityFromCell(animal);
        addAnimalOrPlantToNewCell(newCell, animal);
        animal.setSatiety(animal.getSatiety() - 1);
    }

    private static boolean isAnimalDead(ru.javarush.zakharov.island.game.entity.Animal animal) {
        if (animal.getSatiety() < 0) {
            log.info("Погиб от голода: " + animal);
            die(animal);
            return true;
        }
        return false;
    }

    /**
     * Выбор новой ячейки на рандомный шаг от 0 до animal.getStepSpeed() от текущей ячейки.
     *
     * @param maxStepSpeed - максимально возможный шаг за один ход.
     * @param currentCell  - текущая клетка, в которой находится животное
     * @return ячейка в которую в дальнейшем будет перемещено животное
     */
    private static Cell chooseNewCell(int maxStepSpeed, Cell currentCell) {
        final Cell[][] cells = Field.getInstance().getCells();
        int bound = maxStepSpeed == 0 ? 1 : maxStepSpeed + 1;
        int randomMoveSpeed = random.nextInt(bound);
        int row = getNewRowOrNewCol(randomMoveSpeed, currentCell.getRow(), cells.length);
        int col = getNewRowOrNewCol(randomMoveSpeed, currentCell.getCol(), cells[currentCell.getRow()].length);

        return cells[row][col];
    }

    private static int getNewRowOrNewCol(int randomMoveSpeed, int currentRowOrCol, int cellsLength) {
        int rowOrCol = -1;
        while (rowOrCol < 0 || rowOrCol >= cellsLength) {
            int bound = randomMoveSpeed == 0 ? 1 : randomMoveSpeed * 2;
            rowOrCol = currentRowOrCol + random.nextInt(bound) - randomMoveSpeed;
        }
        return rowOrCol;
    }


    /**
     * Размножение животных, путем поиска подходящей пары в текущей ячейке
     *
     * @param animal животное, которое ищет пару
     */
    public static void multiply(Animal animal) {
        try {
            if (isAnimalDead(animal)) return;
            List<AllCharactersAction> entityList = animal.getCell().getList();
            for (int i = 0; i < entityList.size(); i++) {
                var animalOrPlants = entityList.get(i);
                if (checkForCompatibility(animal, animalOrPlants) && isMultiply() && isCellNotFill(animal)) {
                    Animal newAnimal = (Animal) createEntity("animal." + animal.getName());
                    addAnimalOrPlantToNewCell(animal.getCell(), newAnimal);
                    new Thread(newAnimal).start();
                    animal.setSatiety(animal.getSatiety() - 1);
                    ((Animal) animalOrPlants).setSatiety(((Animal) animalOrPlants).getSatiety() - 1);
                    Statistics.fieldStatisticsAnimals.incrementAndGet();
                    log.info("На свет появился: " + animal);
                    return;
                }
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }

    /**
     * Метод зависит от MultiplyPercent указанного в application.properties
     *
     * @return разрешает/не разрешает размножаться
     */
    private static boolean isMultiply() {
        return Math.random() < Double.parseDouble(PropertiesUtil.get("MultiplyPercent")) / 100;
    }

    /**
     * Проверка на совместимость, являются ли животные одного типа и разного пола
     *
     * @param oneAnimal    животное, которое ищет
     * @param secondAnimal животное, которое нашли
     * @return результат проверки.
     */
    private static boolean checkForCompatibility(ru.javarush.zakharov.island.game.entity.Animal oneAnimal, AllCharactersAction secondAnimal) {
            if (oneAnimal.getClass().equals(secondAnimal.getClass())) {
                return oneAnimal.isSexMale() != ((ru.javarush.zakharov.island.game.entity.Animal) secondAnimal).isSexMale();
            }
        return false;
    }

    /**
     * Поиск еды
     *
     * @param animal животное
     */
    public static void eatFood(ru.javarush.zakharov.island.game.entity.Animal animal) {
        if (animal.getSatiety() == animal.getMaxSatiety()) return;
        List<AllCharactersAction> entityList = animal.getCell().getList();
        for (int i = 0; i < entityList.size(); i++) {
            if (animal.getSatiety() == animal.getMaxSatiety()) return;
            final AllCharactersAction entity = entityList.get(i);
            final String percent = ConstructorUtil.get(animal.getName() + entity.getName());
            if (Double.parseDouble(percent) > Math.random() * 100 && !entity.isDead()) {
                log.info(animal.getName() + " съел " + entity.getName());
                    die(entity);
                animal.setSatiety(animal.getSatiety() + 3);
                if (animal.getSatiety() > animal.getMaxSatiety()) animal.setSatiety(animal.getMaxSatiety());
                i--;
            }
        }
    }
}
