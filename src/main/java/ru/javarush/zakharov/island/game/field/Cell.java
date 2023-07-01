package ru.javarush.zakharov.island.game.field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.javarush.zakharov.island.game.entity.AllCharactersAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "list")
public class Cell {
    private int row;
    private int col;
    List<AllCharactersAction> list;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        list = Collections.synchronizedList(new ArrayList<>());
    }
}
