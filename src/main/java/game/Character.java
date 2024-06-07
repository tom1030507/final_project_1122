package game;

import javafx.animation.Animation;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

    Rectangle imageBoundary, realBoundary;
    BoundingBox boundingBox;

    public Character(double x, double y) {
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);

        // 初始化动画
        walkAnimation = new SpriteAnimation(imageview,Duration.millis(500),8,8,0,0,78,58);
        walkAnimation.setCycleCount(Animation.INDEFINITE);

        realBoundary = new Rectangle(x + 10, y + 18, 30, 25);
        realBoundary.setStroke(Color.BLUE); // 邊界線顏色
        realBoundary.setFill(Color.TRANSPARENT); // 內部填充顏色

        boundingBox = new BoundingBox(x + 10, y + 18, 40, 25);

        imageBoundary = new Rectangle(x, y, 78, 58);
        imageBoundary.setStroke(Color.RED); // 邊界線顏色
        imageBoundary.setFill(Color.TRANSPARENT); // 內部填充顏色
        getChildren().addAll(imageBoundary, realBoundary);

    }

    public double getX(){
        return imageview.getTranslateX();
    }

    public double getY(){
        return imageview.getTranslateY();
    }

    public void playWalkAnimation() {
        walkAnimation.play();
    }

    boolean lastMoveLeft = false;
    boolean lastMoveRight = true;

    public void move_right() {
        if (!walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            imageview.setScaleX(1);
            walkAnimation.play();
        }
        imageview.setTranslateX(imageview.getTranslateX() + speed);

        lastMoveRight = true;
        if (lastMoveLeft) { 
            imageview.setTranslateX(imageview.getTranslateX() + 20);
            lastMoveLeft = false;
        }

        imageBoundary.setX(imageview.getTranslateX());
        realBoundary.setX(imageview.getTranslateX() + 10);
        boundingBox = new BoundingBox(imageview.getTranslateX() + 10, imageview.getTranslateY() + 18, 30, 25);
    }

    public void move_left() {
        if (!walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            imageview.setScaleX(-1);
            walkAnimation.play();
        }
        imageview.setTranslateX(imageview.getTranslateX() - speed);

        lastMoveLeft = true;
        if (lastMoveRight) {
            imageview.setTranslateX(imageview.getTranslateX() - 20);
            lastMoveRight = false;
        }

        imageBoundary.setX(imageview.getTranslateX());
        realBoundary.setX(imageview.getTranslateX() + 30);
        boundingBox = new BoundingBox(imageview.getTranslateX() + 30, imageview.getTranslateY() + 18, 30, 25);
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

    Boundary boundary = new Boundary();
    Rectangle rect;
    public void applyGravity() {
        velocityY += gravity;
        double newY = imageview.getTranslateY() + velocityY;
        Bounds bounds = new BoundingBox(boundingBox.getMinX() + 4, boundingBox.getMinY() + velocityY + 1, 20, 25);
        // rect = new Rectangle(boundingBox.getMinX() + 4, boundingBox.getMinY() + velocityY + 1, 20, 25);
        // rect.setStroke(Color.YELLOW);
        // rect.setFill(Color.TRANSPARENT);
        // getChildren().add(rect);
        if (!boundary.isWithinBounds(bounds)) {
            imageview.setTranslateY(newY);
        } else {
            velocityY = 0;
            isJumping = false;
        }

        imageBoundary.setY(imageview.getTranslateY());
        realBoundary.setY(imageview.getTranslateY() + 18);
        if (lastMoveRight) {
            imageBoundary.setX(imageview.getTranslateX());
            realBoundary.setX(imageview.getTranslateX() + 10);
            boundingBox = new BoundingBox(imageview.getTranslateX() + 10, imageview.getTranslateY() + 18, 30, 25);
        } else {
            imageBoundary.setX(imageview.getTranslateX());
            realBoundary.setX(imageview.getTranslateX() + 30);
            boundingBox = new BoundingBox(imageview.getTranslateX() + 30, imageview.getTranslateY() + 18, 30, 25);
        }
    }
    
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
}

