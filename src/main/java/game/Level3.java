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

public class Level3 implements Background {
    private int backgroundWidth = 1300, backgroundHeight = 700;
    private double scope = 1; // 摄像机缩放比例
    private ImageView background;
    private Pane pane = new Pane();
    private Scene scene;

    public Scene createScene(Stage primaryStage) {
        background = new ImageView(new Image(getClass().getResourceAsStream("level3/level3_background.jpg")));
        background.setFitWidth(backgroundWidth);
        background.setFitHeight(backgroundHeight);

		Character character = new Character(630, 590, 3);
        CharacterController controller = new CharacterController(character, 3);

        Boundary boundary = new Boundary(3);
        
        Door door=new Door(630,591);
        door.setTargetPlayer(character);
        door.used=false;


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

        pane.getChildren().addAll(background, door, character, platform, boundary.getBoundary());

        scene = new Scene(pane, backgroundWidth, backgroundHeight);

        ParallelCamera camera = new ParallelCamera();
        scene.setCamera(camera);
        camera.setScaleX(scope);
        camera.setScaleY(scope);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.0/60), e -> {
            controller.update();
            door.update();

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

    public void setScope() {
        return;
    }
}