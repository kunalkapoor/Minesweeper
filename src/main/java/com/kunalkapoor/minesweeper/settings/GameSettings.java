package com.kunalkapoor.minesweeper.settings;

import java.util.Map;

public class GameSettings {
    public enum GameDifficulty {
        Beginner, Intermediate, Expert
    }

    private static GameSettings instance;
    public GameDifficulty difficulty;
    public Map<GameDifficulty, BoardSettings> difficultyMap;
    public BoardSettings boardSettings;

    private GameSettings(GameDifficulty difficulty) {
        this.difficulty = difficulty;
        difficultyMap = Map.of(
                GameDifficulty.Beginner, new BoardSettings(8, 8, 10),
                GameDifficulty.Intermediate, new BoardSettings(16, 16, 40),
                GameDifficulty.Expert, new BoardSettings(24, 24, 99)
        );
        boardSettings = difficultyMap.get(difficulty);
    }

    public static GameSettings getInstance(GameDifficulty difficulty) {
        if (instance == null)
            instance = new GameSettings(difficulty);
        return instance;
    }
}
