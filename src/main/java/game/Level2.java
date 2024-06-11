package game;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.PerspectiveCamera;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Level2 implements Background {
    private int backgroundWidth = 2600, backgroundHeight = 1400;
    private double scope = 0.25; // 摄像机缩放比例
    private double scale = 0.5; // 畫面缩放比例
    private ImageView background;
    private Pane pane = new Pane();
    private Timeline timeline;
    private MediaPlayer mediaPlayer;
    
    public Scene createScene(Stage primaryStage) {
        background = new ImageView(new Image(getClass().getResourceAsStream("level2_background.png")));
        background.setFitWidth(backgroundWidth);
        background.setFitHeight(backgroundHeight);

		Character character = new Character(2435, 1300, 2);
        CharacterController controller = new CharacterController(character, 2);

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

        pane.getChildren().addAll(background, door, door2, key, fire, character, pig, cannon, platform, boundary.getBoundary());
        
        scalePane(pane, scale, backgroundWidth, backgroundHeight);

        playMusic();

        Scene scene = new Scene(pane, 1300, 700);

        PerspectiveCamera  camera = new PerspectiveCamera();
        scene.setCamera(camera);
        camera.setScaleX(scope);
        camera.setScaleY(scope);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1.0/60), e -> {
            character.applyGravity();
            if(character.health>0){
                controller.update();
            }
            pig1.update();
            pig2.update();
            pig3.update();
            pig4.update();
            pig5.update();
            cannon.update();
            door.update();
            door2.update();
            key.update();
            fire1.update();
            fire2.update();
            fire3.update();
            fire4.update();
            fire5.update();
            fire6.update();

            if (door2.nextlevel) {
                nextlevel();
            }
            if(character.attackstate()){
                character.attackstateupdate();
            }
            if (controller.stop) {
                gamestop();
            }

            double newCameraX = (character.boundingBox.getCenterX() * scale - (scene.getWidth()/2*scope));
            double newCameraY = (character.boundingBox.getCenterY() * scale - (scene.getHeight()/2*scope));
            // 限制摄像机X轴范围
            newCameraX = Math.max(newCameraX, 0);
            newCameraX = Math.min(newCameraX, (backgroundWidth * scale - scene.getWidth() * scope));

            // 限制摄像机Y轴范围
            newCameraY = Math.max(newCameraY, 0);
            newCameraY = Math.min(newCameraY, (backgroundHeight * scale - scene.getHeight() * scope));
            camera.setTranslateX(newCameraX);
            camera.setTranslateY(newCameraY);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

		character.requestFocus();
        character.setFocusTraversable(true);

        return scene;
    }

    private void scalePane(Pane pane, double scale, double width, double height) {
        // 计算缩放后的偏移量，以确保Scene的左上角是原点
        double offsetX = width * (1 - scale) / 4;
        double offsetY = height * (1 - scale) / 4;

        // 设置Pane的缩放比例
        pane.setScaleX(scale);
        pane.setScaleY(scale);
    
        // 设置Pane的偏移量，以确保缩放后Scene的左上角是原点
        pane.setLayoutX(-offsetX);
        pane.setLayoutY(-offsetY);
    }

    public void gamestop(){
        timeline.stop();
    }

    public void nextlevel() {
        timeline.stop();
        Platform.runLater(() -> Main.setLevel(3));
    }

    public void playMusic() {
        String audioUriPath = new File("src/main/resources/game/level2_music.mp3").toURI().toString();
        Media level1Audio = new Media(audioUriPath);
    	mediaPlayer = new MediaPlayer(level1Audio);
        mediaPlayer.play();
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }
}