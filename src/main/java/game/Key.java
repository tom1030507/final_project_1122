package game;

import javafx.animation.Animation;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Key extends Pane{
    Image img_key = new Image(getClass().getResourceAsStream("key.png"));
    ImageView imageview = new ImageView(img_key);
    Character targetPlayer;
    boolean used=true;
    SpriteAnimation idleAnimation;

    Rectangle realBoundary;
    BoundingBox boundingBox;

    public Key(double x,double y){
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);

        idleAnimation = new SpriteAnimation(imageview,Duration.millis(1000),8,8,0,0,24,24);
        idleAnimation.setCycleCount(Animation.INDEFINITE);
        idleAnimation.play();

        realBoundary = new Rectangle(x, y, 24, 24);
        realBoundary.setStroke(Color.BLUE); // 邊界線顏色
        realBoundary.setFill(Color.TRANSPARENT); // 內部填充顏色

        boundingBox = new BoundingBox(x, y, 24, 24);
        getChildren().add(realBoundary);
    }

    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }

    public void update(){
        if(used){
            if(targetPlayer.boundingBox.intersects(boundingBox)){
                targetPlayer.keyexist=true;
                used=false;
                getChildren().clear();
            }
        }
    }
}
