package com.kunalkapoor.minesweeper;

import com.kunalkapoor.minesweeper.game.GameBoard;
import com.kunalkapoor.minesweeper.game.GameResult;
import com.kunalkapoor.minesweeper.settings.GameSettings;
import com.kunalkapoor.minesweeper.util.Coordinate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Scanner;

public class MinesweeperMain extends Application {
    private GameBoard board;

    @java.lang.Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void playGame() {
        GameSettings settings = GameSettings.getInstance(GameSettings.GameDifficulty.Beginner);
        initializeGame(settings);

        Scanner scanner = new Scanner(System.in);
        GameResult result = new GameResult();
        while (result.state == GameResult.GameState.Playing) {
            renderAscii();
            Coordinate coordinate = prompt(scanner);
            if (coordinate == null)
                continue;
            result = board.openCell(coordinate);
        }

        System.out.println("Final Game State:");
        renderAscii();
        if (result.state == GameResult.GameState.Won)
            System.out.println("You found all the bombs... Congratulations, you won!");
        else
            System.out.println("You uncovered a bomb and lost... Better luck next time!");
        System.exit(0);
    }

    private Coordinate prompt(Scanner scanner) {
        System.out.println("Enter coordinate to open in [row,col] format: ");
        String line = scanner.nextLine();
        try {
            int row = Integer.parseInt(line.split(",")[0]);
            int col = Integer.parseInt(line.split(",")[1]);
            return new Coordinate(row, col);
        } catch (Exception e) {
            System.out.println("Invalid input");
        }
        return null;
    }

    public void initializeGame(GameSettings settings) {
        board = new GameBoard(settings.boardSettings);
    }

    public void renderAscii() {
        board.printBoard();
    }

    public static void main(String[] args) {
//        launch(args);
        new MinesweeperMain().playGame();
    }
}
