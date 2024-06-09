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
import javafx.application.Platform;

public class Level1 implements Background {
    private int backgroundWidth = 1300, backgroundHeight = 700;
    private double scope = 0.5; // 摄像机缩放比例
    private ImageView background;
    private Pane pane = new Pane();
    private Scene scene;
    private Timeline timeline;

    public Scene createScene(Stage primaryStage) {
        background = new ImageView(new Image(getClass().getResourceAsStream("level1_background.png")));
        background.setFitWidth(backgroundWidth);
        background.setFitHeight(backgroundHeight);

		Character character = new Character(95, 590, 1);
        CharacterController controller = new CharacterController(character, 1);

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

        pane.getChildren().addAll(background, door, door2, key, character, pig, cannon, platform, boundary.getBoundary());

        scene = new Scene(pane, backgroundWidth, backgroundHeight);

        ParallelCamera camera = new ParallelCamera();
        scene.setCamera(camera);
        camera.setScaleX(scope);
        camera.setScaleY(scope);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1.0/60), e -> {
            controller.update();
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
            camera.setTranslateY(newCameraY-1);
            camera.setTranslateY(newCameraY);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        if (door2.nextlevel) {
            timeline.stop();
            Platform.runLater(() -> Main.setLevel(2));
        }
		character.requestFocus();
        character.setFocusTraversable(true);

        return scene;
    }

    public void nextlevel() {
        timeline.stop();
        Platform.runLater(() -> Main.setLevel(2));
    }
}