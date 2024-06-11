package game;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.PerspectiveCamera;
import javafx.util.Duration;
import javafx.stage.Stage;

public class Level3 implements Background {
    private int backgroundWidth = 1300, backgroundHeight = 700;
    private double scope = 0.5; // 摄像机缩放比例
    private ImageView background;
    private Pane pane = new Pane();
    private Scene scene;
    Timeline timeline;

    public Scene createScene(Stage primaryStage) {
        background = new ImageView(new Image(getClass().getResourceAsStream("level3/level3_background.jpg")));
        background.setFitWidth(backgroundWidth);
        background.setFitHeight(backgroundHeight);

		Character character = new Character(600, 590, 3);
        CharacterController controller = new CharacterController(character, 3);

        Boundary boundary = new Boundary(3);
        
        Door door=new Door(604,591);
        door.setTargetPlayer(character);
        door.used=false;

        Door door2=new Door(604,253);
        door2.setTargetPlayer(character);
        door2.imageview.setOpacity(0);
        door2.realBoundary.setOpacity(0);

        Boss boss=new Boss(501, 60, character);

        Pane fire = new Pane();
        ArrayList<Fire> boss_left=new ArrayList<Fire>();
        ArrayList<Fire> boss_right=new ArrayList<Fire>();

        for(int i=0;i<10;i++){
            Fire fire1=new Fire(170+33*i, 243);
            fire1.setTargetPlayer(character);
            fire1.used=false;
            boss_left.add(fire1);
            Fire fire2=new Fire(750+33*i, 243);
            fire2.setTargetPlayer(character);
            fire2.used=true;
            boss_right.add(fire2);
            fire.getChildren().addAll(fire1,fire2);
        }

        int[] x1={133,73,106,139,1186};
        int[] x2={1083,1120,1153,1186,73};
        int[] y={285,485,485,485,385};
        ArrayList<Fire> stair1=new ArrayList<Fire>();
        ArrayList<Fire> stair2=new ArrayList<Fire>();

        for(int i=0;i<5;i++){
            Fire fire1=new Fire(x1[i], y[i]);
            fire1.setTargetPlayer(character);
            stair1.add(fire1);
            Fire fire2=new Fire(x2[i], y[i]);
            fire2.setTargetPlayer(character);
            fire2.used=false;
            stair2.add(fire2);
            fire.getChildren().addAll(fire1,fire2);
        }

        character.setOnKeyPressed(e -> {
            controller.handleKeyPressed(e.getCode());
        });

        character.setOnKeyReleased(e -> {
            controller.handleKeyReleased(e.getCode());
        });

        woodPlatform shortPlatform1 = new woodPlatform(1, 75, 450);
        woodPlatform shortPlatform2 = new woodPlatform(1, 132, 350);
        woodPlatform shortPlatform3 = new woodPlatform(1, 1187, 450);
        woodPlatform shortPlatform4 = new woodPlatform(1, 1085, 350);
        woodPlatform longPlatform1 = new woodPlatform(2, 75, 550);
        woodPlatform longPlatform2 = new woodPlatform(2, 1122, 550);

        Pane platform = new Pane();
        platform.getChildren().addAll(shortPlatform1, shortPlatform2, shortPlatform3, shortPlatform4, longPlatform1, longPlatform2);

        pane.getChildren().addAll(background, door, door2, fire, character, boss, platform, boundary.getBoundary());

        scene = new Scene(pane, backgroundWidth, backgroundHeight);

        PerspectiveCamera  camera = new PerspectiveCamera();
        scene.setCamera(camera);
        camera.setScaleX(scope);
        camera.setScaleY(scope);

        int[] count={0,1,1};
        timeline = new Timeline(new KeyFrame(Duration.seconds(1.0/60), e -> {
            if(!boss.exist){
                door2.imageview.setOpacity(1);
                door2.realBoundary.setOpacity(1);
                character.keyexist=true;
            }
            count[0]++;
            controller.update();
            door.update();
            door2.update();
            boss.update(count[0]);
            if(count[0]==1800){
                count[0]=0;
                count[1]*=-1;
                if(count[1]==1){
                    for(int i=0;i<10;i++){
                        boss_left.get(i).used=false;
                        boss_right.get(i).used=true;
                    }
                }
                else{
                    for(int i=0;i<10;i++){
                        boss_left.get(i).used=true;
                        boss_right.get(i).used=false;
                    }
                }
            }

            if(count[0]%180==0){
                count[2]*=-1;
                if(count[2]==1){
                    for(int i=0;i<5;i++){
                        stair1.get(i).used=true;
                        stair2.get(i).used=false;
                    }
                }
                else{
                    for(int i=0;i<5;i++){
                        stair1.get(i).used=false;
                        stair2.get(i).used=true;
                    }
                }
            }

            if(count[0]%180==120){
                for(int i=0;i<5;i++){
                    stair1.get(i).used=false;
                    stair2.get(i).used=false;
                }
            }

            for(int i=0;i<10;i++){
                boss_left.get(i).update();
                boss_right.get(i).update();
                if(i<5){
                    stair1.get(i).update();
                    stair2.get(i).update();
                }
            }

            if(character.attackstate()){
                character.attackstateupdate();
            }

            if (controller.stop) {
                gamestop();
            }

            double newCameraX = character.boundingBox.getCenterX() - (scene.getWidth()/2*scope);
            double newCameraY = character.boundingBox.getCenterY() - (scene.getHeight()/2*scope);
            // 限制摄像机X轴范围
            newCameraX = Math.max(newCameraX, 0);
            newCameraX = Math.min(newCameraX, backgroundWidth-scene.getWidth()*scope);

            // 限制摄像机Y轴范围
            newCameraY = Math.max(newCameraY, 0);
            newCameraY = Math.min(newCameraY, backgroundHeight-scene.getHeight()*scope);

            camera.setTranslateX(newCameraX);
            camera.setTranslateY(newCameraY);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

		character.requestFocus();
        character.setFocusTraversable(true);

        return scene;
    }

    public void gamestop(){
        timeline.stop();
    }

    public void setScope() {
        return;
    }
}