package game;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.PerspectiveCamera;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;

public class Level1 implements Background {
    private int backgroundWidth = 1300, backgroundHeight = 700;
    private double scope = 0.5; // 摄像机缩放比例
    private ImageView background;
    private Pane rootPane = new Pane(), pauseMenu = new Pane(), diedPane = new Pane();
    private Scene scene;
    private Timeline timeline;
    private CharacterController controller;

    public Scene createScene(Stage primaryStage) {
        background = new ImageView(new Image(getClass().getResourceAsStream("level1_background.png")));
        background.setFitWidth(backgroundWidth);
        background.setFitHeight(backgroundHeight);

		Character character = new Character(95, 590, 1);
        controller = new CharacterController(character, 1);

        Boundary boundary = new Boundary(1);
        
        Pig pig1 = new Pig(450, 612, 877, 612);
        Pig pig2 = new Pig(430, 220, 700, 220);
        Pig pig3 = new Pig(75, 270, 245, 270);
		pig1.setTargetPlayer(character);
        pig2.setTargetPlayer(character);
        pig3.setTargetPlayer(character);
        Pane pig = new Pane();
        pig.getChildren().addAll(pig1, pig2, pig3);        

        Cannon cannon=new Cannon(75,377,1170,377,-1);
        cannon.setTargetPlayer(character);

        Door door=new Door(100,584);
        door.setTargetPlayer(character);
        door.used=false;

        Door door2=new Door(1140,139);
        door2.setTargetPlayer(character);

        Key key=new Key(80,167);
        key.setTargetPlayer(character);

        character.setOnKeyPressed(e -> {
            controller.handleKeyPressed(e.getCode());
        });

        character.setOnKeyReleased(e -> {
            controller.handleKeyReleased(e.getCode());
        });

        woodPlatform shortPlatform1 = new woodPlatform(1, 600, 400);
        woodPlatform shortPlatform2 = new woodPlatform(1, 800, 450);
        woodPlatform shortPlatform3 = new woodPlatform(1, 810, 210);
        woodPlatform longPlatform1 = new woodPlatform(2, 75, 300);
        woodPlatform longPlatform2 = new woodPlatform(2, 175, 300);
        woodPlatform longPlatform3 = new woodPlatform(2, 75, 200);
        woodPlatform longPlatform4 = new woodPlatform(2, 430, 250);
        woodPlatform longPlatform5 = new woodPlatform(2, 530, 250);
        woodPlatform longPlatform6 = new woodPlatform(2, 630, 250);
        Pane platform = new Pane();
        platform.getChildren().addAll(shortPlatform1, shortPlatform2, shortPlatform3, longPlatform1, longPlatform2, longPlatform3, longPlatform4, longPlatform5, longPlatform6);

        rootPane.getChildren().addAll(background, door, door2, key, character, pig, cannon, platform, boundary.getBoundary(), pauseMenu, diedPane);

        scene = new Scene(rootPane, backgroundWidth, backgroundHeight);

        VolumeController.playMusic("level1");

        initPauseOverlay();
        initDiedLayout();

        PerspectiveCamera camera = new PerspectiveCamera();
        scene.setCamera(camera);
        camera.setScaleX(scope);
        camera.setScaleY(scope);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1.0/60), e -> {
            controller.update();
            togglePause();
            if (character.health <= 0) {
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> {
                    showDiedLayout();
                    diedPane.setVisible(true);
                });
                pause.play();
            }
            if (!controller.stop || character.health <= 0) {
                character.applyGravity();
                pig1.update();
                pig2.update();
                pig3.update();
                cannon.update();
                door.update();
                door2.update();
                key.update();
    
                if (door2.nextlevel) {
                    nextlevel();
                }
    
                if (controller.stop) {
                    gamestop();
                }
    
                if (character.attackstate()){
                    character.attackstateupdate();
                }
    
                double newCameraX = character.boundingBox.getCenterX() - (scene.getWidth()/2*scope);
                double newCameraY = character.boundingBox.getCenterY() - (scene.getHeight()/2*scope);
                // 限制摄像机X轴范围
                newCameraX = Math.max(newCameraX, 0);
                newCameraX = Math.min(newCameraX, backgroundWidth-scene.getWidth()*scope);
    
                // 限制摄像机Y轴范围
                newCameraY = Math.max(newCameraY, 0);
                newCameraY = Math.min(newCameraY, backgroundHeight-scene.getHeight()*scope);
    
                camera.setTranslateX(newCameraX);
                camera.setTranslateY(newCameraY);
            } else {
                togglePause();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    
		character.requestFocus();
        character.setFocusTraversable(true);

        return scene;
    }

    public void gamestop(){
        timeline.stop();
    }

    public void nextlevel() {
        timeline.stop();
        VolumeController.stopMusic("level1");
        Platform.runLater(() -> Main.setLevel(2));
    }

    private void initDiedLayout() {
        ImageView diedImage = new ImageView(new Image(getClass().getResourceAsStream("death_screen.png")));

        diedImage.setLayoutX(200);
        diedImage.setLayoutY(60);
        Rectangle diedScreen = new Rectangle(scene.getWidth(), scene.getHeight(), Color.rgb(0, 0, 0, 0.7));
        Button homeButton = createUrmButton(2);
        homeButton.setLayoutX(225);
        homeButton.setLayoutY(160);
        homeButton.setScaleX(0.7);
        homeButton.setScaleY(0.7);
        homeButton.setOnAction(e -> {
            timeline.stop();
            VolumeController.stopMusic("level1");
            Main.setLevel(0); 
        });

        Button restartButton = createUrmButton(1);
        restartButton.setLayoutX(330);
        restartButton.setLayoutY(160);
        restartButton.setScaleX(0.7);
        restartButton.setScaleY(0.7);

        restartButton.setOnAction(e -> {
            timeline.stop();
            VolumeController.stopMusic("level1");
            Main.setLevel(1);
        });
        diedPane.getChildren().addAll(diedScreen, diedImage, homeButton, restartButton);
        diedPane.setVisible(false);
    }

    private void showDiedLayout() {
        if (scene != null) {
            PerspectiveCamera camera = (PerspectiveCamera) scene.getCamera();
            double cameraX = camera.getTranslateX();
            double cameraY = camera.getTranslateY();
            diedPane.setLayoutX(cameraX + (scene.getWidth() - diedPane.getWidth()) / 2);
            diedPane.setLayoutY(cameraY + (scene.getHeight() - diedPane.getHeight()) / 2);
        }
        diedPane.setVisible(true);
        VolumeController.stopMusic("level1");
    }

    private void initPauseOverlay() {
        ImageView pauseMenuImage = new ImageView(new Image(getClass().getResourceAsStream("pause_menu.png")));
        pauseMenuImage.setFitWidth(206.4);
        pauseMenuImage.setFitHeight(311.2);
        pauseMenuImage.setLayoutX(220);
        pauseMenuImage.setLayoutY(20);
        Rectangle pauseScreen = new Rectangle(scene.getWidth(), scene.getHeight(), Color.rgb(0, 0, 0, 0.7));
        Button musicButton = createSoundsButton(0, VolumeController.musicMute);
        musicButton.setLayoutX(345);
        musicButton.setLayoutY(105);
        musicButton.setScaleX(0.7);
        musicButton.setScaleY(0.7);
        Button soundButton = createSoundsButton(1, VolumeController.soundMute);
        soundButton.setLayoutX(345);
        soundButton.setLayoutY(140);
        soundButton.setScaleX(0.7);
        soundButton.setScaleY(0.7);
        Image volumeBarImage = new Image(getClass().getResourceAsStream("volume_bar.png"));
        ImageView volumeBar = new ImageView(volumeBarImage);
        volumeBar.setLayoutX(215);
        volumeBar.setLayoutY(220);
        volumeBar.setScaleX(0.7);
        volumeBar.setScaleY(0.7);
        Button volumeButton = createVolumeButton();
        volumeButton.setScaleX(0.7);
        volumeButton.setScaleY(0.7);
        Button homeButton = createUrmButton(2);
        homeButton.setLayoutX(232);
        homeButton.setLayoutY(253);
        homeButton.setScaleX(0.7);
        homeButton.setScaleY(0.7);
        homeButton.setOnAction(e -> {
            timeline.stop();
            VolumeController.stopMusic("level1");
            Main.backToMenu();
        });

        Button playButton = createUrmButton(0);
        playButton.setLayoutX(286);
        playButton.setLayoutY(253);
        playButton.setScaleX(0.7);
        playButton.setScaleY(0.7);

        playButton.setOnAction(e -> {
            controller.stop = false;
            togglePause();
        });

        Button restartButton = createUrmButton(1);
        restartButton.setLayoutX(340);
        restartButton.setLayoutY(253);
        restartButton.setScaleX(0.7);
        restartButton.setScaleY(0.7);

        restartButton.setOnAction(e -> {
            timeline.stop();
            VolumeController.stopMusic("level1");
            Main.setLevel(1);
        });

        pauseMenu.getChildren().addAll(pauseScreen, pauseMenuImage, musicButton, soundButton, volumeBar, volumeButton, playButton, homeButton, restartButton); // 添加到根面板
        pauseMenu.setVisible(false);  
    }

    public void togglePause() {
        pauseMenu.setVisible(controller.stop); 
        updatePauseOverlayPosition();
    }

    private void updatePauseOverlayPosition() {
        if (scene != null && controller.stop) {
            PerspectiveCamera camera = (PerspectiveCamera) scene.getCamera();
            double cameraX = camera.getTranslateX();
            double cameraY = camera.getTranslateY();
            pauseMenu.setLayoutX(cameraX + (scene.getWidth() - pauseMenu.getWidth()) / 2);
            pauseMenu.setLayoutY(cameraY + (scene.getHeight() - pauseMenu.getHeight()) / 2);
        }
    }

    private Button createSoundsButton(int soundType, Boolean isMute) {
        Image soundsButtonImage = new Image(getClass().getResourceAsStream("sound_button.png"));
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
        Image volumeButtonImage = new Image(getClass().getResourceAsStream("volume_buttons.png"));
        Button button = new Button();
        double buttonWidth = volumeButtonImage.getWidth() / 3;
        double buttonHeight = volumeButtonImage.getHeight();
        double startX = 230;
        double endX = 370;
        ImageView imageView = new ImageView(volumeButtonImage);

        imageView.setViewport(new Rectangle2D(0, 0, buttonWidth, buttonHeight));
        button.setGraphic(imageView);
        button.setLayoutX(VolumeController.totalVolume * (endX - startX) + startX);
        button.setLayoutY(216);

        button.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent;");

        button.setOnMousePressed(e -> {
            button.setUserData(e.getSceneX());
        });

        button.setOnMouseDragged(e -> {
            double dragX = e.getSceneX();
            double offsetX = (dragX - (double) button.getUserData()) * scope;
            double newLayoutX = button.getLayoutX() + offsetX;
        
            if (newLayoutX >= startX && newLayoutX <= endX) {
                button.setLayoutX(newLayoutX);
                button.setUserData(dragX);
            }
            VolumeController.setTotalVolume((newLayoutX - startX) / (endX - startX));
        });

        button.setOnMouseReleased(e -> {
            imageView.setViewport(new Rectangle2D(0 , 0, buttonWidth, buttonHeight));
            button.setGraphic(imageView);
        });
        
        return button;
    }

    private Button createUrmButton(int type) {
        Image urmButtonsImage = new Image(getClass().getResourceAsStream("urm_buttons.png"));
        Button button = new Button();
        double buttonWidth = urmButtonsImage.getWidth() / 3;
        double buttonHeight = urmButtonsImage.getHeight() / 3;
        ImageView imageView = new ImageView(urmButtonsImage);
        imageView.setViewport(new Rectangle2D(0, type * buttonHeight, buttonWidth, buttonHeight));
        button.setGraphic(imageView);

        button.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent;");

        button.setOnMousePressed(e -> {
            imageView.setViewport(new Rectangle2D(2 * buttonWidth, type * buttonHeight, buttonWidth, buttonHeight));
            button.setGraphic(imageView);
        });
        button.setOnMouseReleased(e -> {
            imageView.setViewport(new Rectangle2D(0 , type * buttonHeight, buttonWidth, buttonHeight));
            button.setGraphic(imageView);
        });
        
        return button;
    }
}