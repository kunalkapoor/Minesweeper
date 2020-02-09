package com.kunalkapoor.minesweeper.game;

public class Cell {
    public enum CellType {
        Number, Bomb
    }

    public CellType type;
    public int value = 0;
    public boolean visible = false;
    public boolean flagged = false;

    public Cell(CellType type) {
        this.type = type;
    }

    public String str() {
        if (visible) {
            if (type == CellType.Bomb)
                return "*";
            if (value == 0)
                return "░";
            return String.valueOf(value);
        } else {
            if (type == CellType.Bomb)
                return "@";
            if (flagged)
                return "!";
            return "▓";
        }
    }

    public void flipBombFlag() {
        if (!visible)
            flagged = !flagged;
    }

    public boolean open() {
        if (flagged || visible)
            return false;

        visible = true;
        return true;
    }

    public void incrementValue() {
        if (type == CellType.Bomb || visible)
            return;
        value++;
    }

    public boolean isEmpty() {
        return value == 0;
    }
}
