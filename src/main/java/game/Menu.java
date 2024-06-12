package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Menu implements Background {
    private int backgroundWidth = 1300, backgroundHeight = 700;
    private Pane rootPane = new Pane(), menuPane = new Pane(), optionsPane = new Pane();
    private Scene scene;
    private Image menuButtonsImage = new Image(getClass().getResourceAsStream("menu_buttons.png"));
    private Image titleImage = new Image(getClass().getResourceAsStream("title.png"));
    private Image backgroundImage = new Image(getClass().getResourceAsStream("menu.jpg"));
    private Image menuBackgroundImage = new Image(getClass().getResourceAsStream("menu_background.png"));
    private Image optionsBackgroundImage = new Image(getClass().getResourceAsStream("options_background.png"));
    private Image homeButtonsImage = new Image(getClass().getResourceAsStream("urm_buttons.png"));
    private Image soundsButtonImage = new Image(getClass().getResourceAsStream("sound_button.png"));
    private Image volumeBarImage = new Image(getClass().getResourceAsStream("volume_bar.png"));
    private Image volumeButtonImage = new Image(getClass().getResourceAsStream("volume_buttons.png"));
    private ImageView imageView;
    Timeline timeline;

    public Scene createScene(Stage primaryStage) {
        VolumeController.playMusic("level1");
        imageView = new ImageView(backgroundImage);
        imageView.setFitWidth(backgroundWidth);
        imageView.setFitHeight(backgroundHeight);
        rootPane.getChildren().add(imageView);
        
        imageView = new ImageView(titleImage);
        imageView.setLayoutX(170);
        imageView.setLayoutY(80);
        imageView.setFitHeight(120);
        imageView.setFitWidth(960);
        rootPane.getChildren().add(imageView);

        imageView = new ImageView(menuBackgroundImage);
        imageView.setFitWidth(338.4);
        imageView.setFitHeight(400.8);
        imageView.setLayoutX(473);
        imageView.setLayoutY(200);
        menuPane.getChildren().add(imageView);

        Button startButton = createMenuButton(0);
        startButton.setLayoutX(565); // Set button position
        startButton.setLayoutY(255);

        startButton.setOnAction(e -> {
            timeline.stop();
            Main.setLevel(1);
        });

        Button optionsButton = createMenuButton(1);
        optionsButton.setLayoutX(565);
        optionsButton.setLayoutY(365);

        optionsButton.setOnAction(e -> {
            menuPane.setVisible(false);
            optionsPane.setVisible(true);
        });

        Button exitButton = createMenuButton(2);
        exitButton.setLayoutX(565);
        exitButton.setLayoutY(475);

        exitButton.setOnAction(e -> {
            primaryStage.close();
        });
        
        menuPane.getChildren().addAll(startButton, optionsButton, exitButton);

        imageView = new ImageView(optionsBackgroundImage);
        imageView.setFitWidth(337);
        imageView.setFitHeight(384);
        imageView.setLayoutX(473);
        imageView.setLayoutY(200);
        optionsPane.getChildren().add(imageView);

        Button homeButton = createHomeButton();

        homeButton.setOnAction(e -> {
            menuPane.setVisible(true);
            optionsPane.setVisible(false);
        });
        optionsPane.setVisible(false);
        optionsPane.getChildren().addAll(homeButton);

        Button musicSoundsButton = createSoundsButton(0, VolumeController.musicMute);
        musicSoundsButton.setLayoutX(680);
        musicSoundsButton.setLayoutY(243);

        Button soundEffectsButton = createSoundsButton(1, VolumeController.soundMute);
        soundEffectsButton.setLayoutX(680);
        soundEffectsButton.setLayoutY(293);

        ImageView volumnBar = new ImageView(volumeBarImage);
        volumnBar.setLayoutX(535);
        volumnBar.setLayoutY(420);

        Button volumeButton = createVolumeButton();
        optionsPane.getChildren().addAll(volumnBar, volumeButton, musicSoundsButton, soundEffectsButton);


        Pig pig=new Pig(70,620,1185,620);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1.0/60), e -> {
            pig.walk(); 
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        rootPane.getChildren().addAll(menuPane, optionsPane,pig);

        scene = new Scene(rootPane, backgroundWidth, backgroundHeight);
        return scene;
    }

    private Button createMenuButton(int index) {
        Button button = new Button();
        double buttonWidth = menuButtonsImage.getWidth() / 3;
        double buttonHeight = menuButtonsImage.getHeight() / 3;
        ImageView imageView = new ImageView(menuButtonsImage);
        imageView.setViewport(new Rectangle2D(0, index * buttonHeight, buttonWidth, buttonHeight));
        button.setGraphic(imageView);
        button.setScaleX(1.5);
        button.setScaleY(1.5);
        button.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent;");

        button.setOnMousePressed(e -> {
            imageView.setViewport(new Rectangle2D(2 * buttonWidth, index * buttonHeight, buttonWidth, buttonHeight));
            button.setGraphic(imageView);
        });
        button.setOnMouseReleased(e -> {
            imageView.setViewport(new Rectangle2D(0, index * buttonHeight, buttonWidth, buttonHeight));
            button.setGraphic(imageView);
        });
        
        return button;
    }

    private Button createHomeButton() {
        Button button = new Button();
        double buttonWidth = homeButtonsImage.getWidth() / 3;
        double buttonHeight = homeButtonsImage.getHeight() / 3;
        ImageView imageView = new ImageView(homeButtonsImage);
        imageView.setViewport(new Rectangle2D(0, 2 * buttonHeight, buttonWidth, buttonHeight));
        button.setGraphic(imageView);
        button.setLayoutX(602);
        button.setLayoutY(480);

        button.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent;");

        button.setOnMousePressed(e -> {
            imageView.setViewport(new Rectangle2D(2 * buttonWidth, 2 * buttonHeight, buttonWidth, buttonHeight));
            button.setGraphic(imageView);
        });
        button.setOnMouseReleased(e -> {
            imageView.setViewport(new Rectangle2D(0 , 2 * buttonHeight, buttonWidth, buttonHeight));
            button.setGraphic(imageView);
        });
        
        return button;
    }

    private Button createSoundsButton(int soundType, Boolean isMute) {
        Button button = new Button();
        double buttonWidth = soundsButtonImage.getWidth() / 3;
        double buttonHeight = soundsButtonImage.getHeight() / 2;
        final boolean[] isMuteType = {isMute};

        ImageView imageView = new ImageView(soundsButtonImage);
        imageView.setViewport(new Rectangle2D(0, (isMuteType[0] ? 1 : 0) * buttonHeight, buttonWidth, buttonHeight));
        button.setGraphic(imageView);

        button.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent;");

        button.setOnMousePressed(e -> {
            imageView.setViewport(new Rectangle2D(2 * buttonWidth, (isMuteType[0] ? 1 : 0) * buttonHeight, buttonWidth, buttonHeight));
            button.setGraphic(imageView);
        });
        button.setOnMouseReleased(e -> {
            isMuteType[0] = !isMuteType[0];

            if (soundType == 0) {
                VolumeController.setMusicMute(isMuteType[0]);
            } else {
                VolumeController.setSoundMute(isMuteType[0]);
            }
            imageView.setViewport(new Rectangle2D(0 , (isMuteType[0] ? 1 : 0) * buttonHeight, buttonWidth, buttonHeight));
            button.setGraphic(imageView);
        });
        
        return button;
    }

    private Button createVolumeButton() {
        Button button = new Button();
        double buttonWidth = volumeButtonImage.getWidth() / 3;
        double buttonHeight = volumeButtonImage.getHeight();
        double startX = 525;
        double endX = 715;
        ImageView imageView = new ImageView(volumeButtonImage);

        imageView.setViewport(new Rectangle2D(0, 0, buttonWidth, buttonHeight));
        button.setGraphic(imageView);
        button.setLayoutX(620);
        button.setLayoutY(416);

        button.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent;");

        button.setOnMousePressed(e -> {
            button.setUserData(e.getSceneX());
        });

        button.setOnMouseDragged(e -> {
            double dragX = e.getSceneX();
            double offsetX = dragX - (double) button.getUserData();
            double newLayoutX = button.getLayoutX() + offsetX;
        
            if (newLayoutX >= startX && newLayoutX <= endX) {
                button.setLayoutX(newLayoutX);
                button.setUserData(dragX);
            }
            VolumeController.setVolume((newLayoutX - startX) / (endX - startX));
        });

        button.setOnMouseReleased(e -> {
            imageView.setViewport(new Rectangle2D(0 , 0, buttonWidth, buttonHeight));
            button.setGraphic(imageView);
        });
        
        return button;
    }

}