package game;

import javafx.animation.Animation;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Character extends Pane {
    Image img_run = new Image(getClass().getResourceAsStream("01-King Human/Run (78x58).png"));
    Image img_attack = new Image(getClass().getResourceAsStream("01-King Human/Attack (78x58).png"));
    Image img_hit = new Image(getClass().getResourceAsStream("01-King Human/Hit (78x58).png"));
    Image img_dead = new Image(getClass().getResourceAsStream("01-King Human/Dead (78x58).png"));
    ImageView imageview = new ImageView(img_run);
    double velocityY = 0;
    double gravity = 0.2;
    double jumpStrength = 7;
    double full=5,health=5,power=1;
    boolean isJumping = false;
    boolean isattcking = false;
    SpriteAnimation walkAnimation,attackAnimation,hitAnimation,deadAnimation;
    double speed = 3;
    Line blood;
    double blong=30;

    Rectangle imageBoundary, realBoundary;
    BoundingBox boundingBox,attackBox;

    public Character(double x, double y) {
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);

        // 初始化动画
        walkAnimation = new SpriteAnimation(imageview,Duration.millis(500),8,8,0,0,78,58);
        walkAnimation.setCycleCount(Animation.INDEFINITE);

        blood=new Line(x+10,y+15,x+10+blong,y+15);
        blood.setStrokeWidth(3);
        blood.setStroke(Color.RED);

        realBoundary = new Rectangle(x + 10,y + 18, 30, 25);
        realBoundary.setStroke(Color.BLUE); // 邊界線顏色
        realBoundary.setFill(Color.TRANSPARENT); // 內部填充顏色

        boundingBox = new BoundingBox(x + 10, y + 18, 40, 25);
        attackBox = new BoundingBox(x, y , 78, 58);

        imageBoundary = new Rectangle(x, y, 78, 58);
        imageBoundary.setStroke(Color.RED); // 邊界線顏色
        imageBoundary.setFill(Color.TRANSPARENT); // 內部填充顏色
        getChildren().addAll(blood,imageBoundary, realBoundary);

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
        attackBox = new BoundingBox(imageview.getTranslateX(), imageview.getTranslateY() , 78, 58);

        blood.setStartX(imageview.getTranslateX() + 10);
        blood.setStartY(imageview.getTranslateY() + 13);
        blood.setEndX(imageview.getTranslateX() + 10 + blong);
        blood.setEndY(imageview.getTranslateY() + 13);
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
        attackBox = new BoundingBox(imageview.getTranslateX(), imageview.getTranslateY() , 78, 58);

        blood.setStartX(imageview.getTranslateX() + 30);
        blood.setStartY(imageview.getTranslateY() + 13);
        blood.setEndX(imageview.getTranslateX() + 30 + blong);
        blood.setEndY(imageview.getTranslateY() + 13);
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
        isattcking=true;
        imageview.setImage(img_attack); // 设置攻击动画的第一帧
        attackAnimation.play();
    }
    
    public boolean attackstate(){
        return isattcking;
    }

    public void takeDamage(Double damage) {
        System.out.println("att");
        health -= damage;
        blong = (health/full)*30.0;
        if (hitAnimation == null) { // 如果攻击动画对象为空，则初始化它
            hitAnimation = new SpriteAnimation(imageview, Duration.millis(500), 2, 2, 0, 0, 78, 58);
            hitAnimation.setCycleCount(1); // 攻击动画只播放一次
            hitAnimation.setOnFinished(e -> { // 当攻击动画播放完毕时，切换回跑步动画
                imageview.setImage(img_run);
            });
        }
        imageview.setImage(img_hit); // 设置攻击动画的第一帧
        hitAnimation.play();
        if (health <= 0) {
            // 敌人被击败，执行相应操作
            blood.setOpacity(0);
            defeat();
        }
    }

    public void defeat(){
        deadAnimation = new SpriteAnimation(imageview,Duration.millis(500),4,4,0,0,78,58);
        deadAnimation.setCycleCount(1);
        deadAnimation.setOnFinished(e -> { // 当攻击动画播放完毕时，切换回跑步动画
            //getChildren().clear();
            System.out.println("gg");
        });
        imageview.setImage(img_dead);
        deadAnimation.play();
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
            attackBox = new BoundingBox(imageview.getTranslateX(), imageview.getTranslateY() , 78, 58);
            blood.setStartX(imageview.getTranslateX() + 10);
            blood.setStartY(imageview.getTranslateY() + 13);
            blood.setEndX(imageview.getTranslateX() + 10 + blong);
            blood.setEndY(imageview.getTranslateY() + 13);
        } else {
            imageBoundary.setX(imageview.getTranslateX());
            realBoundary.setX(imageview.getTranslateX() + 30);
            boundingBox = new BoundingBox(imageview.getTranslateX() + 30, imageview.getTranslateY() + 18, 30, 25);
            attackBox = new BoundingBox(imageview.getTranslateX(), imageview.getTranslateY() , 78, 58);
            blood.setStartX(imageview.getTranslateX() + 30);
            blood.setStartY(imageview.getTranslateY() + 13);
            blood.setEndX(imageview.getTranslateX() + 30 + blong);
            blood.setEndY(imageview.getTranslateY() + 13);
        }
    }
    
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
}

