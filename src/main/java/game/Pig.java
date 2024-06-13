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
    private Image runImage = new Image(getClass().getResourceAsStream("03-Pig/Run (34x28).png"));
    private Image attackImage = new Image(getClass().getResourceAsStream("03-Pig/Attack (34x28).png"));
    private Image hitImage = new Image(getClass().getResourceAsStream("03-Pig/Hit (34x28).png"));
    private Image deadImage = new Image(getClass().getResourceAsStream("03-Pig/Dead (34x28).png"));
    private ImageView imageView = new ImageView(runImage);
    private double x, y, endX, endY;
    private SpriteAnimation walkAnimation,attackAnimation,hitAnimation,deadAnimation;
    private int status = 0;
    private double fullHealth = 3, health = 3, power = 1;
    private boolean iSattacking = false;
    private double speed = 2;
    private Character targetPlayer;
    private double attackRange = 150;
    private Line blood;
    private double bloodLength = 20;
    private BoundingBox boundingBox, attackBox;
    private boolean lastMoveLeft = true;
    private boolean lastMoveRight = false;
    private boolean isExists = true;

    public Pig(double x, double y, double endx, double endy) {
        this.x=x;
        this.y=y;
        this.endX=endx;
        this.endY=endy;
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        getChildren().add(imageView);

        walkAnimation = new SpriteAnimation(imageView,Duration.millis(500),6,6,0,0,34,28);
        walkAnimation.setCycleCount(Animation.INDEFINITE);

        blood=new Line(x+9,y+7,x+9+bloodLength,y+7);
        blood.setStrokeWidth(3);
        blood.setStroke(Color.RED);

        boundingBox = new BoundingBox(x + 9, y + 10, 20, 17);
        attackBox = new BoundingBox(x , y , 34, 28);

        getChildren().add(blood);
    }
    
    public void moveRight() {
        imageView.setScaleX(-1);
        if (!walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            walkAnimation.play();
        }
        imageView.setTranslateX(imageView.getTranslateX() + speed);

        lastMoveRight = true;
        if (lastMoveLeft) { 
            imageView.setTranslateX(imageView.getTranslateX());
            lastMoveLeft = false;
        }

        boundingBox = new BoundingBox(imageView.getTranslateX() + 5, imageView.getTranslateY() + 10, 20, 17);
        attackBox = new BoundingBox(imageView.getTranslateX(), imageView.getTranslateY(), 34, 28);
        
        blood.setStartX(imageView.getTranslateX() + 5);
        blood.setStartY(imageView.getTranslateY() + 7);
        blood.setEndX(imageView.getTranslateX() + 5 + bloodLength);
        blood.setEndY(imageView.getTranslateY() + 7);
    }

    public void moveLeft() {
        imageView.setScaleX(1);
        if (!walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            walkAnimation.play();
        }
        imageView.setTranslateX(imageView.getTranslateX() - speed);

        lastMoveLeft = true;
        if (lastMoveRight) {
            imageView.setTranslateX(imageView.getTranslateX());
            lastMoveRight = false;
        }

        boundingBox = new BoundingBox(imageView.getTranslateX() + 9, imageView.getTranslateY() + 10, 20, 17);
        attackBox = new BoundingBox(imageView.getTranslateX(), imageView.getTranslateY(), 34, 28);
        
        blood.setStartX(imageView.getTranslateX() + 9);
        blood.setStartY(imageView.getTranslateY() + 7);
        blood.setEndX(imageView.getTranslateX() + 9 + bloodLength);
        blood.setEndY(imageView.getTranslateY() + 7);
    }

    public void moveStop() {
        if (walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            walkAnimation.stop();
        }
    }
    
    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }

    public void takeDamage(Double damage) {
        health -= damage;
        bloodLength = (health/fullHealth)*20.0;
        if(lastMoveLeft){
            blood.setStartX(imageView.getTranslateX() + 9);
            blood.setStartY(imageView.getTranslateY() + 7);
            blood.setEndX(imageView.getTranslateX() + 9 + bloodLength);
            blood.setEndY(imageView.getTranslateY() + 7);
        }
        else{
            blood.setStartX(imageView.getTranslateX() + 5);
            blood.setStartY(imageView.getTranslateY() + 7);
            blood.setEndX(imageView.getTranslateX() + 5 + bloodLength);
            blood.setEndY(imageView.getTranslateY() + 7);
        }
        if (hitAnimation == null) { 
            hitAnimation = new SpriteAnimation(imageView, Duration.millis(500), 2, 2, 0, 0, 34, 28);
            hitAnimation.setCycleCount(1);
            hitAnimation.setOnFinished(e -> { 
                imageView.setImage(runImage);
            });
        }
        imageView.setImage(hitImage); 
        hitAnimation.play();
        if (health <= 0) {
            blood.setOpacity(0);
            defeat();
        }
    }

    public void defeat(){
        deadAnimation = new SpriteAnimation(imageView,Duration.millis(500),4,4,0,0,34,28);
        deadAnimation.setCycleCount(1);
        deadAnimation.setOnFinished(e -> {
            getChildren().clear();
        });
        imageView.setImage(deadImage);
        deadAnimation.play();
        isExists=false;
    }

    public void update() {
        if (!isExists){
            return;
        }
        if(targetPlayer.attackState()){
            if(targetPlayer.getLightBoundingBox().intersects(boundingBox) && targetPlayer.getIsUsingLight()){
                takeDamage(targetPlayer.getPower()*3);
            }
            else if(targetPlayer.getAttackBoundingBox().intersects(boundingBox)){
                takeDamage(targetPlayer.getPower());
            }
            
        }
        
        if(status==0){
            moveRight();
        }
        else if(status==1){
            moveLeft();
        }
        else if(status==2){
            moveStop();
        }
        double distance =Math.abs(targetPlayer.getX() - imageView.getTranslateX());
        if (distance <= attackRange && Math.abs(targetPlayer.getY() - imageView.getTranslateY())<=50 && targetPlayer.getHealth()>0) {
            if(imageView.getTranslateX()<=x && imageView.getTranslateY()==y){
                if(targetPlayer.getX() - imageView.getTranslateX()>=0){
                    status=0;
                }
                else{
                    status=2;
                }
            }
            else if(imageView.getTranslateX()>=endX && imageView.getTranslateY()==endY){
                if(targetPlayer.getX() - imageView.getTranslateX()>=-5){
                    status=2;
                }
                else{
                    status=1;
                }
            }
            else if(targetPlayer.getX() - imageView.getTranslateX()>=0){
                status=0;
            }
            else if(targetPlayer.getX() - imageView.getTranslateX()<0){
                status=1;
            }
            else{
                status=2;
            }
            if(!iSattacking){
                iSattacking=true;
                attack();
            }
        }
        else{
            if (imageView.getTranslateX()<=x && imageView.getTranslateY()==y) {
                status=0;
            }
            if (imageView.getTranslateX()>=endX && imageView.getTranslateY()==endY) {
                status=1;
            }
        }
    }

    public void walk(){
        blood.setOpacity(0);
        if(status==0){
            moveRight();
        }
        else if(status==1){
            moveLeft();
        }
        else if(status==2){
            moveStop();
        }
        if (imageView.getTranslateX()<=x && imageView.getTranslateY()==y) {
            status=0;
        }
        if (imageView.getTranslateX()>=endX && imageView.getTranslateY()==endY) {
            status=1;
        }
    }

    public void attack() {
        if (attackAnimation == null) {
            attackAnimation = new SpriteAnimation(imageView, Duration.millis(500), 5, 5, 0, 0, 34, 28);
            attackAnimation.setCycleCount(1); 
            attackAnimation.setOnFinished(e -> {
                imageView.setImage(runImage);
                iSattacking=false;
            });
        }
        if(targetPlayer.getHealth()<=0){
            return;
        }
        imageView.setImage(attackImage);
        attackAnimation.play();
        if(targetPlayer.getCharacterBoundingBox().intersects(attackBox)){
            targetPlayer.takeDamage(power);
        }
    }
}

