package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	boolean moveRight=false, moveLeft=false;

	public void start(Stage primaryStage) {
		Pane pane=new Pane();
		Character character=new Character(100,300);
        Enemy pig=new Enemy(200, 314, 700, 314);
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
            }
        };
        timer.start();
		pane.getChildren().addAll(character,pig);
        
		Scene scene = new Scene(pane, 1300, 700);
		primaryStage.setTitle("test"); 
		primaryStage.setScene(scene); 
		primaryStage.show();

		character.requestFocus();
	}
    
    public static void main(String[] args) {
      	launch(args);
    }
}
