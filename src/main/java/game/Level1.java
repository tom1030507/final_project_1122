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

public class Level1 {
    private int backgroundWidth = 1300, backgroundHeight = 700;
    private double scope = 0.5; // 摄像机缩放比例
    private ImageView background;
    private Pane pane = new Pane();

    public Scene createScene(Stage primaryStage) {
        background = new ImageView(new Image(getClass().getResourceAsStream("background.png")));
        background.setFitWidth(backgroundWidth);
        background.setFitHeight(backgroundHeight);

		Character character = new Character(100, 590, 1);
        CharacterController controller = new CharacterController(character, 1);

        Boundary boundary = new Boundary(1);
        
        Pig pig1 = new Pig(200, 612, 300, 612);
        Pig pig2 = new Pig(430, 220, 700, 220);
        Pig pig3 = new Pig(75, 270, 245, 270);
		pig1.setTargetPlayer(character);
        pig2.setTargetPlayer(character);
        pig3.setTargetPlayer(character);
        Pane pig = new Pane();
        pig.getChildren().addAll(pig1, pig2, pig3);        

        Cannon cannon=new Cannon(75,377,1100,377);
        cannon.setTargetPlayer(character);
        
        character.setOnKeyPressed(e -> {
            controller.handleKeyPressed(e.getCode());
        });

        character.setOnKeyReleased(e -> {
            controller.handleKeyReleased(e.getCode());
        });

        Platform shortPlatform1 = new Platform(1, 600, 400);
        Platform shortPlatform2 = new Platform(1, 800, 450);
        Platform shortPlatform3 = new Platform(1, 810, 210);
        Platform longPlatform1 = new Platform(2, 75, 300);
        Platform longPlatform2 = new Platform(2, 175, 300);
        Platform longPlatform3 = new Platform(2, 75, 200);
        Platform longPlatform4 = new Platform(2, 430, 250);
        Platform longPlatform5 = new Platform(2, 530, 250);
        Platform longPlatform6 = new Platform(2, 630, 250);
        Pane platform = new Pane();
        platform.getChildren().addAll(shortPlatform1, shortPlatform2, shortPlatform3, longPlatform1, longPlatform2, longPlatform3, longPlatform4, longPlatform5, longPlatform6);

        pane.getChildren().addAll(background, character, pig, cannon, platform, boundary.getBoundary());

        Scene scene = new Scene(pane, backgroundWidth, backgroundHeight);

        ParallelCamera camera = new ParallelCamera();
        scene.setCamera(camera);
        camera.setScaleX(scope);
        camera.setScaleY(scope);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.0/60), e -> {
            controller.update();
            pig1.update();
            pig2.update();
            pig3.update();
            cannon.update();
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
            camera.setTranslateY(newCameraY-1);
            camera.setTranslateY(newCameraY);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

		character.requestFocus();
        character.setFocusTraversable(true);

        return scene;
    }
}