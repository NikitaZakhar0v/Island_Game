package ru.javarush.zakharov.island_game.services;

import lombok.extern.slf4j.Slf4j;
import ru.javarush.zakharov.island_game.entity.AllCharactersAction;
import ru.javarush.zakharov.island_game.entity.Plant;
import ru.javarush.zakharov.island_game.field.Cell;
import ru.javarush.zakharov.island_game.field.Field;
import ru.javarush.zakharov.island_game.util.EntityUtil;
import ru.javarush.zakharov.island_game.util.PropertiesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class FieldService {
    private static final Field field = Field.getInstance();
    private static final int row = Integer.parseInt(PropertiesUtil.get("FieldRow"));
    private static final int col = Integer.parseInt(PropertiesUtil.get("FieldCol"));
    static List<Thread> threads = new ArrayList<>();

    public static void fillFieldByCells() {
        field.setCells(new Cell[row][col]);
        for (int i = 0; i < field.getCells().length; i++) {
            for (int j = 0; j < field.getCells()[i].length; j++) {
                field.getCells()[i][j] = new Cell(i, j);
            }
        }
        log.debug("Ячейки созданы");
    }

    public static void fillFieldByGameCharacter() {
        List<String> allEntity = EntityUtil.getAllEntity();
        for (String entityName : allEntity) {
            for (int i = 0; i < EntityUtil.getCount(entityName); i++) {
                AllCharactersAction entity = GameService.createEntity(entityName);
                putToRandomCell(entity);
                if (entity.getCell() != null) {
                    putToThreadsList(entity);
                }
            }
        }
        log.debug("Поле заполнено героями");
    }

    private static void putToRandomCell(AllCharactersAction animal) {
        int randomRow = (int) (Math.random() * row);
        int randomCol = (int) (Math.random() * col);
        Cell cell = field.getCells()[randomRow][randomCol];

        if (isCellFill(animal, cell)) {
            log.debug("Для животного " + animal.getName() + " не нашлось места в ячейке " +
                    "" + cell + " пытаемся положить в свободную ячейку");
            tryPutToAnotherCell(animal);
        }
    }

    static  boolean isCellFill(AllCharactersAction animal, Cell cell) {
        if ((int) cell.getList().stream()
                .filter(gameProcess -> gameProcess.equals(animal))
                .count() == animal.getMaxCountTypeOnOneCell()){
            return true;
        } else {
            animal.setCell(cell);
            cell.getList().add(animal);
            return false;
        }
    }

    private static void tryPutToAnotherCell(AllCharactersAction animal) {
        for (Cell[] cells : field.getCells()) {
            if (animal.getCell() != null) return;
            Optional<Cell> cellOptional = Arrays.stream(cells)
                    .findFirst()
                    .filter(cell -> !isCellFill(animal, cell));
            if (cellOptional.isPresent()) {
                animal.setCell(cellOptional.get());
                animal.getCell().getList().add(animal);
                log.debug("Нашлось место в ячейке: " + animal.getCell());
            }
        }
        log.debug("Свободного места на поле для " + animal.getName() + " не осталось");
    }

    private static void putToThreadsList(AllCharactersAction entity) {
        if (entity instanceof Plant) {
            threads.add(PlantService.setDiamond(entity));
            StatisticsService.fieldStatisticsPlants.incrementAndGet();
        } else {
            threads.add(new Thread(entity));
            StatisticsService.fieldStatisticsAnimals.incrementAndGet();
        }
    }

}