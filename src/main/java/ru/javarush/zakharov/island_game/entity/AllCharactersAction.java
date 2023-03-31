package ru.javarush.zakharov.island_game.entity;

import ru.javarush.zakharov.island_game.field.Cell;


public interface AllCharactersAction extends Runnable {

    void eat();

    void multiply();

    void move();

    Cell getCell();

    void setDead(boolean isDead);

    boolean isDead();

    String getName();
    void setCell(Cell cell);

    int getMaxCountTypeOnOneCell();
}
