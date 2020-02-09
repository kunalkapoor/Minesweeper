package com.kunalkapoor.minesweeper.game;

public class GameResult {
    public enum GameState {
        Playing, Won, Lost
    }

    public GameState state = GameState.Playing;
}
