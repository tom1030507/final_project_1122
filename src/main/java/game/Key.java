package game;

import javafx.animation.Animation;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Key extends Pane{
    private Image keyImage = new Image(getClass().getResourceAsStream("key.png"));
    private ImageView imageView = new ImageView(keyImage);
    private Character targetPlayer;
    private boolean used = true;
    private SpriteAnimation idleAnimation;
    private BoundingBox boundingBox;

    public void stopAnimation(){
        if (idleAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            idleAnimation.stop();
        }
    }

    public Key(double x,double y){
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        getChildren().add(imageView);

        idleAnimation = new SpriteAnimation(imageView,Duration.millis(1000),8,8,0,0,24,24);
        idleAnimation.setCycleCount(Animation.INDEFINITE);
        idleAnimation.play();

        boundingBox = new BoundingBox(x, y, 24, 24);
    }

    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }

    public void update(){
        if(used){
            if(targetPlayer.getCharacterBoundingBox().intersects(boundingBox)){
                targetPlayer.setKeyExists(true);
                used=false;
                getChildren().clear();
                VolumeController.playSound("get_box");
            }
        }
    }
}
