package game;

import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Door extends Pane{
    Image img_idle = new Image(getClass().getResourceAsStream("11-Door/Idle.png"));
    Image img_open = new Image(getClass().getResourceAsStream("11-Door/Opening (46x56).png"));
    Image img_close = new Image(getClass().getResourceAsStream("11-Door/Closiong (46x56).png"));
    ImageView imageview = new ImageView(img_idle);
    SpriteAnimation openAnimation,closeAnimation;
    Character targetPlayer;
    boolean used=true, nextlevel = false;

    BoundingBox boundingBox;

    public Door(double x,double y){
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);

        openAnimation = new SpriteAnimation(imageview,Duration.millis(500),5,5,0,0,46,56);
        openAnimation.setCycleCount(1);
        closeAnimation = new SpriteAnimation(imageview,Duration.millis(500),3,3,0,0,46,56);
        closeAnimation.setCycleCount(1);

        boundingBox = new BoundingBox(x, y, 46, 56);
    }

    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }

    public void action(){
        used=false;
        targetPlayer.setIsAttached(false);
        VolumeController.playSound("open_door");
        imageview.setImage(img_open);
        openAnimation.play();
        openAnimation.setOnFinished(e -> { 
            imageview.setImage(img_close);
            closeAnimation.play();
            closeAnimation.setOnFinished(e2 -> {
                nextlevel = true;
            });
        });
    }

    public void update(){
        if(used && targetPlayer.getKeyExists()){
            if(targetPlayer.characterBoundingBox.intersects(boundingBox)){
                targetPlayer.setIsAttached(true);
                if(targetPlayer.getIsPressed()){
                    action();
                }
            }
            else{
                targetPlayer.setIsAttached(false);
            }
        }
    }
}
