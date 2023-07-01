package ru.javarush.zakharov.island.game.field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field {
    private Cell[][] cells;
    private static Field instance;

    private Field() {
    }

    public static Field getInstance() {
        if (instance == null) {
            instance = new Field();
        }
        return instance;
    }
}
