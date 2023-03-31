package ru.javarush.zakharov.island_game.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.javarush.zakharov.island_game.field.Cell;
import ru.javarush.zakharov.island_game.services.PlantService;
import ru.javarush.zakharov.island_game.util.ConstructorUtil;

@Getter
@EqualsAndHashCode
@ToString
public class Plant implements AllCharactersAction {
    private final String name;
    private final double weight;
    private final int maxCountTypeOnOneCell;
    @Setter
    private Cell cell;
    @Setter
    private boolean isDead = false;

    public Plant() {
        this.name = this.getClass().getSimpleName();
        this.weight = Double.parseDouble(ConstructorUtil.get(name + "weight"));
        this.maxCountTypeOnOneCell = Integer.parseInt(ConstructorUtil.get(name + "maxCountTypeOnOneCell"));
    }

    @Override
    public void eat() {
        PlantService.stub();
    }

    @Override
    public void multiply() {
        PlantService.multiply(this);
    }

    @Override
    public void move() {
        PlantService.stub();
    }


    @Override
    public void run() {
            while (!isDead) {
                synchronized (this.getCell()) {
                    if (!isDead) {
                        multiply();
                    }
                }
            }
    }
}
