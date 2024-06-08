package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static int level = 1;

	public void start(Stage primaryStage) {
        switch (level) {
            case 0:
                Background menu = new Menu();
                Scene scene = menu.createScene(primaryStage);
                primaryStage.setTitle("Menu");
                primaryStage.setScene(scene);
                primaryStage.show();
                break;
            case 1:
                Background level1 = new Level1();
                Scene scene1 = level1.createScene(primaryStage);
                primaryStage.setTitle("Level 1");
                primaryStage.setScene(scene1);
                primaryStage.show();
                break;
        }
	}
    
    public static void main(String[] args) {
      	launch(args);
    }
}
