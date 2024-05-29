package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class Main extends Application {

	public void start(Stage primaryStage) {
		Pane pane=new Pane();
		Character character=new Character(100,100);
		character.setOnKeyPressed(e ->{
			if (e.getCode() == KeyCode.D){
				character.move_right();
			}
			if (e.getCode() == KeyCode.A) {
				character.move_left();
			}
		});

		pane.getChildren().add(character);
		
		Scene scene = new Scene(pane, 500, 300);
		primaryStage.setTitle("test"); 
		primaryStage.setScene(scene); 
		primaryStage.show();

		character.requestFocus();
	}

    public static void main(String[] args) {
      	launch(args);
    }
}
