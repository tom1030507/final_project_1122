package game;

import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Character extends Pane {
    Image img_run = new Image(getClass().getResourceAsStream("01-King Human/Run (78x58).png"));
    Image img_attack = new Image(getClass().getResourceAsStream("01-King Human/Attack (78x58).png"));
    ImageView imageview = new ImageView(img_run);
    double velocityY = 0;
    double gravity = 0.2;
    double jumpStrength = 7;
    boolean isJumping = false;
    SpriteAnimation walkAnimation,attackAnimation;
    double speed = 3;

    public Character(double x, double y) {
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);

        // 初始化动画
        walkAnimation = new SpriteAnimation(imageview,Duration.millis(500),8,8,0,0,78,58);
        walkAnimation.setCycleCount(Animation.INDEFINITE);
    }

    public double getX(){
        return imageview.getTranslateX();
    }

    public double getY(){
        return imageview.getTranslateY();
    }

    public void move_right(double deltaTime) {
        if (!walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            imageview.setScaleX(1);
            walkAnimation.play();
        }
        imageview.setTranslateX(imageview.getTranslateX() + speed);
    }

    public void move_left(double deltaTime) {
        if (!walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            imageview.setScaleX(-1);
            walkAnimation.play();
        }
        imageview.setTranslateX(imageview.getTranslateX() - speed);
    }

    public void stop() {
        if (walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            walkAnimation.stop();
        }
    }

    public void move_jump() {
        if (!isJumping) {
            velocityY = -jumpStrength;
            isJumping = true;
        }
    }

    public void attack() {
        if (attackAnimation == null) { // 如果攻击动画对象为空，则初始化它
            attackAnimation = new SpriteAnimation(imageview, Duration.millis(500), 3, 3, 0, 0, 78, 58);
            attackAnimation.setCycleCount(1); // 攻击动画只播放一次
            attackAnimation.setOnFinished(e -> { // 当攻击动画播放完毕时，切换回跑步动画
                imageview.setImage(img_run);
            });
        }
        imageview.setImage(img_attack); // 设置攻击动画的第一帧
        attackAnimation.play();
    }

    public void applyGravity() {
        if (isJumping) {
            velocityY += gravity;
            imageview.setTranslateY(imageview.getTranslateY() + velocityY);
            if (imageview.getTranslateY() >= 300) { // Assuming ground level is at translateY = 0
                imageview.setTranslateY(300);
                velocityY = 0;
                isJumping = false;
            }
        }
    }
}

