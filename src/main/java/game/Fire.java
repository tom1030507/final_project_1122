package game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Fire extends Pane{
    Image img_fire = new Image(getClass().getResourceAsStream("fire.png"));
    Image img_run = new Image(getClass().getResourceAsStream("fire_run.png"));
    ImageView imageview = new ImageView(img_fire);
    SpriteAnimation fireAnimation;
    Character targetPlayer;
    double power=1;

    Rectangle realBoundary;
    BoundingBox boundingBox;

    public Fire(double x,double y){
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);


        fireAnimation = new SpriteAnimation(imageview,Duration.millis(1000),8,8,0,0,33,69);
        fireAnimation.setCycleCount(Animation.INDEFINITE);
        imageview.setImage(img_run);
        fireAnimation.play();

        realBoundary = new Rectangle(x, y+10, 33, 60);
        realBoundary.setStroke(Color.BLUE); // 邊界線顏色
        realBoundary.setFill(Color.TRANSPARENT); // 內部填充顏色

        boundingBox = new BoundingBox(x, y+10, 33, 69);
        getChildren().add(realBoundary);
    }

    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }

    public void update(){
        if(targetPlayer.firetime){
            if(targetPlayer.boundingBox.intersects(boundingBox)){
                targetPlayer.firetime=false;
                targetPlayer.takeDamage(power);
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                    targetPlayer.firetime=true;
                }));
                timeline.setCycleCount(1);
                timeline.play();
            }
        }
    }
}
