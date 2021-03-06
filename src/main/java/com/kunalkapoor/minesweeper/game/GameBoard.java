package com.kunalkapoor.minesweeper.game;

import com.kunalkapoor.minesweeper.settings.BoardSettings;
import com.kunalkapoor.minesweeper.util.Coordinate;

import java.util.List;
import java.util.Random;

public class GameBoard {
    public Cell[][] cells;
    private BoardSettings settings;
    int numNonBombCells;
    int numCellsOpened;
    Random randomizer;
    GameResult result;

    public GameBoard(BoardSettings boardSettings) {
        this.settings = boardSettings;
        this.randomizer = new Random();
        numNonBombCells = (settings.numCols * settings.numRows) - settings.numBombs;
        numCellsOpened = 0;
        result = new GameResult();
        initializeBoard();
    }

    public void initializeBoard() {
        cells = new Cell[settings.numRows][settings.numCols];
        if (settings.numBombs > settings.numRows * settings.numCols)
            throw new RuntimeException("Too many bombs for the board size");

        for (int i = 0; i < settings.numBombs; i++) {
            Coordinate c = getRowColForIndex(i);
            cells[c.row][c.col] = new Cell(Cell.CellType.Bomb);
        }

        for (int i = settings.numBombs; i < settings.numRows * settings.numCols; i++) {
            Coordinate c = getRowColForIndex(i);
            cells[c.row][c.col] = new Cell(Cell.CellType.Number);
        }

        shuffleBoard();
        assignCellValues();
    }

    private Coordinate getRowColForIndex(int index) {
        int row = index / settings.numCols;
        int col = index % settings.numCols;
        return new Coordinate(row, col);
    }

    private void shuffleBoard() {
        int maxIndex = settings.numRows * settings.numCols;
        for (int i = 0; i < maxIndex; i++) {
            Coordinate src = getRowColForIndex(i);
            Coordinate dst = getRowColForIndex(randomizer.nextInt(maxIndex));
            swapCells(src, dst);
        }
    }

    private void swapCells(Coordinate src, Coordinate dst) {
        Cell tempCell = cells[src.row][src.col];
        cells[src.row][src.col] = cells[dst.row][dst.col];
        cells[dst.row][dst.col] = tempCell;
    }

    private void assignCellValues() {
        int maxIndex = settings.numRows * settings.numCols;
        for (int i = 0; i < maxIndex; i++) {
            updateSurroundingCells(getRowColForIndex(i));
        }
    }

    private void updateSurroundingCells(Coordinate coordinate) {
        Cell cell = getCell(coordinate);
        if (cell.type != Cell.CellType.Bomb)
            return;

        for (Coordinate neighbour: surroundingCoordinates(coordinate))
            if (isInBoardBounds(neighbour))
                getCell(neighbour).incrementValue();
    }

    private List<Coordinate> surroundingCoordinates(Coordinate coordinate) {
        Coordinate topLeft = new Coordinate(coordinate.row - 1, coordinate.col - 1);
        Coordinate top = new Coordinate(coordinate.row - 1, coordinate.col);
        Coordinate topRight = new Coordinate(coordinate.row - 1, coordinate.col + 1);
        Coordinate bottomLeft = new Coordinate(coordinate.row + 1, coordinate.col - 1);
        Coordinate bottom = new Coordinate(coordinate.row + 1, coordinate.col);
        Coordinate bottomRight = new Coordinate(coordinate.row + 1, coordinate.col + 1);
        Coordinate left = new Coordinate(coordinate.row, coordinate.col - 1);
        Coordinate right = new Coordinate(coordinate.row, coordinate.col + 1);

        return List.of(topLeft, top, topRight, bottomLeft, bottom, bottomRight, left, right);
    }

    private boolean isInBoardBounds(Coordinate coordinate) {
        return coordinate.row >= 0 && coordinate.row < settings.numRows &&
                coordinate.col >= 0 && coordinate.col < settings.numCols;
    }

    public void printBoard() {
        System.out.println();
        for (Cell[] cellRow: cells) {
            for (Cell cell: cellRow)
                System.out.print(cell.str() + " ");
            System.out.println();
        }
        System.out.println();
    }

    public GameResult openCell(Coordinate coordinate) {
        if (!isInBoardBounds(coordinate))
            return result;

        Cell cell = getCell(coordinate);
        if (cell.visible)
            return result;
        if (cell.type == Cell.CellType.Bomb && cell.open()) {
            result.state = GameResult.GameState.Lost;
            return result;
        }
        if (cell.isEmpty())
            openEmptyCells(coordinate);
        else if (cell.open())
            numCellsOpened++;

        if (isVictory())
            result.state = GameResult.GameState.Won;
        return result;
    }

    private boolean isVictory() {
        return numCellsOpened >= numNonBombCells;
    }

    private void openEmptyCells(Coordinate coordinate) {
        if (!isInBoardBounds(coordinate))
            return;

        Cell cell = getCell(coordinate);
        if (cell.visible)
            return;

        if (cell.open())
            numCellsOpened++;

        if (cell.isEmpty())
            for (Coordinate neighbour: surroundingCoordinates(coordinate))
                openEmptyCells(neighbour);
    }

    public void flipBombFlag(Coordinate coordinate) {
        Cell cell = getCell(coordinate);
        cell.flipBombFlag();
    }

    public Cell getCell(Coordinate coordinate) {
        return cells[coordinate.row][coordinate.col];
    }
}
