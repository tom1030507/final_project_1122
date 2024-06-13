package game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Fire extends Pane{
    private Image fireImage = new Image(getClass().getResourceAsStream("fire.png"));
    private Image runImage = new Image(getClass().getResourceAsStream("fire_run.png"));
    private ImageView imageView = new ImageView(fireImage);
    private SpriteAnimation fireAnimation;
    private Character targetPlayer;
    private double power = 1;
    private boolean used = true;
    private BoundingBox boundingBox;

    public Fire(double x,double y){
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        getChildren().add(imageView);

        fireAnimation = new SpriteAnimation(imageView,Duration.millis(1000),8,8,0,0,33,69);
        fireAnimation.setCycleCount(Animation.INDEFINITE);
        imageView.setImage(runImage);
        fireAnimation.play();

        boundingBox = new BoundingBox(x, y+10, 33, 69);
    }

    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }

    public void update(){
        if(used){
            imageView.setOpacity(1);
            if(targetPlayer.getFireTime()){
                if(targetPlayer.getCharacterBoundingBox().intersects(boundingBox)){
                    targetPlayer.setFireTime(false);
                    targetPlayer.takeDamage(power);
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                        targetPlayer.setFireTime(true);
                    }));
                    timeline.setCycleCount(1);
                    timeline.play();
                }
            }
        }
        else{
            imageView.setOpacity(0);
        }
        
    }

    public void setUsed(boolean used){
        this.used=used;
    }
}
