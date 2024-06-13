package game;

import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Door extends Pane{
    private Image idleImage = new Image(getClass().getResourceAsStream("11-Door/Idle.png"));
    private Image openedImage = new Image(getClass().getResourceAsStream("11-Door/Opening (46x56).png"));
    private Image closedImage = new Image(getClass().getResourceAsStream("11-Door/Closiong (46x56).png"));
    private ImageView imageView = new ImageView(idleImage);
    private SpriteAnimation openAnimation,closeAnimation;
    private Character targetPlayer;
    private boolean used = true, nextlevel = false;
    private BoundingBox boundingBox;

    public Door(double x,double y){
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        getChildren().add(imageView);

        openAnimation = new SpriteAnimation(imageView,Duration.millis(500),5,5,0,0,46,56);
        openAnimation.setCycleCount(1);
        closeAnimation = new SpriteAnimation(imageView,Duration.millis(500),3,3,0,0,46,56);
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
        imageView.setImage(openedImage);
        openAnimation.play();
        openAnimation.setOnFinished(e -> { 
            imageView.setImage(closedImage);
            closeAnimation.play();
            closeAnimation.setOnFinished(e2 -> {
                nextlevel = true;
            });
        });
    }

    public void update(){
        if(used && targetPlayer.getKeyExists()){
            if(targetPlayer.getCharacterBoundingBox().intersects(boundingBox)){
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

    public void setUsed(boolean used){
        this.used=used;
    }

    public boolean getNextLevel(){
        return nextlevel;
    }
}
