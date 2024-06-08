package game;

import java.util.ArrayList;

import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Cannon extends Pane{
    Image img_idle = new Image(getClass().getResourceAsStream("10-Cannon/Idle.png"));
    Image img_shoot = new Image(getClass().getResourceAsStream("10-Cannon/Shoot (44x28).png"));
    Image img_ball = new Image(getClass().getResourceAsStream("10-Cannon/Cannon Ball.png"));
    Image img_boom = new Image(getClass().getResourceAsStream("09-Bomb/Boooooom (52x56).png"));
    ImageView imageview = new ImageView(img_idle);
    ArrayList<ImageView> bullet=new ArrayList<ImageView>();
    ArrayList<Rectangle> bullet_Rec=new ArrayList<Rectangle>();
    ArrayList<BoundingBox> bullet_Box=new ArrayList<BoundingBox>();
    ArrayList<Integer> bullet_avail=new ArrayList<Integer>();
    int pointer=0;
    double x,y,endx,endy;
    SpriteAnimation shootAnimation,boomAnimation;
    int sta=0;
    double full=3,health=3,power=1;
    boolean isattcking=false;
    double speed=2;
    Character targetPlayer;
    Line blood;
    double blong=24;


    Rectangle imageBoundary,realBoundary;
    BoundingBox boundingBox;

    public Cannon(double x, double y, double endx, double endy) {
        this.x=x;
        this.y=y;
        this.endx=endx;
        this.endy=endy;
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        imageview.setScaleX(-1);
        getChildren().add(imageview);

        // 初始化动画
        shootAnimation = new SpriteAnimation(imageview,Duration.millis(500),4,4,0,0,44,28);
        shootAnimation.setCycleCount(1);

        blood=new Line(x+3,y+3,x+3+blong,y+3);
        blood.setStrokeWidth(3);
        blood.setStroke(Color.RED);

        realBoundary = new Rectangle(x + 3, y + 6, 24, 19);
        realBoundary.setStroke(Color.BLUE); // 邊界線顏色
        realBoundary.setFill(Color.TRANSPARENT); // 內部填充顏色

        boundingBox = new BoundingBox(x + 3, y + 6, 24, 19);

        imageBoundary = new Rectangle(x, y, 44, 28);
        imageBoundary.setStroke(Color.RED); // 邊界線顏色
        imageBoundary.setFill(Color.TRANSPARENT); // 內部填充顏色
        getChildren().addAll(blood,imageBoundary, realBoundary);
    }


    public void shoot() {
        imageview.setImage(img_shoot);
        shootAnimation.play();
        shootAnimation.setOnFinished(e -> { // 当攻击动画播放完毕时，切换回跑步动画
            if(exist){
                ImageView ball = new ImageView(img_ball);
                ball.setTranslateX(x+5);
                ball.setTranslateY(y-5);
                ball.setScaleX(-1);
                Rectangle ball_rec = new Rectangle(x + 13, y + 7, 13, 13);
                ball_rec.setStroke(Color.BLUE); // 邊界線顏色
                ball_rec.setFill(Color.TRANSPARENT); // 內部填充顏色
                BoundingBox ball_box = new BoundingBox(x + 13, y + 7, 13, 13);
                
                getChildren().addAll(ball,ball_rec);
                bullet.add(ball);
                bullet_Rec.add(ball_rec);
                bullet_Box.add(ball_box);
                bullet_avail.add(1);
            }
        });
    }

    public void move(){
        for(int i=pointer;i<bullet.size();i++){
            if(bullet_avail.get(i)==0) continue;
            bullet.get(i).setTranslateX(bullet.get(i).getTranslateX() + speed);
            bullet_Rec.get(i).setX(bullet.get(i).getTranslateX() + 8 + speed);
            bullet_Box.set(i,new BoundingBox(bullet.get(i).getTranslateX() + 8 + speed, bullet.get(i).getTranslateY()+ 7 , 13, 13));
            if(targetPlayer.boundingBox.intersects(bullet_Box.get(i)) && exist){
                targetPlayer.takeDamage(power);
                boomAnimation = new SpriteAnimation(bullet.get(i),Duration.millis(500),6,6,0,0,52,56);
                bullet.get(i).setImage(img_boom);
                boomAnimation.setCycleCount(1);
                boomAnimation.play();
                bullet_avail.set(i,0);
                //bulletproblem(pointer);
                getChildren().remove(bullet_Rec.get(i));
                int[] tmp={i};
                boomAnimation.setOnFinished(e -> { 
                    getChildren().remove(bullet.get(tmp[0]));
                });
                
            }
            if(bullet.get(i).getTranslateX()>=endx){
                pointer=i+1;
                getChildren().remove(bullet.get(i));
                getChildren().remove(bullet_Rec.get(i));
            }
        }
    }

    // int correct=0;
    // public void bulletproblem(int last){
    //     for(int i=correct;i<last;i++){
    //         getChildren().remove(bullet.get(i));
    //         getChildren().remove(bullet_Rec.get(i));
    //     }
    //     correct=last;
    // }
    
    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }

    public void takeDamage(Double damage) {
        System.out.println("enatt");
        health -= damage;
        blong = (health/full)*24.0;
        blood.setEndX(x+3+blong);
        if (health <= 0) {
            // 敌人被击败，执行相应操作
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
        if(targetPlayer.attackstate()){
            if(targetPlayer.attackBox.intersects(boundingBox)){
                takeDamage(targetPlayer.power);
            }
        }
    }
}
