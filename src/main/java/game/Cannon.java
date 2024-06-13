package game;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Cannon extends Pane{
    Image img_idle = new Image(getClass().getResourceAsStream("10-Cannon/Idle.png"));
    Image img_shoot = new Image(getClass().getResourceAsStream("10-Cannon/Shoot (44x28).png"));
    Image img_ball = new Image(getClass().getResourceAsStream("10-Cannon/Cannon Ball.png"));
    Image img_boom = new Image(getClass().getResourceAsStream("09-Bomb/Boooooom (52x56).png"));
    ImageView imageview = new ImageView(img_idle);
    ArrayList<ImageView> bullet=new ArrayList<ImageView>();
    ArrayList<BoundingBox> bullet_Box=new ArrayList<BoundingBox>();
    ArrayList<Integer> bullet_avail=new ArrayList<Integer>();
    int pointer=0;
    double x,y,endx,endy;
    SpriteAnimation shootAnimation,boomAnimation;
    int sta=0;
    double full=3,health=3,power=1;
    boolean isattcking=false;
    double speed=3;
    Character targetPlayer;
    Line blood;
    double blong=24;
    int dirction=1;
    int modx,modx2,modx3;

    BoundingBox boundingBox;

    public Cannon(double x, double y, double endx, double endy , int dirction) {
        this.x=x;
        this.y=y;
        this.endx=endx;
        this.endy=endy;
        this.dirction=dirction;
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        imageview.setScaleX(dirction);
        getChildren().add(imageview);

        if(dirction==-1){
            modx=3;
            modx2=13;
            modx3=5;
        }
        else{
            modx=17;
            modx2=18;
            modx3=-5;
        }

        shootAnimation = new SpriteAnimation(imageview,Duration.millis(500),4,4,0,0,44,28);
        shootAnimation.setCycleCount(1);

        blood=new Line(x+modx,y+3,x+modx+blong,y+3);
        blood.setStrokeWidth(3);
        blood.setStroke(Color.RED);

        boundingBox = new BoundingBox(x + modx, y + 6, 24, 19);
        getChildren().addAll(blood);
    }


    public void shoot() {
        imageview.setImage(img_shoot);
        shootAnimation.play();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.07), e -> {
            if(exist){
                ImageView ball = new ImageView(img_ball);
                ball.setTranslateX(x+modx3);
                ball.setTranslateY(y-5);
                ball.setScaleX(dirction);
                BoundingBox ball_box = new BoundingBox(x + modx2, y + 7, 13, 13);
                
                getChildren().addAll(ball);
                bullet.add(ball);
                bullet_Box.add(ball_box);
                bullet_avail.add(1);
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void move(){
        for(int i=pointer;i<bullet.size();i++){
            if(bullet_avail.get(i)==0) continue;
            bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() - speed*dirction);
            bullet_Box.set(i,new BoundingBox(bullet.get(i).getTranslateX() + modx2-modx3 - speed*dirction, bullet.get(i).getTranslateY()+ 7 , 13, 13));
            if(targetPlayer.getcharacterBoundingBox().intersects(bullet_Box.get(i)) && exist){
                targetPlayer.takeDamage(power);
                boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),6,6,0,0,52,56);
                bullet.get(i).setImage(img_boom);
                boomAnimation.setCycleCount(1);
                boomAnimation.play();
                bullet_avail.set(i,0);
                int[] tmp={i};
                boomAnimation.setOnFinished(e -> { 
                    getChildren().remove(bullet.get(tmp[0]));
                });
            }
            if(dirction==1){
                if(bullet.get(i).getTranslateX()<=endx){
                    pointer=i+1;
                    boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),6,6,0,0,52,56);
                    bullet.get(i).setImage(img_boom);
                    boomAnimation.setCycleCount(1);
                    boomAnimation.play();
                    bullet_avail.set(i,0);
                    int[] tmp={i};
                    boomAnimation.setOnFinished(e -> { 
                        getChildren().remove(bullet.get(tmp[0]));
                    });
                }
            }
            else{
                if(bullet.get(i).getTranslateX()>=endx){
                    pointer=i+1;
                    boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),6,6,0,0,52,56);
                    bullet.get(i).setImage(img_boom);
                    boomAnimation.setCycleCount(1);
                    boomAnimation.play();
                    bullet_avail.set(i,0);
                    int[] tmp={i};
                    boomAnimation.setOnFinished(e -> { 
                        getChildren().remove(bullet.get(tmp[0]));
                    });
                }
            }
        }
    }
    
    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }

    public void takeDamage(Double damage) {
        health -= damage;
        blong = (health/full)*24.0;
        blood.setEndX(x+modx+blong);
        if (health <= 0) {
            blood.setOpacity(0);
            defeat();
        }
    }

    public void defeat(){
        getChildren().clear();
        exist=false;
    }

    boolean exist=true;
    int count=0;

    public void update() {
        count++;
        if(count==120){
            count=0;
            if(exist) shoot();
        }
        move();
        if(targetPlayer.attackstate()){
            if(targetPlayer.getlightBoundingBox().intersects(boundingBox) && targetPlayer.getIsUsingLight()){
                takeDamage(targetPlayer.getPower()*3);
            }
            else if(targetPlayer.getattackBoundingBox().intersects(boundingBox)){
                takeDamage(targetPlayer.getPower());
            }
        }
    }
}
