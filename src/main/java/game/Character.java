package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Character extends Text{
    int x=10,y=10;
    // Image image=new Image("");
    // ImageView imageview=new ImageView(image);

    public Character(){

    }

    public Character(int x,int y){
        this.x=x;
        this.y=y;
        setX(x);
        setY(y);
        setText("asdasd");;
    }

    public void move_right(){
        x++;
        setX(x);
    }

    public void move_left(){
        x--;
        setX(x);
    }

    public void move_jump(){

    }
}
