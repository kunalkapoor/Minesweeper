package com.kunalkapoor.minesweeper;

import com.kunalkapoor.minesweeper.game.Cell;
import com.kunalkapoor.minesweeper.game.GameBoard;
import com.kunalkapoor.minesweeper.game.GameResult;
import com.kunalkapoor.minesweeper.settings.GameSettings;
import com.kunalkapoor.minesweeper.util.Coordinate;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MinesweeperMain extends Application {
    private GameBoard board;
    private Map<Button, Coordinate> buttonMap;
    private GameSettings settings;

    @java.lang.Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles.css");

        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.show();

        playGameGui(root);
    }

    private void playGameGui(GridPane root) {
        initializeGame(settings);

        // Create buttons for each cell
        for (int i = 0; i < settings.boardSettings.numRows; i++) {
            for (int j = 0; j < settings.boardSettings.numCols; j++) {
                Button button = new Button();
                button.setMaxSize(30.0, 30.0);
                button.setMinSize(30.0, 30.0);
                root.add(button, j, i);

                Coordinate coordinate = new Coordinate(i, j);
                buttonMap.put(button, coordinate);
            }
        }

        // Set event handler for each button click
        for (Button button: buttonMap.keySet()) {
            Coordinate coordinate = buttonMap.get(button);
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    GameResult result = board.openCell(coordinate);
                    renderGameBoard(root, result);
                }
            });
        }

        renderGameBoard(root, null);
    }

    private void renderGameBoard(GridPane root, GameResult result) {
        for (Button button: buttonMap.keySet()) {
            Coordinate coordinate = buttonMap.get(button);
            Cell cell = board.getCell(coordinate);

            if (cell.visible) {
                button.setDisable(true);
                if (cell.type == Cell.CellType.Bomb) {
                    button.setText("@");
                    button.setTextFill(Color.BLACK);
                } else {
                    button.setText(String.valueOf(cell.value));
                    if (cell.value == 0) {
                        button.setText("");
                    } else if (cell.value == 1) {
                        button.setTextFill(Color.BLUE);
                    } else if (cell.value == 2) {
                        button.setTextFill(Color.GREEN);
                    } else if (cell.value == 3){
                        button.setTextFill(Color.RED);
                    } else {
                        button.setTextFill(Color.PURPLE);
                    }
                }
            } else {
                button.setDisable(false);
                if (cell.flagged) {
                    button.setText("!");
                    button.setTextFill(Color.ORANGE);
                } else {
                    button.setText("");
                }
            }
        }

        if (result != null && result.state != GameResult.GameState.Playing)
            gameOver(root, result);
    }

    private void gameOver(GridPane root, GameResult result) {
        Text text = new Text();
        if (result.state == GameResult.GameState.Won)
            text.setText("You found all the bombs... Congratulations, you won!");
        else
            text.setText("You uncovered a bomb and lost... Better luck next time!");

        root.add(text, settings.boardSettings.numCols + 1, settings.boardSettings.numRows + 1);
    }

    public void playGameCli() {
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
        // Harcode the settings until the ability for the user to select is implemented
        if (settings == null)
            settings = GameSettings.getInstance(GameSettings.GameDifficulty.Beginner);
        this.settings = settings;
        board = new GameBoard(settings.boardSettings);
        buttonMap = new HashMap<>();
    }

    public void renderAscii() {
        board.printBoard();
    }

    public static void main(String[] args) {
        launch(args);
//        new MinesweeperMain().playGame();
    }
}
