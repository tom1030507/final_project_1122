module game {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.media;

    opens game to javafx.fxml;
    exports game;
}
