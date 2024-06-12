package game;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.PerspectiveCamera;
import javafx.util.Duration;
import javafx.stage.Stage;

public class Level3 implements Background {
    private int backgroundWidth = 1300, backgroundHeight = 700;
    private double scope = 0.5; // 摄像机缩放比例
    private ImageView background;
    private Pane pane = new Pane(), pauseMenu = new Pane(), diedPane = new Pane();
    private Scene scene;
    Timeline timeline;
    CharacterController controller;

    public Scene createScene(Stage primaryStage) {
        background = new ImageView(new Image(getClass().getResourceAsStream("level3/level3_background.jpg")));
        background.setFitWidth(backgroundWidth);
        background.setFitHeight(backgroundHeight);

        Pane bloodpane = new Pane();
        ImageView blood2= new ImageView(new Image(getClass().getResourceAsStream("Big Bars/2.png")));
        ImageView blood4= new ImageView(new Image(getClass().getResourceAsStream("Big Bars/4.png")));
        ImageView blood= new ImageView(new Image(getClass().getResourceAsStream("Big Bars/blood.png")));
        blood4.setTranslateX(32*6);
        blood.setTranslateX(16);
        blood.setTranslateY(13);
        blood.setFitWidth(32*7-18);
        blood.setFitHeight(4);
        for(int i=1;i<6;i++){
            ImageView blood3= new ImageView(new Image(getClass().getResourceAsStream("Big Bars/3.png")));
            blood3.setTranslateX(32*i);
            bloodpane.getChildren().add(blood3);
        }
        bloodpane.getChildren().addAll(blood2,blood4,blood);

		Character character = new Character(600, 590, 3);
        controller = new CharacterController(character, 3);

        Boundary boundary = new Boundary(3);
        
        Door door=new Door(604,591);
        door.setTargetPlayer(character);
        door.used=false;

        Door door2=new Door(604,253);
        door2.setTargetPlayer(character);
        door2.imageview.setOpacity(0);
        door2.realBoundary.setOpacity(0);

        Boss boss=new Boss(501, 60, character);

        Pane fire = new Pane();
        ArrayList<Fire> boss_left=new ArrayList<Fire>();
        ArrayList<Fire> boss_right=new ArrayList<Fire>();

        for(int i=0;i<10;i++){
            Fire fire1=new Fire(170+33*i, 243);
            fire1.setTargetPlayer(character);
            fire1.used=false;
            boss_left.add(fire1);
            Fire fire2=new Fire(750+33*i, 243);
            fire2.setTargetPlayer(character);
            fire2.used=true;
            boss_right.add(fire2);
            fire.getChildren().addAll(fire1,fire2);
        }

        int[] x1={133,73,106,139,1186};
        int[] x2={1083,1120,1153,1186,73};
        int[] y={285,485,485,485,385};
        ArrayList<Fire> stair1=new ArrayList<Fire>();
        ArrayList<Fire> stair2=new ArrayList<Fire>();

        for(int i=0;i<5;i++){
            Fire fire1=new Fire(x1[i], y[i]);
            fire1.setTargetPlayer(character);
            stair1.add(fire1);
            Fire fire2=new Fire(x2[i], y[i]);
            fire2.setTargetPlayer(character);
            fire2.used=false;
            stair2.add(fire2);
            fire.getChildren().addAll(fire1,fire2);
        }

        character.setOnKeyPressed(e -> {
            controller.handleKeyPressed(e.getCode());
        });

        character.setOnKeyReleased(e -> {
            controller.handleKeyReleased(e.getCode());
        });

        woodPlatform shortPlatform1 = new woodPlatform(1, 75, 450);
        woodPlatform shortPlatform2 = new woodPlatform(1, 132, 350);
        woodPlatform shortPlatform3 = new woodPlatform(1, 1187, 450);
        woodPlatform shortPlatform4 = new woodPlatform(1, 1085, 350);
        woodPlatform longPlatform1 = new woodPlatform(2, 75, 550);
        woodPlatform longPlatform2 = new woodPlatform(2, 1122, 550);

        Pane platform = new Pane();
        platform.getChildren().addAll(shortPlatform1, shortPlatform2, shortPlatform3, shortPlatform4, longPlatform1, longPlatform2);

        pane.getChildren().addAll(background, door, door2, fire, character, boss, platform, boundary.getBoundary(), bloodpane, pauseMenu, diedPane);

        scene = new Scene(pane, backgroundWidth, backgroundHeight);

        PerspectiveCamera camera = new PerspectiveCamera();
        scene.setCamera(camera);
        camera.setScaleX(scope);
        camera.setScaleY(scope);

        initPauseOverlay();
        initDiedLayout();

        int[] count={0,1,1};
        //boolean exist=true;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1.0/60), e -> {
            character.applyGravity();
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
                if(!boss.exist){
                    door2.imageview.setOpacity(1);
                    door2.realBoundary.setOpacity(1);
                    character.keyexist=true;
                }
                count[0]++;
                door.update();
                door2.update();
                boss.update(count[0]);
                if(boss.isattcking){
                    if(boss.health<=0){
                        pane.getChildren().remove(bloodpane);
                    }
                    blood.setFitWidth(boss.blong);
                    boss.isattcking=false;
                }
    
                if(count[0]==1800){
                    count[0]=0;
                    count[1]*=-1;
                    if(count[1]==1){
                        for(int i=0;i<10;i++){
                            boss_left.get(i).used=false;
                            boss_right.get(i).used=true;
                        }
                    }
                    else{
                        for(int i=0;i<10;i++){
                            boss_left.get(i).used=true;
                            boss_right.get(i).used=false;
                        }
                    }
                }
    
                if(count[0]%180==0){
                    count[2]*=-1;
                    if(count[2]==1){
                        for(int i=0;i<5;i++){
                            stair1.get(i).used=true;
                            stair2.get(i).used=false;
                        }
                    }
                    else{
                        for(int i=0;i<5;i++){
                            stair1.get(i).used=false;
                            stair2.get(i).used=true;
                        }
                    }
                }
    
                if(count[0]%180==120){
                    for(int i=0;i<5;i++){
                        stair1.get(i).used=false;
                        stair2.get(i).used=false;
                    }
                }
    
                for(int i=0;i<10;i++){
                    boss_left.get(i).update();
                    boss_right.get(i).update();
                    if(i<5){
                        stair1.get(i).update();
                        stair2.get(i).update();
                    }
                }
    
                if(character.attackstate()){
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
                bloodpane.setTranslateX(newCameraX+213);
                bloodpane.setTranslateY(newCameraY);
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

    public void setScope() {
        return;
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
            Main.backToMenu();
        });

        Button restartButton = createUrmButton(1);
        restartButton.setLayoutX(330);
        restartButton.setLayoutY(160);
        restartButton.setScaleX(0.7);
        restartButton.setScaleY(0.7);

        restartButton.setOnAction(e -> {
            timeline.stop();
            VolumeController.stopMusic("level1");
            Main.setLevel(3);
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
            // VolumeController.stopMusic("level3");
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
            Main.setLevel(3);
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