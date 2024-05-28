package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class Main extends Application {

  public void start(Stage primaryStage) {
    Pane pane=new Pane();
    Text text=new Text(200,150,"hello world!");
    pane.getChildren().add(text);
    // Create a scene and place it in the stage
    Scene scene = new Scene(pane, 500, 300);
    primaryStage.setTitle("SliderDemo"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage   
  }
    public static void main(String[] args) {
      launch(args);
    }
}
