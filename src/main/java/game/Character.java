package game;

import javafx.animation.Animation;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
public class Character extends Pane {
    private Image idleImage = loadImage("01-King Human/Idle (78x58).png");
    private Image runImage = loadImage("01-King Human/Run (78x58).png");
    private Image attackImage = loadImage("01-King Human/Attack (78x58).png");
    private Image hitImage = loadImage("01-King Human/Hit (78x58).png");
    private Image deadImage = loadImage("01-King Human/Dead (78x58).png");
    private Image doorInImage = loadImage("01-King Human/Door in (78x58).png");
    private Image lightImage = loadImage("light.png");
    private ImageView characterImageView = new ImageView(idleImage);
    private ImageView lightImageView = new ImageView(lightImage);
    private double velocityY = 0;
    private double gravity = 0.2;
    private double jumpStrength = 7;
    private double maxHealth = 5, health = 5, power = 1;
    private boolean isJumping = false;
    private boolean isAttcking = false;
    private boolean isAttacked = false;
    private SpriteAnimation idleAnimation, walkAnimation, attackAnimation, lightAnimation, hitAnimation, deadAnimation, doorinAnimation;
    private double speed = 3;
    private double healthBarLength = 142;
    private boolean coldTime = false;
    private boolean fireTime = true;
    private boolean keyExists = false;
    private boolean isPressed = false;
    private boolean isAttached = false;
    private boolean useLight = false;
    private boolean isUsingLight = false;
    private boolean lastMoveLeft = false;
    private boolean lastMoveRight = true;
    private Boundary boundary;
    private BoundingBox characterBoundingBox, attackBoundingBox, lightBoundingBox;

    public void stopAnimation(){
        if (idleAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            idleAnimation.stop();
        }
        if (walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            walkAnimation.stop();
        }
        if (hitAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            hitAnimation.stop();
        }
        if (deadAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            deadAnimation.stop();
        }
        if (attackAnimation == null) return;
        if (attackAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            attackAnimation.stop();
        }
    }

    public Character(double x, double y, int level) {
        characterImageView.setTranslateX(x);
        characterImageView.setTranslateY(y);
        getChildren().add(characterImageView);
        boundary = new Boundary(level);

        idleAnimation = new SpriteAnimation(characterImageView,Duration.millis(500),11,11,0,0,78,58);
        idleAnimation.setCycleCount(Animation.INDEFINITE);
        walkAnimation = new SpriteAnimation(characterImageView,Duration.millis(500),8,8,0,0,78,58);
        walkAnimation.setCycleCount(Animation.INDEFINITE);
        idleAnimation.play();

        characterBoundingBox = new BoundingBox(x + 10, y + 18, 40, 25);
        attackBoundingBox = new BoundingBox(x, y , 78, 58);

        lightImageView.setTranslateX(x+30);
        lightImageView.setTranslateY(y-10);
        lightImageView.setOpacity(0);

        lightBoundingBox = new BoundingBox(x+50, y+5 , 260, 55);

        getChildren().addAll(lightImageView);

    }

    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }

    public double getX(){
        return characterImageView.getTranslateX();
    }

    public double getY(){
        return characterImageView.getTranslateY();
    }

    

    public void moveRight() {
        if (health <= 0) return;
        if (!walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            characterImageView.setScaleX(1);
            lightImageView.setScaleX(1);
            characterImageView.setImage(runImage);
            idleAnimation.stop();
            walkAnimation.play();
        }
        characterImageView.setTranslateX(characterImageView.getTranslateX() + speed);

        lastMoveRight = true;
        if (lastMoveLeft) { 
            characterImageView.setTranslateX(characterImageView.getTranslateX() + 20);
            lastMoveLeft = false;
        }

        lightImageView.setTranslateX(characterImageView.getTranslateX()+30);
        lightBoundingBox = new BoundingBox(characterImageView.getTranslateX() + 50, characterImageView.getTranslateY() + 5, 260, 55);
        characterBoundingBox = new BoundingBox(characterImageView.getTranslateX() + 10, characterImageView.getTranslateY() + 18, 30, 25);
        attackBoundingBox = new BoundingBox(characterImageView.getTranslateX(), characterImageView.getTranslateY() , 78, 58);
    }

    public void moveLeft() {
        if (health <= 0) return;
        if (!walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            characterImageView.setScaleX(-1);
            lightImageView.setScaleX(-1);
            characterImageView.setImage(runImage);
            idleAnimation.stop();
            walkAnimation.play();
        }
        characterImageView.setTranslateX(characterImageView.getTranslateX() - speed);

        lastMoveLeft = true;
        if (lastMoveRight) {
            characterImageView.setTranslateX(characterImageView.getTranslateX() - 20);
            lastMoveRight = false;
        }

        lightImageView.setTranslateX(characterImageView.getTranslateX()-250);
        lightBoundingBox = new BoundingBox(characterImageView.getTranslateX()-240, characterImageView.getTranslateY() + 5, 260, 55);
        characterBoundingBox = new BoundingBox(characterImageView.getTranslateX() + 30, characterImageView.getTranslateY() + 18, 30, 25);
        attackBoundingBox = new BoundingBox(characterImageView.getTranslateX(), characterImageView.getTranslateY() , 78, 58);

    }

    public void stop() {
        if (walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            walkAnimation.stop();
        }
        if (!idleAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            characterImageView.setImage(idleImage);
            idleAnimation.play();
        }
    }

    public void jump() {
        Bounds bounds = new BoundingBox(characterBoundingBox.getMinX() + 4, characterBoundingBox.getMinY() + velocityY + 1, 20, 27);
        if (!isJumping && boundary.isWithinBounds(bounds)) {
            VolumeController.playSound("jump");
            velocityY = -jumpStrength;
            isJumping = true;
        }
    }

    public void attack() {
        if (coldTime){
            return;
        }
        if(useLight){
            light();
            return;
        }
        if (attackAnimation == null) {
            attackAnimation = new SpriteAnimation(characterImageView, Duration.millis(500), 3, 3, 0, 0, 78, 58);
            attackAnimation.setCycleCount(1);
            attackAnimation.setOnFinished(e -> { 
                coldTime=false;
                if (walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
                    characterImageView.setImage(runImage);
                }
                if (idleAnimation.getStatus().equals(Animation.Status.RUNNING)) {
                    characterImageView.setImage(idleImage);
                }
            });
        }
        isAttcking=true;
        coldTime=true;
        characterImageView.setImage(attackImage);
        attackAnimation.play();
        VolumeController.playSound("attack");
    }

    public void light(){
        if (lightAnimation == null) { 
            lightImageView.setOpacity(1);
            lightAnimation = new SpriteAnimation(lightImageView, Duration.millis(500), 6, 6, 0, 0, 300, 100);
            lightAnimation.setCycleCount(1); 
            lightAnimation.setOnFinished(e -> { 
                coldTime=false;
                if (walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
                    characterImageView.setImage(runImage);
                }
                if (idleAnimation.getStatus().equals(Animation.Status.RUNNING)) {
                    characterImageView.setImage(idleImage);
                }
            });
        }
        isAttcking=true;
        coldTime=true;
        isUsingLight=true;
        useLight=false;
        lightAnimation.play();
        VolumeController.playSound("lightning");
    }
    
    public void attackStateUpdate(){
        isAttcking=false;
        isUsingLight=false;
    }

    public boolean attackState(){
        return isAttcking;
    }

    public void takeDamage(Double damage) {
        if(health <= 0) return;
        isAttacked=true;
        health -= damage;
        healthBarLength = (health/maxHealth)*142.0;
        if (hitAnimation == null) { 
            hitAnimation = new SpriteAnimation(characterImageView, Duration.millis(500), 2, 2, 0, 0, 78, 58);
            hitAnimation.setCycleCount(1); 
            hitAnimation.setOnFinished(e -> { 
                if (walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
                    walkAnimation.stop();
                }
                if (!idleAnimation.getStatus().equals(Animation.Status.RUNNING)) {
                    characterImageView.setImage(idleImage);
                    idleAnimation.play();
                }
                else if (idleAnimation.getStatus().equals(Animation.Status.RUNNING)) {
                    characterImageView.setImage(idleImage);
                }
            });
        }
        VolumeController.playSound("get_hurt");
        
        if (health <= 0) {
            defeat();
        }
        else{
            characterImageView.setImage(hitImage); 
            hitAnimation.play();
        }
    }

    public void defeat(){
        deadAnimation = new SpriteAnimation(characterImageView,Duration.millis(1000),4,4,0,0,78,58);
        deadAnimation.setCycleCount(1);
        VolumeController.playSound("die");
        stopAnimation();
        characterImageView.setImage(deadImage);
        deadAnimation.play();
    }

    public void action(){
        if(keyExists && isPressed && isAttached){
            doorinAnimation = new SpriteAnimation(characterImageView,Duration.millis(1000),8,8,0,0,78,58);
            doorinAnimation.setCycleCount(1);
            characterImageView.setImage(doorInImage);
            if (walkAnimation.getStatus().equals(Animation.Status.RUNNING)) {
                walkAnimation.stop();
            }
            if (idleAnimation.getStatus().equals(Animation.Status.RUNNING)) {
                idleAnimation.stop();
            }
            doorinAnimation.play();
        }
    }

    public void applyGravity() {
        velocityY += gravity;
        double newY = characterImageView.getTranslateY() + velocityY;
        Bounds bounds = new BoundingBox(characterBoundingBox.getMinX() + 4, characterBoundingBox.getMinY() + velocityY + 1, 20, 25);
        if (!boundary.isWithinBounds(bounds)) {
            characterImageView.setTranslateY(newY);
        } else {
            if (velocityY >= 0.2) {
                isJumping = false;
            }
            velocityY = 0;
        }
        if (lastMoveRight) {
            lightImageView.setTranslateX(characterImageView.getTranslateX()+30);
            lightImageView.setTranslateY(characterImageView.getTranslateY()-10);
            lightBoundingBox = new BoundingBox(characterImageView.getTranslateX() + 50, characterImageView.getTranslateY() + 5, 260, 55);
            characterBoundingBox = new BoundingBox(characterImageView.getTranslateX() + 10, characterImageView.getTranslateY() + 18, 30, 25);
            attackBoundingBox = new BoundingBox(characterImageView.getTranslateX(), characterImageView.getTranslateY() , 78, 58);
        } else {
            lightImageView.setTranslateX(characterImageView.getTranslateX()-250);
            lightImageView.setTranslateY(characterImageView.getTranslateY()-10);
            lightBoundingBox = new BoundingBox(characterImageView.getTranslateX() - 240, characterImageView.getTranslateY() + 5, 260, 55);
            characterBoundingBox = new BoundingBox(characterImageView.getTranslateX() + 30, characterImageView.getTranslateY() + 18, 30, 25);
            attackBoundingBox = new BoundingBox(characterImageView.getTranslateX(), characterImageView.getTranslateY() , 78, 58);
        }
    }

    public double getHealth() {
        return health;
    }

    public double getPower() {
        return power;
    }

    public boolean getIsAttacked() {
        return isAttacked;
    }

    public void setIsAttacked(boolean isAttacked) {
        this.isAttacked = isAttacked;
    }
    
    public double getHealthBarLength() {
        return healthBarLength;
    }

    public boolean getFireTime() {
        return fireTime;
    }

    public void setFireTime(boolean fireTime) {
        this.fireTime = fireTime;
    }

    public boolean getKeyExists() {
        return keyExists;
    }

    public void setKeyExists(boolean keyExists) {
        this.keyExists = keyExists;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public void setIsAttached(boolean isAttached) {
        this.isAttached = isAttached;
    }

    public void setUseLight(boolean useLight) {
        this.useLight = useLight;
    }

    public boolean getIsUsingLight() {
        return isUsingLight;
    }

    public BoundingBox getCharacterBoundingBox(){
        return characterBoundingBox;
    } 

    public BoundingBox getLightBoundingBox(){
        return lightBoundingBox;
    } 

    public BoundingBox getAttackBoundingBox(){
        return attackBoundingBox;
    } 
}

