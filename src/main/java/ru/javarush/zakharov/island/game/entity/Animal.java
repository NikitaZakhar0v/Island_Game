package ru.javarush.zakharov.island.game.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.javarush.zakharov.island.game.field.Cell;
import ru.javarush.zakharov.island.game.services.AnimalService;
import ru.javarush.zakharov.island.game.services.Statistics;
import ru.javarush.zakharov.island.game.util.ConstructorUtil;

import java.util.Random;

@Slf4j
@Getter
@ToString(exclude = {"weight", "maxCountTypeOnOneCell", "maxStepSpeed", "satiety", "maxSatiety", "isSexMale", "isDead"})
public abstract class Animal implements AllCharactersAction {

    private final String name;
    private final double weight;
    private final int maxCountTypeOnOneCell;
    private final int maxStepSpeed;
    @Setter
    private double satiety;
    private final double maxSatiety;
    private final boolean isSexMale;
    @Setter
    private Cell cell;
    @Setter
    private boolean isDead = false;

    protected Animal() {
        this.name = this.getClass().getSimpleName();
        this.weight = Double.parseDouble(ConstructorUtil.get(name + "weight"));
        this.maxCountTypeOnOneCell = Integer.parseInt(ConstructorUtil.get(name + "maxCountTypeOnOneCell"));
        this.maxStepSpeed = Integer.parseInt(ConstructorUtil.get(name + "stepSpeed"));
        this.maxSatiety = Double.parseDouble(ConstructorUtil.get(name + "MaxSatiety"));
        this.satiety = getMaxSatiety();
        this.isSexMale = new Random().nextInt(2) == 0;
    }


    @Override
    public void eat() {
        AnimalService.eatFood(this);
    }

    @Override
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
                if (!isDead) {
                    eat();
                }
                if (!isDead) {
                    multiply();
                }
                if (!isDead) {
                    move();
                }
            }
        }
        log.info("Всего на поле животных: " + Statistics.fieldStatisticsAnimals.get() + " и растений: " + Statistics.fieldStatisticsPlants.get());
    }
}