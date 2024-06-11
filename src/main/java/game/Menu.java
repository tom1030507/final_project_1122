package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;

public class Menu implements Background {
    private int backgroundWidth = 1300, backgroundHeight = 700;
    private Pane pane;
    private Scene scene;
    private Image originButtonsImage = new Image(getClass().getResourceAsStream("button_origin.png"));
    private Image pressedButtonsImage = new Image(getClass().getResourceAsStream("button_pressed.png"));
    private Image titleImage = new Image(getClass().getResourceAsStream("title.png"));
    private ImageView imageView;
    public Scene createScene(Stage primaryStage) {
        pane = new Pane();
        scene = new Scene(pane, backgroundWidth, backgroundHeight);

        double buttonWidth = originButtonsImage.getWidth();
        double buttonHeight = originButtonsImage.getHeight() / 3;
        
        ImageView title = new ImageView(titleImage);
        title.setLayoutX(170);
        title.setLayoutY(80);
        title.setFitHeight(120);
        title.setFitWidth(960);
        pane.getChildren().add(title);

        Button startButton = createButton(0, buttonWidth, buttonHeight);
        startButton.setLayoutX(565); // Set button position
        startButton.setLayoutY(250);

        startButton.setOnAction(e -> {
            Main.setLevel(1);
        });

        Button optionsButton = createButton(1, buttonWidth, buttonHeight);
        optionsButton.setLayoutX(565);
        optionsButton.setLayoutY(380);

        Button exitButton = createButton(2, buttonWidth, buttonHeight);
        exitButton.setLayoutX(565);
        exitButton.setLayoutY(510);

        exitButton.setOnAction(e -> {
            primaryStage.close();
        });
        
        Pane button = new Pane();
        button.getChildren().addAll(startButton, optionsButton, exitButton);

        pane.getChildren().add(button);

        return scene;
    }

    private Button createButton(int index, double buttonWidth, double buttonHeight) {
        Button button = new Button();
        imageView = new ImageView(originButtonsImage);
        imageView.setViewport(new Rectangle2D(0, index * buttonHeight, buttonWidth, buttonHeight));
        button.setGraphic(imageView);
        button.setScaleX(1.5);
        button.setScaleY(1.5);
        button.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent;");

        button.setOnMousePressed(e -> {
            imageView = new ImageView(pressedButtonsImage);
            imageView.setViewport(new Rectangle2D(0, index * buttonHeight, buttonWidth, buttonHeight));
            button.setGraphic(imageView);
        });
        button.setOnMouseReleased(e -> {
            imageView = new ImageView(originButtonsImage);
            imageView.setViewport(new Rectangle2D(0, index * buttonHeight, buttonWidth, buttonHeight));
            button.setGraphic(imageView);
        });
        
        return button;
    }

}