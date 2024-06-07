package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Platform extends Pane {
    private Image image;
    private ImageView imageView;
    public Platform(int type, double x, double y) {
        if (type == 1) {
            image = new Image(getClass().getResourceAsStream("ShortPlatform.png"));
        } else if (type == 2) {
            image = new Image(getClass().getResourceAsStream("LongPlatform.png"));
        }
        imageView = new ImageView(image);
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        getChildren().add(imageView);
    }
}
