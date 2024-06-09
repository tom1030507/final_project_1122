package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {

    private static int level = 2;
    private static Stage primaryStage;

	public void start(Stage Stage) {
        primaryStage = Stage;
        launchLevel();
	}

    public static void launchLevel() {
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
            case 2:
                Background level2 = new Level2();
                Scene scene2 = level2.createScene(primaryStage);
                primaryStage.setTitle("Level 2");
                primaryStage.setScene(scene2);
                primaryStage.show();
                break;
        }
    }
    
    public static void setLevel(int newLevel) {
        level = newLevel;
        launchLevel();
    }

    public static void main(String[] args) {
      	launch(args);
    }
}
