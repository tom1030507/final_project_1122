package game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Box extends Pane{
    Image img_idle = new Image(getClass().getResourceAsStream("08-Box/Idle.png"));
    Image img_hit = new Image(getClass().getResourceAsStream("08-Box/Hit.png"));
    Image img_dia = new Image(getClass().getResourceAsStream("Diamond.png"));
    Image img_diaef = new Image(getClass().getResourceAsStream("Diamond effect.png"));
    ImageView imageview = new ImageView(img_idle);
    double x,y;
    SpriteAnimation diaAnimation,boomAnimation;
    Character targetPlayer;
    Boolean exist=true;
    Boolean end=false;

    BoundingBox boundingBox;

    public Box(double x, double y, int t) {
        this.x=x;
        this.y=y;
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);

        // 初始化动画
        diaAnimation = new SpriteAnimation(imageview,Duration.millis(500),4,4,0,0,24,24);
        diaAnimation.setCycleCount(Animation.INDEFINITE);

        boomAnimation = new SpriteAnimation(imageview,Duration.millis(500),4,4,0,0,24,24);
        boomAnimation.setCycleCount(1);

        boundingBox = new BoundingBox(x, y, 28, 22);
    }
    
    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }

    public void takeDamage(Double damage) {
        imageview.setImage(img_hit);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), e -> {
            exist=false;
            imageview.setImage(img_dia);
            diaAnimation.play();
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }


    public void update() {
        if(end) return;
        if(targetPlayer.attackstate() && exist){
            if(targetPlayer.llbox.intersects(boundingBox) && targetPlayer.isutl){
                takeDamage(targetPlayer.power*3);
            }
            else if(targetPlayer.attackBox.intersects(boundingBox)){
                takeDamage(targetPlayer.power);
            }
        }
        if(!exist){
            if(targetPlayer.boundingBox.intersects(boundingBox)){
                targetPlayer.utl=true;
                imageview.setImage(img_diaef);
                boomAnimation.setOnFinished(e ->{
                    end=true;
                    getChildren().clear();
                });
                boomAnimation.play();
            }
        }
    }
}
