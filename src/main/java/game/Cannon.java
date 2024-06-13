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
    private Image idleImage = new Image(getClass().getResourceAsStream("10-Cannon/Idle.png"));
    private Image shootImage = new Image(getClass().getResourceAsStream("10-Cannon/Shoot (44x28).png"));
    private Image ballImage = new Image(getClass().getResourceAsStream("10-Cannon/Cannon Ball.png"));
    private Image boomImage = new Image(getClass().getResourceAsStream("09-Bomb/Boooooom (52x56).png"));
    private ImageView imageView = new ImageView(idleImage);
    private ArrayList<ImageView> bullet = new ArrayList<ImageView>();
    private ArrayList<BoundingBox> bulletBox = new ArrayList<BoundingBox>();
    private ArrayList<Integer> bulletAvailable = new ArrayList<Integer>();
    private int pointer = 0;
    private double x, y, endx, endy;
    private SpriteAnimation shootAnimation, boomAnimation;
    private double fullHealth = 3, health = 3, power = 1;
    private double speed = 3;
    private Character targetPlayer;
    private Line blood;
    private double bloodLength=24;
    private int direction = 1;
    private int modX, modX2, modX3;

    BoundingBox boundingBox;

    public Cannon(double x, double y, double endx, double endy , int dirction) {
        this.x=x;
        this.y=y;
        this.endx=endx;
        this.endy=endy;
        this.direction=dirction;
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        imageView.setScaleX(dirction);
        getChildren().add(imageView);

        if(dirction==-1){
            modX=3;
            modX2=13;
            modX3=5;
        }
        else{
            modX=17;
            modX2=18;
            modX3=-5;
        }

        shootAnimation = new SpriteAnimation(imageView,Duration.millis(500),4,4,0,0,44,28);
        shootAnimation.setCycleCount(1);

        blood=new Line(x+modX,y+3,x+modX+bloodLength,y+3);
        blood.setStrokeWidth(3);
        blood.setStroke(Color.RED);

        boundingBox = new BoundingBox(x + modX, y + 6, 24, 19);
        getChildren().addAll(blood);
    }


    public void shoot() {
        imageView.setImage(shootImage);
        shootAnimation.play();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.07), e -> {
            if(exist){
                ImageView ball = new ImageView(ballImage);
                ball.setTranslateX(x+modX3);
                ball.setTranslateY(y-5);
                ball.setScaleX(direction);
                BoundingBox ball_box = new BoundingBox(x + modX2, y + 7, 13, 13);
                
                getChildren().addAll(ball);
                bullet.add(ball);
                bulletBox.add(ball_box);
                bulletAvailable.add(1);
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void move(){
        for(int i=pointer;i<bullet.size();i++){
            if(bulletAvailable.get(i)==0) continue;
            bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() - speed*direction);
            bulletBox.set(i,new BoundingBox(bullet.get(i).getTranslateX() + modX2-modX3 - speed*direction, bullet.get(i).getTranslateY()+ 7 , 13, 13));
            if(targetPlayer.getCharacterBoundingBox().intersects(bulletBox.get(i)) && exist){
                VolumeController.playSound("bomb");
                targetPlayer.takeDamage(power);
                boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),6,6,0,0,52,56);
                bullet.get(i).setImage(boomImage);
                boomAnimation.setCycleCount(1);
                boomAnimation.play();
                bulletAvailable.set(i,0);
                int[] tmp={i};
                boomAnimation.setOnFinished(e -> { 
                    getChildren().remove(bullet.get(tmp[0]));
                });
            }
            if(direction==1){
                if(bullet.get(i).getTranslateX()<=endx){
                    pointer=i+1;
                    boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),6,6,0,0,52,56);
                    bullet.get(i).setImage(boomImage);
                    boomAnimation.setCycleCount(1);
                    boomAnimation.play();
                    bulletAvailable.set(i,0);
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
                    bullet.get(i).setImage(boomImage);
                    boomAnimation.setCycleCount(1);
                    boomAnimation.play();
                    bulletAvailable.set(i,0);
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
        bloodLength = (health/fullHealth)*24.0;
        blood.setEndX(x+modX+bloodLength);
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
        if(targetPlayer.attackState()){
            if(targetPlayer.getLightBoundingBox().intersects(boundingBox) && targetPlayer.getIsUsingLight()){
                takeDamage(targetPlayer.getPower()*3);
            }
            else if(targetPlayer.getAttackBoundingBox().intersects(boundingBox)){
                takeDamage(targetPlayer.getPower());
            }
        }
    }
}
