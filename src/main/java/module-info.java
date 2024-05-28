module game {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens game to javafx.fxml;
    exports game;
}
