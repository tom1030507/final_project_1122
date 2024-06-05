package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    boolean moveRight=false, moveLeft=false;

	public void start(Stage primaryStage) {
        int backgroundWidth=1300,backgroundHeight=700;
        double scope=0.5;

        ImageView background = new ImageView(new Image(getClass().getResourceAsStream("background.png")));
        background.setFitWidth(backgroundWidth);
        background.setFitHeight(backgroundHeight);

        Pane pane = new Pane();
		Character character = new Character(50,600);
        Enemy pig = new Enemy(200, 600, 300, 600);
		pig.setTargetPlayer(character);

        character.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT) {
                moveRight = true;
            } else if (e.getCode() == KeyCode.LEFT) {
                moveLeft = true;
            } else if (e.getCode() == KeyCode.SPACE) {
                character.move_jump();
            } else if (e.getCode() == KeyCode.Z){
                character.attack();
            }
            
        });
        character.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.RIGHT) {
                moveRight = false;
                character.stop();
            } else if (e.getCode() == KeyCode.LEFT) {
                moveLeft = false;
                character.stop();
            }
        });

		pane.getChildren().addAll(background, character, pig);

        Scene scene = new Scene(pane, backgroundWidth, backgroundHeight);

        ParallelCamera camera = new ParallelCamera();
        scene.setCamera(camera);
        camera.setScaleX(scope);
        camera.setScaleY(scope);

		primaryStage.setTitle("game"); 
		primaryStage.setScene(scene); 
		primaryStage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.0/60), e -> {
            if (moveRight) {
                character.move_right();
            }
            if (moveLeft) {
                character.move_left();
            }
            character.applyGravity();
            pig.update();
            double newCameraX = character.imageview.getTranslateX() - (scene.getWidth()/2*scope);
            double newCameraY = character.imageview.getTranslateY() - (scene.getHeight()/2*scope);
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
