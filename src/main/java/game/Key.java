package game;

import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Key extends Pane{
    Image img_key = new Image(getClass().getResourceAsStream("key.png"));
    ImageView imageview = new ImageView(img_key);
    Character targetPlayer;
    boolean used=true;

    Rectangle realBoundary;
    BoundingBox boundingBox;

    public Key(double x,double y){
        imageview.setTranslateX(x);
        imageview.setTranslateY(y);
        getChildren().add(imageview);

        realBoundary = new Rectangle(x, y, 30, 30);
        realBoundary.setStroke(Color.BLUE); // 邊界線顏色
        realBoundary.setFill(Color.TRANSPARENT); // 內部填充顏色

        boundingBox = new BoundingBox(x, y, 30, 30);
        getChildren().add(realBoundary);
    }

    public void setTargetPlayer(Character character) {
        this.targetPlayer = character;
    }

    public void update(){
        if(used){
            if(targetPlayer.boundingBox.intersects(boundingBox)){
                targetPlayer.keyexist=true;
                used=false;
                getChildren().clear();
            }
        }
    }
}
