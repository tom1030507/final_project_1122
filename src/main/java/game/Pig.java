package game;

import javafx.animation.Animation;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Pig extends Pane {
    Image img_run = new Image(getClass().getResourceAsStream("03-Pig/Run (34x28).png"));
    Image img_attack = new Image(getClass().getResourceAsStream("03-Pig/Attack (34x28).png"));
    Image img_hit = new Image(getClass().getResourceAsStream("03-Pig/Hit (34x28).png"));
    Image img_dead = new Image(getClass().getResourceAsStream("03-Pig/Dead (34x28).png"));
    ImageView imageview = new ImageView(img_run);
    double x,y,endx,endy;
    SpriteAnimation walkAnimation,attackAnimation,hitAnimation,deadAnimation;
    int sta=0;
    double full=3,health=3,power=1;
    boolean isattcking=false;
    double speed=2;
    Character targetPlayer;
    double attackRange = 150;
    Line blood;
    double blong=20;

    BoundingBox boundingBox,attackBox;

    public Pig(double x, double y, double endx, double endy) {
        this.x=x;
        this.y=y;
        this.endx=endx;
        this.endy=endy;
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);

        walkAnimation = new SpriteAnimation(imageview,Duration.millis(500),6,6,0,0,34,28);
        walkAnimation.setCycleCount(Animation.INDEFINITE);

        blood=new Line(x+9,y+7,x+9+blong,y+7);
        blood.setStrokeWidth(3);
        blood.setStroke(Color.RED);

        boundingBox = new BoundingBox(x + 9, y + 10, 20, 17);
        attackBox = new BoundingBox(x , y , 34, 28);

        getChildren().add(blood);
    }

    boolean lastMoveLeft = true;
    boolean lastMoveRight = false;

    public void move_right() {
        imageview.setScaleX(-1);
        if (!walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            walkAnimation.play();
        }
        imageview.setTranslateX(imageview.getTranslateX() + speed);

        lastMoveRight = true;
        if (lastMoveLeft) { 
            imageview.setTranslateX(imageview.getTranslateX());
            lastMoveLeft = false;
        }

        boundingBox = new BoundingBox(imageview.getTranslateX() + 5, imageview.getTranslateY() + 10, 20, 17);
        attackBox = new BoundingBox(imageview.getTranslateX(), imageview.getTranslateY(), 34, 28);
        
        blood.setStartX(imageview.getTranslateX() + 5);
        blood.setStartY(imageview.getTranslateY() + 7);
        blood.setEndX(imageview.getTranslateX() + 5 + blong);
        blood.setEndY(imageview.getTranslateY() + 7);
    }

    public void move_left() {
        imageview.setScaleX(1);
        if (!walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            walkAnimation.play();
        }
        imageview.setTranslateX(imageview.getTranslateX() - speed);

        lastMoveLeft = true;
        if (lastMoveRight) {
            imageview.setTranslateX(imageview.getTranslateX());
            lastMoveRight = false;
        }

        boundingBox = new BoundingBox(imageview.getTranslateX() + 9, imageview.getTranslateY() + 10, 20, 17);
        attackBox = new BoundingBox(imageview.getTranslateX(), imageview.getTranslateY(), 34, 28);
        
        blood.setStartX(imageview.getTranslateX() + 9);
        blood.setStartY(imageview.getTranslateY() + 7);
        blood.setEndX(imageview.getTranslateX() + 9 + blong);
        blood.setEndY(imageview.getTranslateY() + 7);
    }

    public void move_stop() {
        if (walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            walkAnimation.stop();
        }
    }
    
    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }

    public void takeDamage(Double damage) {
        health -= damage;
        blong = (health/full)*20.0;
        if(lastMoveLeft){
            blood.setStartX(imageview.getTranslateX() + 9);
            blood.setStartY(imageview.getTranslateY() + 7);
            blood.setEndX(imageview.getTranslateX() + 9 + blong);
            blood.setEndY(imageview.getTranslateY() + 7);
        }
        else{
            blood.setStartX(imageview.getTranslateX() + 5);
            blood.setStartY(imageview.getTranslateY() + 7);
            blood.setEndX(imageview.getTranslateX() + 5 + blong);
            blood.setEndY(imageview.getTranslateY() + 7);
        }
        if (hitAnimation == null) { 
            hitAnimation = new SpriteAnimation(imageview, Duration.millis(500), 2, 2, 0, 0, 34, 28);
            hitAnimation.setCycleCount(1);
            hitAnimation.setOnFinished(e -> { 
                imageview.setImage(img_run);
            });
        }
        imageview.setImage(img_hit); 
        hitAnimation.play();
        if (health <= 0) {
            blood.setOpacity(0);
            defeat();
        }
    }

    public void defeat(){
        deadAnimation = new SpriteAnimation(imageview,Duration.millis(500),4,4,0,0,34,28);
        deadAnimation.setCycleCount(1);
        deadAnimation.setOnFinished(e -> {
            getChildren().clear();
        });
        imageview.setImage(img_dead);
        deadAnimation.play();
        exist=false;
    }

    boolean exist=true;

    public void update() {
        if (!exist){
            return;
        }
        if(targetPlayer.attackstate()){
            if(targetPlayer.llbox.intersects(boundingBox) && targetPlayer.isutl){
                takeDamage(targetPlayer.power*3);
            }
            else if(targetPlayer.attackBox.intersects(boundingBox)){
                takeDamage(targetPlayer.power);
            }
            
        }
        
        if(sta==0){
            move_right();
        }
        else if(sta==1){
            move_left();
        }
        else if(sta==2){
            move_stop();
        }
        double distance =Math.abs(targetPlayer.getX() - imageview.getTranslateX());
        if (distance <= attackRange && Math.abs(targetPlayer.getY() - imageview.getTranslateY())<=50 && targetPlayer.health>0) {
            if(imageview.getTranslateX()<=x && imageview.getTranslateY()==y){
                if(targetPlayer.getX() - imageview.getTranslateX()>=0){
                    sta=0;
                }
                else{
                    sta=2;
                }
            }
            else if(imageview.getTranslateX()>=endx && imageview.getTranslateY()==endy){
                if(targetPlayer.getX() - imageview.getTranslateX()>=-5){
                    sta=2;
                }
                else{
                    sta=1;
                }
            }
            else if(targetPlayer.getX() - imageview.getTranslateX()>=0){
                sta=0;
            }
            else if(targetPlayer.getX() - imageview.getTranslateX()<0){
                sta=1;
            }
            else{
                sta=2;
            }
            if(!isattcking){
                isattcking=true;
                attack();
            }
        }
        else{
            if (imageview.getTranslateX()<=x && imageview.getTranslateY()==y) {
                sta=0;
            }
            if (imageview.getTranslateX()>=endx && imageview.getTranslateY()==endy) {
                sta=1;
            }
        }
    }

    public void walk(){
        blood.setOpacity(0);
        if(sta==0){
            move_right();
        }
        else if(sta==1){
            move_left();
        }
        else if(sta==2){
            move_stop();
        }
        if (imageview.getTranslateX()<=x && imageview.getTranslateY()==y) {
            sta=0;
        }
        if (imageview.getTranslateX()>=endx && imageview.getTranslateY()==endy) {
            sta=1;
        }
    }

    public void attack() {
        if (attackAnimation == null) {
            attackAnimation = new SpriteAnimation(imageview, Duration.millis(500), 5, 5, 0, 0, 34, 28);
            attackAnimation.setCycleCount(1); 
            attackAnimation.setOnFinished(e -> {
                imageview.setImage(img_run);
                isattcking=false;
            });
        }
        if(targetPlayer.health<=0){
            return;
        }
        imageview.setImage(img_attack);
        attackAnimation.play();
        if(targetPlayer.boundingBox.intersects(attackBox)){
            targetPlayer.takeDamage(power);
        }
    }
}

