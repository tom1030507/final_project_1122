package game;

import javafx.animation.Animation;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Key extends Pane{
    private Image img_key = new Image(getClass().getResourceAsStream("key.png"));
    private ImageView imageview = new ImageView(img_key);
    private Character targetPlayer;
    private boolean used=true;
    private SpriteAnimation idleAnimation;
    private BoundingBox boundingBox;

    public void stopanimation(){
        if (idleAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            idleAnimation.stop();
        }
    }

    public Key(double x,double y){
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);

        idleAnimation = new SpriteAnimation(imageview,Duration.millis(1000),8,8,0,0,24,24);
        idleAnimation.setCycleCount(Animation.INDEFINITE);
        idleAnimation.play();

        boundingBox = new BoundingBox(x, y, 24, 24);
    }

    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }

    public void update(){
        if(used){
            if(targetPlayer.getcharacterBoundingBox().intersects(boundingBox)){
                targetPlayer.setKeyExists(true);
                used=false;
                getChildren().clear();
                VolumeController.playSound("get_box");
            }
        }
    }
}
