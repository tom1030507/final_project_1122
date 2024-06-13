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
    private Image img_idle = new Image(getClass().getResourceAsStream("level3/boss1.png"));
    private Image img_attack = new Image(getClass().getResourceAsStream("level3/boss.png"));
    private Image img_note1 = new Image(getClass().getResourceAsStream("level3/note1.png"));
    private Image img_note2 = new Image(getClass().getResourceAsStream("level3/note2.png"));
    private Image img_note3 = new Image(getClass().getResourceAsStream("level3/note3.png"));
    private Image img_red = new Image(getClass().getResourceAsStream("level3/red.png"));
    private Image img_boom = new Image(getClass().getResourceAsStream("level3/Boom.png"));
    private ImageView imageview = new ImageView(img_idle);
    private ArrayList<ImageView> bullet=new ArrayList<ImageView>();
    private ArrayList<BoundingBox> bullet_Box=new ArrayList<BoundingBox>();
    private ArrayList<Integer> bullet_type=new ArrayList<Integer>();
    private ArrayList<Integer> bullet_ver=new ArrayList<Integer>();
    private ArrayList<Integer> bullet_dir=new ArrayList<Integer>();
    private double x,y;
    private double left=70,right=1190,up=160,down=270;
    private SpriteAnimation bossAnimation,boomAnimation;
    private double full=10,health=10,power=1;
    private boolean isattacking=false;
    private double speed;
    private Character targetPlayer;
    private double blong=206;
    private int dirction=1;
    private int modx;
    private Fire fire1,fire2;
    private double lastDamageTime = 0, currentTime = 0;
    private Timeline timeline1, timeline2;
    private BoundingBox mainBoundingBox;
    private boolean exist=true;

    public Boss(double x, double y, Character targetPlayer) {
        this.x=x;
        this.y=y;
        this.targetPlayer=targetPlayer;
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);

        bossAnimation = new SpriteAnimation(imageview,Duration.millis(2000),5,5,0,0,247,250);
        bossAnimation.setCycleCount(Animation.INDEFINITE);
        imageview.setImage(img_attack);
        bossAnimation.play();

        mainBoundingBox = new BoundingBox(x + 20, y, 207, 250);

        fire1=new Fire(487, 243);
        fire1.setTargetPlayer(targetPlayer);
        fire1.setused(false);
        fire2=new Fire(730, 243);
        fire2.setTargetPlayer(targetPlayer);
        fire2.setused(false);

        getChildren().addAll(fire1,fire2);
    }

    public void shoot(int type, double site) {
        timeline2 = new Timeline(new KeyFrame(Duration.seconds(0.07), e -> {
            if(exist){
                if(type==1){
                    ImageView note = new ImageView(img_note1);
                    note.setTranslateX(x+modx);
                    note.setTranslateY(y+site+100);
                    BoundingBox note_box = new BoundingBox(x+modx, y + site + 100, 30, 37);

                    getChildren().addAll(note);
                    bullet.add(note);
                    bullet_Box.add(note_box);
                    bullet_type.add(1);
                    bullet_ver.add(1);
                    bullet_dir.add(dirction);
                }
                else if(type==2){
                    ImageView note = new ImageView(img_note2);
                    note.setTranslateX(x+modx);
                    note.setTranslateY(y+site+100);
                    BoundingBox note_box = new BoundingBox(x+modx, y + site + 100, 30, 48);
                    
                    getChildren().addAll(note);
                    bullet.add(note);
                    bullet_Box.add(note_box);
                    bullet_type.add(21);
                    bullet_ver.add(1);
                    bullet_dir.add(dirction);
                }
                else if(type==3){
                    ImageView note = new ImageView(img_note3);
                    note.setTranslateX(x+modx);
                    note.setTranslateY(y+site+100);
                    BoundingBox note_box = new BoundingBox(x+modx, y + site + 100, 30, 39);
                    
                    getChildren().addAll(note);
                    bullet.add(note);
                    bullet_Box.add(note_box);
                    bullet_type.add(31);
                    bullet_ver.add(1);
                    bullet_dir.add(dirction);
                }
                else if(type==4){
                    ImageView note = new ImageView(img_red);
                    note.setTranslateX(x+modx);
                    note.setTranslateY(y+site+100);
                    BoundingBox note_box = new BoundingBox(x+modx, y + site + 100, 33, 33);
                    
                    getChildren().addAll(note);
                    bullet.add(note);
                    bullet_Box.add(note_box);
                    bullet_type.add(41);
                    bullet_ver.add(1);
                    bullet_dir.add(dirction);
                }
            }
        }));
        timeline2.setCycleCount(1);
        timeline2.play();
    }

    public void move(){
        for(int i=0;i<bullet.size();i++){
            if(bullet_type.get(i)==0) continue;
            else if(bullet_type.get(i)==1){
                speed=2;
                if(bullet.get(i).getTranslateY()<=up){
                    bullet_ver.set(i,1);
                }
                else if(bullet.get(i).getTranslateY()>=down){
                    bullet_ver.set(i,-1);
                }
                bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() - speed*bullet_dir.get(i));
                bullet.get(i).setTranslateY(bullet.get(i).getTranslateY() + speed*bullet_ver.get(i));
                bullet_Box.set(i,new BoundingBox(bullet.get(i).getTranslateX() - speed*bullet_dir.get(i), bullet.get(i).getTranslateY() + speed*bullet_ver.get(i) , 30, 37));
                if(targetPlayer.getcharacterBoundingBox().intersects(bullet_Box.get(i)) && exist){
                    targetPlayer.takeDamage(power);
                    boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                    bullet.get(i).setImage(img_boom);
                    boomAnimation.setCycleCount(1);
                    boomAnimation.play();
                    bullet_type.set(i,0);
                    int[] tmp={i};
                    boomAnimation.setOnFinished(e -> { 
                        getChildren().remove(bullet.get(tmp[0]));
                    });
                }
                if(bullet_dir.get(i)==1){
                    if(bullet.get(i).getTranslateX()<=left){
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(img_boom);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bullet_type.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                }
                else{
                    if(bullet.get(i).getTranslateX()>=right){
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(img_boom);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bullet_type.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                }
            }
            else if(bullet_type.get(i)==21 || bullet_type.get(i)==22 || bullet_type.get(i)==23){
                speed=3;
                if(bullet_type.get(i)==21){
                    bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() - speed*bullet_dir.get(i));
                    bullet_Box.set(i,new BoundingBox(bullet.get(i).getTranslateX() - speed*bullet_dir.get(i), bullet.get(i).getTranslateY() , 30, 48));
                    if(targetPlayer.getcharacterBoundingBox().intersects(bullet_Box.get(i)) && exist){
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(img_boom);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bullet_type.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if(bullet_dir.get(i)==1){
                        if(bullet.get(i).getTranslateX()<=left){
                            bullet_type.set(i,22);
                        }
                    }
                    else{
                        if(bullet.get(i).getTranslateX()>=right){
                            bullet_type.set(i,22);
                        }
                    }
                }
                else if(bullet_type.get(i)==22){
                    bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() + speed*bullet_dir.get(i));
                    bullet_Box.set(i,new BoundingBox(bullet.get(i).getTranslateX() + speed*bullet_dir.get(i), bullet.get(i).getTranslateY() , 30, 48));
                    if(targetPlayer.getcharacterBoundingBox().intersects(bullet_Box.get(i)) && exist){
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(img_boom);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bullet_type.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if(bullet_dir.get(i)==1){
                        if(bullet.get(i).getTranslateX()>=x-30){
                            bullet_type.set(i,23);
                        }
                    }
                    else{
                        if(bullet.get(i).getTranslateX()<=x+217+30){
                            bullet_type.set(i,23);
                        }
                    }
                }
                else if(bullet_type.get(i)==23){
                    bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() - speed*bullet_dir.get(i));
                    bullet_Box.set(i,new BoundingBox(bullet.get(i).getTranslateX() - speed*bullet_dir.get(i), bullet.get(i).getTranslateY() , 30, 48));
                    if(targetPlayer.getcharacterBoundingBox().intersects(bullet_Box.get(i)) && exist){
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(img_boom);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bullet_type.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if(bullet_dir.get(i)==1){
                        if(bullet.get(i).getTranslateX()<=left){
                            boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                            bullet.get(i).setImage(img_boom);
                            boomAnimation.setCycleCount(1);
                            boomAnimation.play();
                            bullet_type.set(i,0);
                            int[] tmp={i};
                            boomAnimation.setOnFinished(e -> { 
                                getChildren().remove(bullet.get(tmp[0]));
                            });
                        }
                    }
                    else{
                        if(bullet.get(i).getTranslateX()>=right){
                            boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                            bullet.get(i).setImage(img_boom);
                            boomAnimation.setCycleCount(1);
                            boomAnimation.play();
                            bullet_type.set(i,0);
                            int[] tmp={i};
                            boomAnimation.setOnFinished(e -> { 
                                getChildren().remove(bullet.get(tmp[0]));
                            });
                        }
                    }
                }
                
            }
            else if(bullet_type.get(i)==31 || bullet_type.get(i)==32){
                if(bullet_type.get(i)==31){
                    speed=2;
                    if(bullet.get(i).getTranslateY()<=up){
                        bullet_ver.set(i,1);
                    }
                    else if(bullet.get(i).getTranslateY()>=down){
                        bullet_ver.set(i,-1);
                    }
                    bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() - speed*bullet_dir.get(i));
                    bullet.get(i).setTranslateY(bullet.get(i).getTranslateY() + speed*bullet_ver.get(i));
                    bullet_Box.set(i,new BoundingBox(bullet.get(i).getTranslateX() - speed*bullet_dir.get(i), bullet.get(i).getTranslateY() + speed*bullet_ver.get(i) , 30, 39));
                    if(targetPlayer.getcharacterBoundingBox().intersects(bullet_Box.get(i)) && exist){
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(img_boom);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bullet_type.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if(bullet_dir.get(i)==1){
                        if(bullet.get(i).getTranslateX()<=left){
                            bullet_type.set(i,32);
                            bullet_ver.set(i,1);
                        }
                    }
                    else{
                        if(bullet.get(i).getTranslateX()>=right){
                            bullet_type.set(i,32);
                            bullet_ver.set(i,1);
                        }
                    }
                }
                else if(bullet_type.get(i)==32){
                    speed=5;
                    bullet.get(i).setTranslateY(bullet.get(i).getTranslateY() + speed*bullet_ver.get(i));
                    bullet_Box.set(i,new BoundingBox(bullet.get(i).getTranslateX(), bullet.get(i).getTranslateY() + speed*bullet_ver.get(i) , 30, 39));
                    if(targetPlayer.getcharacterBoundingBox().intersects(bullet_Box.get(i)) && exist){
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(img_boom);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bullet_type.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if(bullet.get(i).getTranslateY()>=390){
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(img_boom);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bullet_type.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                }
            }
            else if(bullet_type.get(i)==41 || bullet_type.get(i)==42){
                if(bullet_type.get(i)==41){
                    speed=2;
                    if(bullet.get(i).getTranslateY()<=up){
                        bullet_ver.set(i,1);
                    }
                    else if(bullet.get(i).getTranslateY()>=down){
                        bullet_ver.set(i,-1);
                    }
                    bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() - speed*bullet_dir.get(i));
                    bullet.get(i).setTranslateY(bullet.get(i).getTranslateY() + speed*bullet_ver.get(i));
                    bullet_Box.set(i,new BoundingBox(bullet.get(i).getTranslateX() - speed*bullet_dir.get(i), bullet.get(i).getTranslateY() + speed*bullet_ver.get(i) , 33, 33));
                    if(targetPlayer.getcharacterBoundingBox().intersects(bullet_Box.get(i)) && exist){
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(img_boom);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bullet_type.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if(bullet_dir.get(i)==1){
                        if(bullet.get(i).getTranslateX()<=left){
                            bullet_type.set(i,42);
                            bullet_ver.set(i,1);
                        }
                    }
                    else{
                        if(bullet.get(i).getTranslateX()>=right){
                            bullet_type.set(i,42);
                            bullet_ver.set(i,1);
                        }
                    }
                }
                else if(bullet_type.get(i)==42){
                    speed=5;
                    bullet.get(i).setTranslateY(bullet.get(i).getTranslateY() + speed*bullet_ver.get(i));
                    bullet_Box.set(i,new BoundingBox(bullet.get(i).getTranslateX(), bullet.get(i).getTranslateY() + speed*bullet_ver.get(i) , 33, 33));
                    if(targetPlayer.getcharacterBoundingBox().intersects(bullet_Box.get(i)) && exist){
                        targetPlayer.takeDamage(power);
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(img_boom);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bullet_type.set(i,0);
                        int[] tmp={i};
                        boomAnimation.setOnFinished(e -> { 
                            getChildren().remove(bullet.get(tmp[0]));
                        });
                    }
                    if(bullet.get(i).getTranslateY()>=390){
                        boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),9,9,0,0,40,40);
                        bullet.get(i).setImage(img_boom);
                        boomAnimation.setCycleCount(1);
                        boomAnimation.play();
                        bullet_type.set(i,0);
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
        isattacking=true;
        health -= damage;
        blong = (health/full)*206.0;
        if (health <= 0) {
            defeat();
        }
        else{
            timeline1 = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
                if(dirction==1){
                    fire1.setused(true);
                    fire2.setused(false);
                }
                else{
                    fire1.setused(false);
                    fire2.setused(true);
                }
            }));
            timeline1.setCycleCount(1);
            timeline1.play();
            timeline2 = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
                fire1.setused(false);
                fire2.setused(false);
            }));
            timeline2.setCycleCount(1);
            timeline2.play();
        }
    }

    public void defeat(){
        getChildren().clear();
        exist=false;
    }

    public void update(int count) {
        if (!exist){
            return;
        }
        fire1.update();
        fire2.update();
        if(count%180==0){
            if(exist){
                double random = Math.random();
                if(count==180*9){
                    shoot(4, (random*1000%110));
                }
                else{
                    shoot((int)(random*1000%3)+1, (random*1000%110));
                }
            }
            if(count%1800==0){
                dirction*=-1;
                imageview.setScaleX(dirction);
                if(dirction==-1){
                    modx=217;
                }
                else{
                    modx=0;
                }
            }
        }
        move();
        if(targetPlayer.attackstate()){
            if(targetPlayer.getlightBoundingBox().intersects(mainBoundingBox) && targetPlayer.getIsUsingLight()){
                takeDamage(targetPlayer.getPower()*3);
            }
            else if(targetPlayer.getattackBoundingBox().intersects(mainBoundingBox)){
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

    public boolean getexist(){
        return exist;
    }

    public boolean getisattacking(){
        return isattacking;
    }

    public void setisattacking(boolean isattacking){
        this.isattacking=isattacking;
    }

    public double gethealth(){
        return health;
    }

    public double getblong(){
        return blong;
    }
}
