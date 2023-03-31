package ru.javarush.zakharov.island_game.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.javarush.zakharov.island_game.field.Cell;
import ru.javarush.zakharov.island_game.services.AnimalService;
import ru.javarush.zakharov.island_game.services.StatisticsService;
import ru.javarush.zakharov.island_game.util.ConstructorUtil;

@Slf4j
@Getter
@ToString(exclude = {"weight", "maxCountTypeOnOneCell", "maxStepSpeed", "satiety", "MaxSatiety", "isSexMale", "isDead"})
public abstract class Animal implements AllCharactersAction {

    private final String name;
    private final double weight;
    private final int maxCountTypeOnOneCell;
    private final int maxStepSpeed;
    @Setter
    private double satiety;
    private final double MaxSatiety;
    private final boolean isSexMale;
    @Setter
    private Cell cell;
    @Setter
    private boolean isDead = false;

    public Animal() {
        this.name = this.getClass().getSimpleName();
        this.weight = Double.parseDouble(ConstructorUtil.get(name + "weight"));
        this.maxCountTypeOnOneCell = Integer.parseInt(ConstructorUtil.get(name + "maxCountTypeOnOneCell"));
        this.maxStepSpeed = Integer.parseInt(ConstructorUtil.get(name + "stepSpeed"));
        this.MaxSatiety = Double.parseDouble(ConstructorUtil.get(name + "MaxSatiety"));
        this.satiety = getMaxSatiety();
        this.isSexMale = (int) (Math.random() * 2) == 0;
    }


    @Override
    public void eat() {
        AnimalService.eatFood(this);
    }

    @Override
    // размножаться
    public void multiply() {
        AnimalService.multiply(this);
    }

    @Override
    public void move() {
        AnimalService.move(this);
    }

    @Override
    public void run() {
        while (!isDead) {
            synchronized (this.getCell()) {
                if (isDead) break;
                eat();
                if (isDead) break;
                multiply();
                if (isDead) break;
                move();
            }
        }
        log.info("Всего на поле животных: " + StatisticsService.fieldStatisticsAnimals.get() + " и растений: " + StatisticsService.fieldStatisticsPlants.get());
    }
}