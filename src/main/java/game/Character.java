package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Character extends Pane{
    double x=0,y=0;
    Image image=new Image(getClass().getResourceAsStream("King.png"));
    ImageView imageview=new ImageView(image);
    double velocityY = 0;
    double gravity = 0.5;
    double jumpStrength = 10;

    public Character(double x,double y){
        this.x=x;
        this.y=y;
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);
    }

    public void move_right(){
        x+=3;
        imageview.setTranslateX(x);
    }

    public void move_left(){
        x-=3;
        imageview.setTranslateX(x);
    }

    public boolean move_jump(boolean moveJump) {
        if (moveJump) {
            velocityY = -jumpStrength;
            return false;
        }
        return true;
    }

    public boolean applyGravity(boolean isJumping) {
        if (isJumping) {
            velocityY += gravity;
            imageview.setTranslateY(imageview.getTranslateY() + velocityY);
            if (imageview.getTranslateY() >= y) { // Assuming ground level is at translateY = 0
                imageview.setTranslateY(y);
                velocityY = 0;
                return false;
            }
            return true;
        }
        return false;
    }
}
