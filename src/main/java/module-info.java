module minesweeper {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.kunalkapoor.minesweeper to javafx.fxml;
    exports com.kunalkapoor.minesweeper;
}