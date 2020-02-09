package com.kunalkapoor.minesweeper.settings;

public class BoardSettings {
    public int numRows, numCols, numBombs;

    public BoardSettings(int nRows, int nCols, int nBombs) {
        this.numRows = nRows;
        this.numCols = nCols;
        this.numBombs = nBombs;

        /*
         * Ensure there aren't too many bombs for the board. Usually the bomb to cell ratio is around 6.5 for
         * the Windows minesweepers. Keeping the max ratio at 5 should be conservative enough.
         */
        if (nBombs > nRows * nCols / 5) {
            nBombs = (nRows * nCols) / 5;
        }
    }
}