package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    boolean moveRight=false, moveLeft=false;

	public void start(Stage primaryStage) {
        ImageView test = new ImageView(new Image(getClass().getResourceAsStream("ttt.jpg")));
        test.setFitWidth(1300);
        test.setFitHeight(700);
        test.setTranslateX(-250);
        test.setTranslateY(-300);

        Pane pane=new Pane();
		Character character=new Character(50,300);
        Enemy pig=new Enemy(200, 314, 800, 314);
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
       
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (moveRight) {
                    character.move_right();
                }
                if (moveLeft) {
                    character.move_left();
                }
                character.applyGravity();
                character.imageview.setTranslateX(character.getX()+1);
                character.imageview.setTranslateX(character.getX()-1);
            }
        };
        timer.start();
		pane.getChildren().addAll(test,character,pig);

        Scene scene = new Scene(pane, 1300, 700);

        ParallelCamera camera = new ParallelCamera();
        scene.setCamera(camera);
        // //绑定摄像机的位置到玩家的位置
        camera.translateXProperty().bind(character.imageview.translateXProperty().subtract(scene.getWidth() / 2-100));
        camera.translateYProperty().bind(character.imageview.translateYProperty().subtract(scene.getHeight() / 2+100));

		primaryStage.setTitle("test"); 
		primaryStage.setScene(scene); 
		primaryStage.show();

		character.requestFocus();
        character.setFocusTraversable(true);
	}
    
    public static void main(String[] args) {
      	launch(args);
    }
}
