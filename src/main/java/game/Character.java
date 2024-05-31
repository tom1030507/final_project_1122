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
    boolean isJumping = false;

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

    public void move_jump() {
        if (!isJumping) {
            velocityY = -jumpStrength;
            isJumping=true;
        }
    }

    public void applyGravity() {
        if (isJumping) {
            velocityY += gravity;
            imageview.setTranslateY(imageview.getTranslateY() + velocityY);
            if (imageview.getTranslateY() >= y) { // Assuming ground level is at translateY = 0
                imageview.setTranslateY(y);
                velocityY = 0;
                isJumping=false;
            }
        }
    }
}
