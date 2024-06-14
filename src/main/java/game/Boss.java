package game;

import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Boss extends Pane{
    private Image idleImage = new Image(getClass().getResourceAsStream("level3/boss1.png"));
    private Image attackImage = new Image(getClass().getResourceAsStream("level3/boss.png"));
    private Image note1Image = new Image(getClass().getResourceAsStream("level3/note1.png"));
    private Image note2Image = new Image(getClass().getResourceAsStream("level3/note2.png"));
    private Image note3Image = new Image(getClass().getResourceAsStream("level3/note3.png"));
    private Image redImage = new Image(getClass().getResourceAsStream("level3/red.png"));
    private Image boomImage = new Image(getClass().getResourceAsStream("level3/Boom.png"));
    private ImageView imageView = new ImageView(idleImage);
    private ArrayList<ImageView> bullet = new ArrayList<ImageView>();
    private ArrayList<BoundingBox> bulletBox = new ArrayList<BoundingBox>();
    private ArrayList<Integer> bulletType = new ArrayList<Integer>();
    private ArrayList<Integer> bulletVersion = new ArrayList<Integer>();
    private ArrayList<Integer> bulletDirection = new ArrayList<Integer>();
    private double x, y;
    private double left = 70, right = 1190, up = 160, down = 270;
    private SpriteAnimation bossAnimation, boomAnimation;
    private double full = 10, health = 10,power = 1;
    private boolean isAttacking = false;
    private double speed;
    private Character targetPlayer;
    private double bloodLength = 206;
    private int direction = 1;
    private int modX;
    private Fire fire1, fire2;
    private double lastDamageTime = 0, currentTime = 0;
    private Timeline timeLine1, timeLine2;
    private BoundingBox mainBoundingBox;
    private boolean bossExists = true;

    public Boss(double x, double y, Character targetPlayer) {
        this.x = x;
        this.y = y;
        this.targetPlayer = targetPlayer;
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        getChildren().add(imageView);

        bossAnimation = new SpriteAnimation(imageView, Duration.millis(2000), 5, 5, 0, 0, 247, 250);
        bossAnimation.setCycleCount(Animation.INDEFINITE);
        imageView.setImage(attackImage);
        bossAnimation.play();

        mainBoundingBox = new BoundingBox(x + 20, y, 207, 250);

        fire1 = new Fire(487, 243);
        fire1.setTargetPlayer(targetPlayer);
        fire1.setUsed(false);
        fire2 = new Fire(730, 243);
        fire2.setTargetPlayer(targetPlayer);
        fire2.setUsed(false);

        getChildren().addAll(fire1,fire2);
    }

    public void shoot(int type, double site) {
        timeLine2 = new Timeline(new KeyFrame(Duration.seconds(0.07), e -> {
            if (bossExists) {
                if (type == 1) {
                    ImageView note = new ImageView(note1Image);
                    note.setTranslateX(x + modX);
                    note.setTranslateY(y + site + 100);
                    BoundingBox noteBox = new BoundingBox(x + modX, y + site + 100, 30, 37);

                    getChildren().addAll(note);
                    bullet.add(note);
                    bulletBox.add(noteBox);
                    bulletType.add(1);
                    bulletVersion.add(1);
                    bulletDirection.add(direction);
                }
                else if (type == 2) {
                    ImageView note = new ImageView(note2Image);
                    note.setTranslateX(x + modX);
                    note.setTranslateY(y + site+100);
                    BoundingBox noteBox = new BoundingBox(x + modX, y + site + 100, 30, 48);
                    
                    getChildren().addAll(note);
                    bullet.add(note);
                    bulletBox.add(noteBox);
                    bulletType.add(21);
                    bulletVersion.add(1);
                    bulletDirection.add(direction);
                }
                else if (type == 3) {
                    ImageView note = new ImageView(note3Image);
                    note.setTranslateX(x + modX);
                    note.setTranslateY(y + site + 100);
                    BoundingBox noteBox = new BoundingBox(x + modX, y + site + 100, 30, 39);
                    
                    getChildren().addAll(note);
                    bullet.add(note);
                    bulletBox.add(noteBox);
                    bulletType.add(31);
                    bulletVersion.add(1);
                    bulletDirection.add(direction);
                }
                else if (type == 4) {
                    ImageView note = new ImageView(redImage);
                    note.setTranslateX(x + modX);
                    note.setTranslateY(y + site + 100);
                    BoundingBox noteBox = new BoundingBox(x + modX, y + site + 100, 33, 33);
                    
                    getChildren().addAll(note);
                    bullet.add(note);
                    bulletBox.add(noteBox);
                    bulletType.add(41);
                    bulletVersion.add(1);
                    bulletDirection.add(direction);
                }
            }
        }));
        timeLine2.setCycleCount(1);
        timeLine2.play();
    }

    public void move(){
        for (int i = 0; i < bullet.size(); i++){
            if (bulletType.get(i) == 0) continue;
            else if (bulletType.get(i) == 1){
                speed = 2;
                if (bullet.get(i).getTranslateY() <= up){
                    bulletVersion.set(i, 1);
                }
                else if (bullet.get(i).getTranslateY() >= down){
                    bulletVersion.set(i, -1);
                }
                bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() - speed*bulletDirection.get(i));
                bullet.get(i).setTranslateY(bullet.get(i).getTranslateY() + speed*bulletVersion.get(i));
                bulletBox.set(i,new BoundingBox(bullet.get(i).getTranslateX() - speed * bulletDirection.get(i), bullet.get(i).getTranslateY() + speed * bulletVersion.get(i), 30, 37));
                if (targetPlayer.getCharacterBoundingBox().intersects(bulletBox.get(i)) && bossExists){
                    targetPlayer.takeDamage(power);
                    boomAnimation = new SpriteAnimation(bullet.get(i), Duration.millis(500), 9, 9, 0, 0, 40, 40);
                    bullet.get(i).setImage(boomImage);
                    boomAnimation.setCycleCount(1);
                    boomAnimation.play();
                    bulletType.set(i, 0);
                    int[] tmp = {i};
                    boomAnimation.setOnFinished(e -> { 
                        getChildren().remove(bullet.get(tmp[0]));
                    });
                }
                if (bulletDirection.get(i)==1) {
                    if (bullet.get(i).getTranslateX()<=left) {
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(boomImage);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bulletType.set(i,0);
                        int[] tmp = {i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                }
                else {
                    if (bullet.get(i).getTranslateX()>=right) {
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(boomImage);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bulletType.set(i,0);
                        int[] tmp = {i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                }
            }
            else if (bulletType.get(i)==21 || bulletType.get(i)==22 || bulletType.get(i)==23) {
                speed = 3;
                if (bulletType.get(i)==21) {
                    bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() - speed * bulletDirection.get(i));
                    bulletBox.set(i, new BoundingBox(bullet.get(i).getTranslateX() - speed * bulletDirection.get(i), bullet.get(i).getTranslateY(), 30, 48));
                    if (targetPlayer.getCharacterBoundingBox().intersects(bulletBox.get(i)) && bossExists) {
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i), Duration.millis(500), 9 , 9, 0, 0, 40, 40);
                        bullet.get(i).setImage(boomImage);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bulletType.set(i,0);
                        int[] tmp = {i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if (bulletDirection.get(i) == 1) {
                        if (bullet.get(i).getTranslateX() <= left){
                            bulletType.set(i, 22);
                        }
                    }
                    else {
                        if (bullet.get(i).getTranslateX() >= right) {
                            bulletType.set(i, 22);
                        }
                    }
                }
                else if (bulletType.get(i)==22) {
                    bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() + speed * bulletDirection.get(i));
                    bulletBox.set(i,new BoundingBox(bullet.get(i).getTranslateX() + speed * bulletDirection.get(i), bullet.get(i).getTranslateY(), 30, 48));
                    if (targetPlayer.getCharacterBoundingBox().intersects(bulletBox.get(i)) && bossExists) {
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i), Duration.millis(500), 9, 9, 0, 0, 40, 40);
                        bullet.get(i).setImage(boomImage);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bulletType.set(i, 0);
                        int[] tmp = {i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if(bulletDirection.get(i) == 1){
                        if (bullet.get(i).getTranslateX() >= x - 30) {
                            bulletType.set(i, 23);
                        }
                    }
                    else {
                        if (bullet.get(i).getTranslateX() <= x + 217 + 30 ){
                            bulletType.set(i, 23);
                        }
                    }
                }
                else if (bulletType.get(i)==23) {
                    bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() - speed * bulletDirection.get(i));
                    bulletBox.set(i, new BoundingBox(bullet.get(i).getTranslateX() - speed * bulletDirection.get(i), bullet.get(i).getTranslateY(), 30, 48));
                    if (targetPlayer.getCharacterBoundingBox().intersects(bulletBox.get(i)) && bossExists) {
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i), Duration.millis(500), 9, 9, 0, 0, 40, 40);
                        bullet.get(i).setImage(boomImage);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bulletType.set(i, 0);
                        int[] tmp = {i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if (bulletDirection.get(i)==1) {
                        if (bullet.get(i).getTranslateX()<=left) {
                            boomAnimation = new SpriteAnimation(bullet.get(i), Duration.millis(500), 9, 9, 0, 0, 40, 40);
                            bullet.get(i).setImage(boomImage);
                            boomAnimation.setCycleCount(1);
                            boomAnimation.play();
                            bulletType.set(i,0);
                            int[] tmp = {i};
                            boomAnimation.setOnFinished(e -> { 
                                getChildren().remove(bullet.get(tmp[0]));
                            });
                        }
                    }
                    else {
                        if (bullet.get(i).getTranslateX() >= right) {
                            boomAnimation = new SpriteAnimation(bullet.get(i), Duration.millis(500), 9, 9, 0, 0, 40, 40);
                            bullet.get(i).setImage(boomImage);
                            boomAnimation.setCycleCount(1);
                            boomAnimation.play();
                            bulletType.set(i,0);
                            int[] tmp = {i};
                            boomAnimation.setOnFinished(e -> { 
                                getChildren().remove(bullet.get(tmp[0]));
                            });
                        }
                    }
                }
                
            }
            else if (bulletType.get(i) == 31 || bulletType.get(i) == 32) {
                if (bulletType.get(i) == 31){
                    speed=2;
                    if(bullet.get(i).getTranslateY()<=up) {
                        bulletVersion.set(i,1);
                    }
                    else if(bullet.get(i).getTranslateY()>=down) {
                        bulletVersion.set(i,-1);
                    }
                    bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() - speed * bulletDirection.get(i));
                    bullet.get(i).setTranslateY(bullet.get(i).getTranslateY() + speed * bulletVersion.get(i));
                    bulletBox.set(i,new BoundingBox(bullet.get(i).getTranslateX() - speed*bulletDirection.get(i), bullet.get(i).getTranslateY() + speed*bulletVersion.get(i) , 30, 39));
                    if (targetPlayer.getCharacterBoundingBox().intersects(bulletBox.get(i)) && bossExists) {
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(boomImage);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bulletType.set(i,0);
                        int[] tmp = {i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if (bulletDirection.get(i) == 1) {
                        if (bullet.get(i).getTranslateX() <= left) {
                            bulletType.set(i, 32);
                            bulletVersion.set(i, 1);
                        }
                    }
                    else {
                        if (bullet.get(i).getTranslateX() >= right) {
                            bulletType.set(i, 32);
                            bulletVersion.set(i, 1);
                        }
                    }
                }
                else if(bulletType.get(i)==32){
                    speed=5;
                    bullet.get(i).setTranslateY(bullet.get(i).getTranslateY() + speed*bulletVersion.get(i));
                    bulletBox.set(i,new BoundingBox(bullet.get(i).getTranslateX(), bullet.get(i).getTranslateY() + speed*bulletVersion.get(i) , 30, 39));
                    if(targetPlayer.getCharacterBoundingBox().intersects(bulletBox.get(i)) && bossExists){
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(boomImage);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bulletType.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if(bullet.get(i).getTranslateY()>=390){
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(boomImage);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bulletType.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                }
            }
            else if(bulletType.get(i)==41 || bulletType.get(i)==42){
                if(bulletType.get(i)==41){
                    speed=2;
                    if(bullet.get(i).getTranslateY()<=up){
                        bulletVersion.set(i,1);
                    }
                    else if(bullet.get(i).getTranslateY()>=down){
                        bulletVersion.set(i,-1);
                    }
                    bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() - speed*bulletDirection.get(i));
                    bullet.get(i).setTranslateY(bullet.get(i).getTranslateY() + speed*bulletVersion.get(i));
                    bulletBox.set(i,new BoundingBox(bullet.get(i).getTranslateX() - speed*bulletDirection.get(i), bullet.get(i).getTranslateY() + speed*bulletVersion.get(i) , 33, 33));
                    if(targetPlayer.getCharacterBoundingBox().intersects(bulletBox.get(i)) && bossExists){
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(boomImage);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bulletType.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if(bulletDirection.get(i)==1){
                        if(bullet.get(i).getTranslateX()<=left){
                            bulletType.set(i,42);
                            bulletVersion.set(i,1);
                        }
                    }
                    else{
                        if(bullet.get(i).getTranslateX()>=right){
                            bulletType.set(i,42);
                            bulletVersion.set(i,1);
                        }
                    }
                }
                else if(bulletType.get(i)==42){
                    speed=5;
                    bullet.get(i).setTranslateY(bullet.get(i).getTranslateY() + speed*bulletVersion.get(i));
                    bulletBox.set(i,new BoundingBox(bullet.get(i).getTranslateX(), bullet.get(i).getTranslateY() + speed*bulletVersion.get(i) , 33, 33));
                    if(targetPlayer.getCharacterBoundingBox().intersects(bulletBox.get(i)) && bossExists){
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(boomImage);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bulletType.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if(bullet.get(i).getTranslateY()>=390){
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(boomImage);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bulletType.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                }
            }
        }
    }

    public void takeDamage(Double damage) {
        isAttacking=true;
        health -= damage;
        bloodLength = (health/full)*206.0;
        if (health <= 0) {
            defeat();
        }
        else{
            timeLine1 = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
                if(direction==1){
                    fire1.setUsed(true);
                    fire2.setUsed(false);
                }
                else{
                    fire1.setUsed(false);
                    fire2.setUsed(true);
                }
            }));
            timeLine1.setCycleCount(1);
            timeLine1.play();
            timeLine2 = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
                fire1.setUsed(false);
                fire2.setUsed(false);
            }));
            timeLine2.setCycleCount(1);
            timeLine2.play();
        }
    }

    public void defeat(){
        getChildren().clear();
        bossExists=false;
    }

    public void update(int count) {
        if (!bossExists){
            return;
        }
        fire1.update();
        fire2.update();
        if(count%180==0){
            if(bossExists){
                double random = Math.random();
                if(count==180*9){
                    shoot(4, (random*1000%110));
                }
                else{
                    shoot((int)(random*1000%3)+1, (random*1000%110));
                }
            }
            if(count%1800==0){
                direction*=-1;
                imageView.setScaleX(direction);
                if(direction==-1){
                    modX=217;
                }
                else{
                    modX=0;
                }
            }
        }
        move();
        if(targetPlayer.attackState()){
            if(targetPlayer.getLightBoundingBox().intersects(mainBoundingBox) && targetPlayer.getIsUsingLight()){
                takeDamage(targetPlayer.getPower()*3);
            }
            else if(targetPlayer.getAttackBoundingBox().intersects(mainBoundingBox)){
                takeDamage(targetPlayer.getPower());
            }
        }
        currentTime = System.currentTimeMillis();
        if (targetPlayer.getCharacterBoundingBox().intersects(mainBoundingBox)) {
            if (currentTime - lastDamageTime > 1000) {
                lastDamageTime = currentTime;
                targetPlayer.takeDamage(power);
            }
        }
    }

    public boolean getBossExists(){
        return bossExists;
    }

    public boolean getIsAttacking(){
        return isAttacking;
    }

    public void setisAttacking(boolean isAttacking){
        this.isAttacking = isAttacking;
    }

    public double getHealth(){
        return health;
    }

    public double getBloodLength(){
        return bloodLength;
    }
}
