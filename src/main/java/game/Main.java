package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static int level = 3;
    private static String[] title = {"Menu", "Level 1", "Level 2", "Level 3"};
    private static Stage primaryStage;
    private static Background background;

	public void start(Stage stage) {
        primaryStage = stage;
        launchLevel(0);
	}

    public static void launchLevel(int level) {
        switch (level) {
            case 0:
                background = new Menu();
                break;
            case 1:
                background = new Level1();
                break;
            case 2:
                background = new Level2();
                break;
            case 3:
                background = new Level3();
                break;
            default:
                System.out.println("Invalid level: " + level);
                return; // 處理無效的level值
        }
        Scene scene = background.createScene(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setTitle(title[level]);
        primaryStage.show();
    }
    
    public static void setLevel(int newLevel) {
        level = newLevel;
        launchLevel(level);
    }

    public static void backToMenu() {
        launchLevel(0);
    }

    public static void continueLevel() {
        launchLevel(level);
    }

    public static void initializeLevel() {
        level = 1;
    }

    public static void main(String[] args) {
      	launch(args);
    }
}
