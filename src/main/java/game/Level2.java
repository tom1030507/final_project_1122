package game;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.ParallelCamera;
import javafx.util.Duration;
import javafx.stage.Stage;


public class Level2 implements Background {
    private int backgroundWidth = 2600, backgroundHeight = 1400;
    private double scope = 1; // 摄像机缩放比例
    private double scale = 0.5; // 畫面缩放比例
    private ImageView background;
    private Pane pane = new Pane();

    public Scene createScene(Stage primaryStage) {
        background = new ImageView(new Image(getClass().getResourceAsStream("level2_background.png")));
        background.setFitWidth(backgroundWidth);
        background.setFitHeight(backgroundHeight);

		Character character = new Character(150, 200, 2);
        CharacterController controller = new CharacterController(character, 2);

        Boundary boundary = new Boundary(2);
        
        Pig pig1 = new Pig(400, 1320, 660, 1320);
        Pig pig2 = new Pig(600, 787, 940, 787);
        Pig pig3 = new Pig(700, 360, 1100, 360);
        Pig pig4 = new Pig(1600, 245, 2000, 245);
        Pig pig5 = new Pig(1500, 1320, 2000, 1320);
		pig1.setTargetPlayer(character);
        pig2.setTargetPlayer(character);
        pig3.setTargetPlayer(character);
        pig4.setTargetPlayer(character);
        pig5.setTargetPlayer(character);
        Pane pig = new Pane();
        pig.getChildren().addAll(pig1, pig2, pig3, pig4, pig5);        

        Cannon cannon=new Cannon(75,377,1100,377);
        cannon.setTargetPlayer(character);
        
        character.setOnKeyPressed(e -> {
            controller.handleKeyPressed(e.getCode());
        });

        character.setOnKeyReleased(e -> {
            controller.handleKeyReleased(e.getCode());
        });

        Platform shortPlatform1 = new Platform(1, 1280, 1060);
        Platform shortPlatform2 = new Platform(1, 1440, 970);
        Platform shortPlatform3 = new Platform(1, 1120, 950);
        Platform shortPlatform4 = new Platform(1, 970, 860);
        Platform shortPlatform5 = new Platform(1, 450, 720);
        Platform shortPlatform6 = new Platform(1, 540, 610);
        Platform shortPlatform7 = new Platform(1, 1340, 340);
        Platform shortPlatform8 = new Platform(1, 2120, 830);
        Platform longPlatform1 = new Platform(2, 75, 1250);
        Platform longPlatform2 = new Platform(2, 175, 1250);
        Platform longPlatform3 = new Platform(2, 75, 1150);
        Platform longPlatform4 = new Platform(2, 1160, 1240);
        Platform longPlatform5 = new Platform(2, 600, 500);
        Platform longPlatform6 = new Platform(2, 460, 350);
        Pane platform = new Pane();
        platform.getChildren().addAll(shortPlatform1, shortPlatform2, shortPlatform3, shortPlatform4, shortPlatform5, shortPlatform6, shortPlatform7, shortPlatform8, longPlatform1, longPlatform2, longPlatform3, longPlatform4, longPlatform5, longPlatform6);

        pane.getChildren().addAll(background, character, pig, cannon, platform, boundary.getBoundary());
        
        scalePane(pane, scale, backgroundWidth, backgroundHeight);

        Scene scene = new Scene(pane, 1300, 700);

        ParallelCamera camera = new ParallelCamera();
        scene.setCamera(camera);
        camera.setScaleX(scope);
        camera.setScaleY(scope);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.0/60), e -> {
            controller.update();
            pig1.update();
            pig2.update();
            pig3.update();
            pig4.update();
            pig5.update();
            cannon.update();
            if(character.attackstate()){
                character.attackstateupdate();
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
            camera.setTranslateY(newCameraY-1);
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
}