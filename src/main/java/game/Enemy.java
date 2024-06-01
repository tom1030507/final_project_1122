package game;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Enemy extends Pane {
    Image img_run = new Image(getClass().getResourceAsStream("03-Pig/Run (34x28).png"));
    Image img_attack = new Image(getClass().getResourceAsStream("03-Pig/Attack (34x28).png"));
    ImageView imageview = new ImageView(img_run);
    // double velocityY = 0;
    // double gravity = 0.3;
    // double jumpStrength = 10;
    // boolean isJumping = false;
    double x,y,endx,endy;
    SpriteAnimation walkAnimation,attackAnimation;
    int count=6;
    int columns=6;
    int offsetX=0;
    int offsetY=0;
    int width=34;
    int height=28;
    int sta=0;
    double speed=1;
    long pauseStartTime;
    boolean isPaused;
    long PAUSE_DURATION = 50_000_000;
    Character targetPlayer;
    double attackRange = 300; // 攻击范围
    long lastAttackTime = 0;
    long attackCooldown = 1000; // 攻击冷却时间，单位为毫秒
    AnimationTimer timer;


    public Enemy(double x, double y,double endx,double endy) {
        this.x=x;
        this.y=y;
        this.endx=endx;
        this.endy=endy;
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);

        // 初始化动画
        walkAnimation = new SpriteAnimation(imageview,Duration.millis(500),count,columns,offsetX,offsetY,width,height);
        walkAnimation.setCycleCount(Animation.INDEFINITE);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(now);
                if (isPaused) {
                    if (now - pauseStartTime >= PAUSE_DURATION) {
                        isPaused = false;
                    } else {
                        return; // Still pausing
                    }
                }
                if (imageview.getTranslateX()==x && imageview.getTranslateY()==y) {
                    sta=0;
                    pauseStartTime = now;
                    isPaused = true;
                }
                if (imageview.getTranslateX()==endx && imageview.getTranslateY()==endy) {
                    sta=1;
                    pauseStartTime = now;
                    isPaused = true;
                }
                if(sta==0){
                    move_right();
                }
                else if(sta==1){
                    move_left();
                }
            }
        };
        timer.start();

    }


    public void move_right() {
        imageview.setScaleX(-1);
        if (!walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            walkAnimation.play();
        }
        imageview.setTranslateX(imageview.getTranslateX() + speed);
    }

    public void move_left() {
        imageview.setScaleX(1);
        if (!walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            walkAnimation.play();
        }
        imageview.setTranslateX(imageview.getTranslateX() - speed);
    }

    public void stop() {
        if (walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            walkAnimation.stop();
        }
    }

    // public void move_jump() {
    //     if (!isJumping) {
    //         velocityY = -jumpStrength;
    //         isJumping = true;
    //     }
    // }
    
    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }
    
    public void update(long now) {
        if (now - lastAttackTime >= attackCooldown) {
            if (targetPlayer != null) {
                double distance = Math.sqrt(Math.pow(targetPlayer.getX() - imageview.getTranslateX(), 2) + Math.pow(targetPlayer.getY() - imageview.getTranslateY(), 2));
                if (distance <= attackRange) {
                    if((imageview.getTranslateX()==x && imageview.getTranslateY()==y) || (imageview.getTranslateX()==endx && imageview.getTranslateY()==endy)){
                        sta=2;
                    }
                    if(targetPlayer.getX() - imageview.getTranslateX()>=0){
                        imageview.setScaleX(-1);
                        //sta=0;
                    }
                    else{
                        imageview.setScaleX(1);
                        //sta=1;
                    }
                    attack(now); 
                }
                else{
                    if((imageview.getTranslateX()==x && imageview.getTranslateY()==y)){
                        sta=0;
                    }
                    else if(imageview.getTranslateX()==endx && imageview.getTranslateY()==endy){
                        sta=1;
                    }
                }
            }
        }
    }

    public void attack(long now) {
        if (attackAnimation == null) { // 如果攻击动画对象为空，则初始化它
            attackAnimation = new SpriteAnimation(imageview, Duration.millis(500), 5, 5, 0, 0, 34, 28);
            attackAnimation.setCycleCount(1); // 攻击动画只播放一次
            attackAnimation.setOnFinished(e -> { // 当攻击动画播放完毕时，切换回跑步动画
                imageview.setImage(img_run);
            });
        }
        imageview.setImage(img_attack); // 设置攻击动画的第一帧
        attackAnimation.play();
    }

    // public void applyGravity() {
    //     if (isJumping) {
    //         velocityY += gravity;
    //         imageview.setTranslateY(imageview.getTranslateY() + velocityY);
    //         if (imageview.getTranslateY() >= 300) { // Assuming ground level is at translateY = 0
    //             imageview.setTranslateY(300);
    //             velocityY = 0;
    //             isJumping = false;
    //         }
    //     }
    // }
}

