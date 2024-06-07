package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public void start(Stage primaryStage) {
        Level1 mainScene = new Level1();
        Scene scene = mainScene.createScene(primaryStage);
        primaryStage.setTitle("game");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
    
    public static void main(String[] args) {
      	launch(args);
    }
}
