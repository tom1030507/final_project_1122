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
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.PerspectiveCamera;
import javafx.util.Duration;
import javafx.stage.Stage;


public class Level2 implements Background {
    private int backgroundWidth = 2600, backgroundHeight = 1400;
    private double scope = 0.25; 
    private double scale = 0.5;
    private ImageView background;
    private Scene scene;
    private Pane pane = new Pane(), pauseMenu = new Pane(), diedPane = new Pane();
    private CharacterController controller;
    private Timeline timeline;
    private Rectangle blackScreen;
    private boolean isPaused = false;
    
    public Scene createScene(Stage primaryStage) {
        background = new ImageView(new Image(getClass().getResourceAsStream("level2_background.png")));
        background.setFitWidth(backgroundWidth);
        background.setFitHeight(backgroundHeight);

        Pane bloodpane = new Pane();
        ImageView blood1= new ImageView(new Image(getClass().getResourceAsStream("Big Bars/1.png")));
        ImageView newblood4= new ImageView(new Image(getClass().getResourceAsStream("Big Bars/4.png")));
        ImageView newblood= new ImageView(new Image(getClass().getResourceAsStream("Big Bars/blood.png")));
        newblood4.setTranslateX(32*4);
        newblood.setTranslateX(16);
        newblood.setTranslateY(13);
        newblood.setFitWidth(32*5-18);
        newblood.setFitHeight(4);
        for(int i=1;i<4;i++){
            ImageView blood3= new ImageView(new Image(getClass().getResourceAsStream("Big Bars/3.png")));
            blood3.setTranslateX(32*i);
            bloodpane.getChildren().add(blood3);
        }
        bloodpane.getChildren().addAll(blood1,newblood4,newblood);

		Character character = new Character(2435, 1300, 2);
        controller = new CharacterController(character, 2);

        Boundary boundary = new Boundary(2);
        
        Pig pig1 = new Pig(80, 1320, 660, 1320);
        Pig pig2 = new Pig(590, 787, 940, 787);
        Pig pig3 = new Pig(700, 360, 1135, 360);
        Pig pig4 = new Pig(1550, 245, 2050, 245);
        Pig pig5 = new Pig(1150, 1320, 2100, 1320);
		pig1.setTargetPlayer(character);
        pig2.setTargetPlayer(character);
        pig3.setTargetPlayer(character);
        pig4.setTargetPlayer(character);
        pig5.setTargetPlayer(character);
        Pane pig = new Pane();
        pig.getChildren().addAll(pig1, pig2, pig3, pig4, pig5);        

        Cannon cannon=new Cannon(2480,777,300,777,1);
        cannon.setTargetPlayer(character);

        Door door=new Door(2435,1291);
        door.setTargetPlayer(character);
        door.used=false;

        Door door2=new Door(100,186);
        door2.setTargetPlayer(character);

        Key key=new Key(2435,350);
        key.setTargetPlayer(character);

        Box box=new Box(75,722,1);
        box.setTargetPlayer(character);

        Box box2=new Box(75,1130,1);
        box2.setTargetPlayer(character);

        Box box3=new Box(75,1227,2);
        box3.setTargetPlayer(character);

        Box box4=new Box(2500,360,2);
        box4.setTargetPlayer(character);

        Fire fire1=new Fire(2083, 410);
        Fire fire2=new Fire(2116, 410);
        Fire fire3=new Fire(2149, 410);
        Fire fire4=new Fire(2182, 410);
        Fire fire5=new Fire(2215, 410);
        Fire fire6=new Fire(2248, 410);
        fire1.setTargetPlayer(character);
        fire2.setTargetPlayer(character);
        fire3.setTargetPlayer(character);
        fire4.setTargetPlayer(character);
        fire5.setTargetPlayer(character);
        fire6.setTargetPlayer(character);
        Pane fire = new Pane();
        fire.getChildren().addAll(fire1, fire2, fire3, fire4, fire5, fire6); 
        
        character.setOnKeyPressed(e -> {
            controller.handleKeyPressed(e.getCode());
        });

        character.setOnKeyReleased(e -> {
            controller.handleKeyReleased(e.getCode());
        });

        blackScreen = new Rectangle(backgroundWidth, backgroundHeight, Color.rgb(0, 0, 0, 0.7));
        blackScreen.setVisible(false);
        
        woodPlatform shortPlatform1 = new woodPlatform(1, 1280, 1060);
        woodPlatform shortPlatform2 = new woodPlatform(1, 1440, 970);
        woodPlatform shortPlatform3 = new woodPlatform(1, 1120, 950);
        woodPlatform shortPlatform4 = new woodPlatform(1, 970, 860);
        woodPlatform shortPlatform5 = new woodPlatform(1, 450, 720);
        woodPlatform shortPlatform6 = new woodPlatform(1, 540, 610);
        woodPlatform shortPlatform7 = new woodPlatform(1, 1340, 340);
        woodPlatform shortPlatform8 = new woodPlatform(1, 2120, 830);
        woodPlatform shortPlatform9 = new woodPlatform(1, 2170, 320);
        woodPlatform longPlatform1 = new woodPlatform(2, 75, 1250);
        woodPlatform longPlatform2 = new woodPlatform(2, 175, 1250);
        woodPlatform longPlatform3 = new woodPlatform(2, 75, 1150);
        woodPlatform longPlatform4 = new woodPlatform(2, 1160, 1240);
        woodPlatform longPlatform5 = new woodPlatform(2, 600, 500);
        woodPlatform longPlatform6 = new woodPlatform(2, 425, 350);
        Pane platform = new Pane();
        platform.getChildren().addAll(shortPlatform1, shortPlatform2, shortPlatform3, shortPlatform4, shortPlatform5, shortPlatform6, shortPlatform7, shortPlatform8, shortPlatform9, longPlatform1, longPlatform2, longPlatform3, longPlatform4, longPlatform5, longPlatform6);

        pane.getChildren().addAll(background, door, door2, key, fire, box, box2, box3, box4, character, pig, cannon, platform, boundary.getBoundary(), bloodpane, blackScreen, pauseMenu, diedPane);

        scalePane(pane, scale, backgroundWidth, backgroundHeight);

        VolumeController.playMusic("level2");

        scene = new Scene(pane, 1300, 700);

        initPauseOverlay();
        initDiedLayout();

        PerspectiveCamera camera = new PerspectiveCamera();
        scene.setCamera(camera);
        camera.setScaleX(scope);
        camera.setScaleY(scope);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1.0/60), e -> {
            controller.update();
            togglePause();
            if (character.getHealth() <= 0 && !isPaused) {
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> {
                    showDiedLayout();
                    diedPane.setVisible(true);
                });
                isPaused = true;
                pause.play();
            }
            if (!controller.isStopped || character.getHealth() <= 0) {
                character.applyGravity();
                pig1.update();
                pig2.update();
                pig3.update();
                pig4.update();
                pig5.update();
                cannon.update();
                door.update();
                door2.update();
                key.update();
                box.update();
                box2.update();
                box3.update();
                box4.update();
                fire1.update();
                fire2.update();
                fire3.update();
                fire4.update();
                fire5.update();
                fire6.update();
                if(character.getIsAttacked()){
                    if(character.getHealth()<=0){
                        pane.getChildren().remove(bloodpane);
                    }
                    newblood.setFitWidth(character.getHealthBarLength());
                    character.setIsAttacked(false);
                }
    
                if (door2.nextlevel) {
                    nextlevel();
                }
                if(character.attackstate()){
                    character.attackstateupdate();
                }
                if (controller.isStopped) {
                    gamestop();
                }
    
                double newCameraX = (character.characterBoundingBox.getCenterX() * scale - (scene.getWidth()/2*scope));
                double newCameraY = (character.characterBoundingBox.getCenterY() * scale - (scene.getHeight()/2*scope));
            
                newCameraX = Math.max(newCameraX, 0);
                newCameraX = Math.min(newCameraX, (backgroundWidth * scale - scene.getWidth() * scope));
    
                newCameraY = Math.max(newCameraY, 0);
                newCameraY = Math.min(newCameraY, (backgroundHeight * scale - scene.getHeight() * scope));
                camera.setTranslateX(newCameraX);
                camera.setTranslateY(newCameraY);
                bloodpane.setTranslateX(newCameraX * 2);
                bloodpane.setTranslateY(newCameraY * 2);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

		character.requestFocus();
        character.setFocusTraversable(true);

        return scene;
    }

    private void scalePane(Pane pane, double scale, double width, double height) {
        double offsetX = width * (1 - scale) / 4;
        double offsetY = height * (1 - scale) / 4;

        pane.setScaleX(scale);
        pane.setScaleY(scale);

        pane.setLayoutX(-offsetX);
        pane.setLayoutY(-offsetY);
    }

    public void gamestop(){
        timeline.stop();
    }

    public void nextlevel() {
        timeline.stop();
        VolumeController.stopMusic("level2");
        Platform.runLater(() -> Main.setLevel(3));
    }

    private void initDiedLayout() {
        ImageView diedImage = new ImageView(new Image(getClass().getResourceAsStream("death_screen.png")));

        diedImage.setLayoutX(200);
        diedImage.setLayoutY(60);
        Rectangle diedScreen = new Rectangle(scene.getWidth() * scale, scene.getHeight() * scale, Color.rgb(0, 0, 0, 0.7));
        Button homeButton = createUrmButton(2);
        homeButton.setLayoutX(225);
        homeButton.setLayoutY(160);
        homeButton.setScaleX(0.7);
        homeButton.setScaleY(0.7);
        homeButton.setOnAction(e -> {
            timeline.stop();
            VolumeController.stopMusic("level2");
            Main.backToMenu();
        });

        Button restartButton = createUrmButton(1);
        restartButton.setLayoutX(330);
        restartButton.setLayoutY(160);
        restartButton.setScaleX(0.7);
        restartButton.setScaleY(0.7);

        restartButton.setOnAction(e -> {
            timeline.stop();
            VolumeController.stopMusic("level2");
            VolumeController.stopSound("die");
            Main.setLevel(2);
        });
        diedPane.getChildren().addAll(diedScreen, diedImage, homeButton, restartButton);
        diedPane.setVisible(false);
    }

    private void showDiedLayout() {
        if (scene != null) {
            PerspectiveCamera camera = (PerspectiveCamera) scene.getCamera();
            double cameraX = camera.getTranslateX();
            double cameraY = camera.getTranslateY();
            diedPane.setLayoutX((cameraX + (scene.getWidth() * scale - diedPane.getWidth()) / 2) * 2);
            diedPane.setLayoutY((cameraY + (scene.getHeight() * scale - diedPane.getHeight()) / 2) * 2);
        }
        diedPane.setVisible(true);
        VolumeController.stopMusic("level2");
    }

    private void initPauseOverlay() {
        ImageView pauseMenuImage = new ImageView(new Image(getClass().getResourceAsStream("pause_menu.png")));
        pauseMenuImage.setFitWidth(206.4);
        pauseMenuImage.setFitHeight(311.2);
        pauseMenuImage.setLayoutX(0);
        pauseMenuImage.setLayoutY(-10);
        Rectangle pauseScreen = new Rectangle(scene.getWidth() * scope, scene.getHeight() * scope, Color.rgb(0, 0, 0, 0));
        Button musicButton = createSoundsButton(0, VolumeController.musicMute);
        musicButton.setLayoutX(125);
        musicButton.setLayoutY(75);
        musicButton.setScaleX(0.7);
        musicButton.setScaleY(0.7);
        Button soundButton = createSoundsButton(1, VolumeController.soundMute);
        soundButton.setLayoutX(125);
        soundButton.setLayoutY(110);
        soundButton.setScaleX(0.7);
        soundButton.setScaleY(0.7);
        Image volumeBarImage = new Image(getClass().getResourceAsStream("volume_bar.png"));
        ImageView volumeBar = new ImageView(volumeBarImage);
        volumeBar.setLayoutX(-5);
        volumeBar.setLayoutY(190);
        volumeBar.setScaleX(0.7);
        volumeBar.setScaleY(0.7);
        Button volumeButton = createVolumeButton();
        volumeButton.setScaleX(0.7);
        volumeButton.setScaleY(0.7);
        Button homeButton = createUrmButton(2);
        homeButton.setLayoutX(12);
        homeButton.setLayoutY(223);
        homeButton.setScaleX(0.7);
        homeButton.setScaleY(0.7);
        homeButton.setOnAction(e -> {
            timeline.stop();
            VolumeController.stopMusic("level2");
            Main.backToMenu();
        });

        Button playButton = createUrmButton(0);
        playButton.setLayoutX(66);
        playButton.setLayoutY(223);
        playButton.setScaleX(0.7);
        playButton.setScaleY(0.7);

        playButton.setOnAction(e -> {
            controller.isStopped = false;
            togglePause();
        });

        Button restartButton = createUrmButton(1);
        restartButton.setLayoutX(120);
        restartButton.setLayoutY(223);
        restartButton.setScaleX(0.7);
        restartButton.setScaleY(0.7);

        restartButton.setOnAction(e -> {
            timeline.stop();
            VolumeController.stopMusic("level2");
            Main.setLevel(2);
        });

        Button invisibleButton = createUrmButton(1);
        invisibleButton.setLayoutX(340);
        invisibleButton.setLayoutY(253);
        invisibleButton.setScaleX(0.7);
        invisibleButton.setScaleY(0.7);
        invisibleButton.setVisible(false);

        pauseMenu.getChildren().addAll(pauseScreen, pauseMenuImage, musicButton, soundButton, volumeBar, volumeButton, playButton, homeButton, restartButton, invisibleButton); 
        pauseMenu.setVisible(false);  
    }

    public void togglePause() {
        if (controller.isStopped) {
            blackScreen.setVisible(true);
        } else {
            blackScreen.setVisible(false);
        }
        pauseMenu.setVisible(controller.isStopped); 
        updatePauseOverlayPosition();
    }

    private void updatePauseOverlayPosition() {
        if (scene != null && controller.isStopped) {
            PerspectiveCamera camera = (PerspectiveCamera) scene.getCamera();
            double cameraX = camera.getTranslateX();
            double cameraY = camera.getTranslateY();
            pauseMenu.setLayoutX((cameraX + (scene.getWidth() * scale - pauseMenu.getWidth()) / 2) * 2);
            pauseMenu.setLayoutY((cameraY + (scene.getHeight() * scale - pauseMenu.getHeight()) / 2) * 2);
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
        double startX = 10;
        double endX = 150;
        ImageView imageView = new ImageView(volumeButtonImage);

        imageView.setViewport(new Rectangle2D(0, 0, buttonWidth, buttonHeight));
        button.setGraphic(imageView);
        button.setLayoutX(VolumeController.totalVolume * (endX - startX) + startX);
        button.setLayoutY(186);

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