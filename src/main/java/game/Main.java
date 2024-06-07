package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    boolean moveRight=false, moveLeft=false;

	public void start(Stage primaryStage) {
        int backgroundWidth = 1300,backgroundHeight = 700;
        double scope = 0.5; // 摄像机缩放比例

        ImageView background = new ImageView(new Image(getClass().getResourceAsStream("background.png")));
        background.setFitWidth(backgroundWidth);
        background.setFitHeight(backgroundHeight);

        Pane pane = new Pane();
		Character character = new Character(100, 590);
        CharacterController controller = new CharacterController(character);

        Boundary boundary = new Boundary();

        Enemy pig = new Enemy(200, 590, 300, 596);
		pig.setTargetPlayer(character);

        character.setOnKeyPressed(e -> {
            controller.handleKeyPressed(e.getCode());
        });

        character.setOnKeyReleased(e -> {
            controller.handleKeyReleased(e.getCode());
        });

		pane.getChildren().addAll(background, character, pig, boundary.getBoundary());

        Scene scene = new Scene(pane, backgroundWidth, backgroundHeight);

        ParallelCamera camera = new ParallelCamera();
        scene.setCamera(camera);
        camera.setScaleX(scope);
        camera.setScaleY(scope);

		primaryStage.setTitle("game"); 
		primaryStage.setScene(scene); 
		primaryStage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.0/60), e -> {
            controller.update();

            pig.update();
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
	}
    
    public static void main(String[] args) {
      	launch(args);
    }


}
