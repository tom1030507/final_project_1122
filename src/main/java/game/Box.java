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
    private Image img_idle = new Image(getClass().getResourceAsStream("08-Box/Idle.png"));
    private Image img_hit = new Image(getClass().getResourceAsStream("08-Box/Hit.png"));
    private Image img_dia = new Image(getClass().getResourceAsStream("Diamond.png"));
    private Image img_diaef = new Image(getClass().getResourceAsStream("Diamond effect.png"));
    private Image img_on = new Image(getClass().getResourceAsStream("09-Bomb/Bomb On (52x56).png"));
    private Image img_boom = new Image(getClass().getResourceAsStream("09-Bomb/Boooooom (52x56).png"));
    private ImageView imageview = new ImageView(img_idle);
    private double x,y;
    private SpriteAnimation diaAnimation,boomAnimation,onAnimation,booomAnimation;
    private Character targetPlayer;
    private Boolean exist=true;
    private int type=0;
    private BoundingBox boundingBox;

    public Box(double x, double y, int t) {
        this.x=x;
        this.y=y;
        type=t;
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);

        diaAnimation = new SpriteAnimation(imageview,Duration.millis(500),4,4,0,0,24,24);
        diaAnimation.setCycleCount(Animation.INDEFINITE);

        if(type==1){
            boomAnimation = new SpriteAnimation(imageview,Duration.millis(500),4,4,0,0,24,24);
            boomAnimation.setCycleCount(1);
            boundingBox = new BoundingBox(x, y, 28, 22);
        }
        else{
            onAnimation = new SpriteAnimation(imageview,Duration.millis(500),4,4,0,0,52,56);
            onAnimation.setCycleCount(1);
            booomAnimation = new SpriteAnimation(imageview,Duration.millis(500),6,6,0,0,52,56);
            booomAnimation.setCycleCount(1);
            boundingBox = new BoundingBox(x, y, 28, 22);
        }
    }
    
    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }

    public void takeDamage(Double damage) {
        imageview.setImage(img_hit);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), e -> {
            exist=false;
            if(type==1){
                imageview.setImage(img_dia);
                diaAnimation.play();
            }
            else if(type==2){
                imageview.setImage(img_on);
                imageview.setTranslateX(x-13);
                imageview.setTranslateY(y-19);
                boundingBox=new BoundingBox(x-13,y-19,52,56);
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void update() {
        if(type==0) return;
        else{
            if(targetPlayer.attackstate() && exist){
                if(targetPlayer.getlightBoundingBox().intersects(boundingBox) && targetPlayer.getIsUsingLight()){
                    takeDamage(targetPlayer.getPower()*3);
                }
                else if(targetPlayer.getattackBoundingBox().intersects(boundingBox)){
                    takeDamage(targetPlayer.getPower());
                }
            }
            if(!exist && type==1){
                if(targetPlayer.getcharacterBoundingBox().intersects(boundingBox)){
                    VolumeController.playSound("get_box");
                    targetPlayer.setUseLight(true);
                    imageview.setImage(img_diaef);
                    boomAnimation.setOnFinished(e ->{
                        getChildren().clear();
                    });
                    type=0;
                    boomAnimation.play();
                }
            }
            else if(!exist && type==2){
                onAnimation.play();
                type=0;
                onAnimation.setOnFinished(e ->{
                    VolumeController.playSound("bomb");
                    imageview.setImage(img_boom);
                    if(targetPlayer.getcharacterBoundingBox().intersects(boundingBox)){
                        targetPlayer.takeDamage(1.0);;
                        
                    }
                    booomAnimation.setOnFinished(ex ->{
                        getChildren().clear();
                    
                    });
                    booomAnimation.play();
                });
            }
        }
    }
}
