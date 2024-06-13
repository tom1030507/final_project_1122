// package game;

// import javafx.geometry.Rectangle2D;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
// import javafx.scene.control.Button;


// public class Buttons extends Button {
//     public Button createSoundsButton(int soundType, Boolean isMute) {
//         Image soundsButtonImage = new Image(getClass().getResourceAsStream("sound_button.png"));
//         Buttons button = new Buttons();
//         double buttonWidth = soundsButtonImage.getWidth() / 3;
//         double buttonHeight = soundsButtonImage.getHeight() / 2;
//         final boolean[] isMuteType = {isMute};

//         ImageView imageView = new ImageView(soundsButtonImage);
//         imageView.setViewport(new Rectangle2D(0, (isMuteType[0] ? 1 : 0) * buttonHeight, buttonWidth, buttonHeight));
//         button.setGraphic(imageView);

//         button.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent;");

//         button.setOnMousePressed(e -> {
//             imageView.setViewport(new Rectangle2D(2 * buttonWidth, (isMuteType[0] ? 1 : 0) * buttonHeight, buttonWidth, buttonHeight));
//             button.setGraphic(imageView);
//         });
//         button.setOnMouseReleased(e -> {
//             isMuteType[0] = !isMuteType[0];

//             if (soundType == 0) {
//                 VolumeController.setMusicMute(isMuteType[0]);
//             } else {
//                 VolumeController.setSoundMute(isMuteType[0]);
//             }
//             imageView.setViewport(new Rectangle2D(0 , (isMuteType[0] ? 1 : 0) * buttonHeight, buttonWidth, buttonHeight));
//             button.setGraphic(imageView);
//         });
        
//         return button;
//     }

//     public Button createVolumeButton(int scope) {
//         Image volumeButtonImage = new Image(getClass().getResourceAsStream("volume_buttons.png"));
//         Buttons button = new Buttons();
//         double buttonWidth = volumeButtonImage.getWidth() / 3;
//         double buttonHeight = volumeButtonImage.getHeight();
//         double startX = 230;
//         double endX = 370;
//         ImageView imageView = new ImageView(volumeButtonImage);

//         imageView.setViewport(new Rectangle2D(0, 0, buttonWidth, buttonHeight));
//         button.setGraphic(imageView);
//         button.setLayoutX(VolumeController.totalVolume * (endX - startX) + startX);
//         button.setLayoutY(216);

//         button.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent;");

//         button.setOnMousePressed(e -> {
//             button.setUserData(e.getSceneX());
//         });

//         button.setOnMouseDragged(e -> {
//             double dragX = e.getSceneX();
//             double offsetX = (dragX - (double) button.getUserData()) * scope;
//             double newLayoutX = button.getLayoutX() + offsetX;
        
//             if (newLayoutX >= startX && newLayoutX <= endX) {
//                 button.setLayoutX(newLayoutX);
//                 button.setUserData(dragX);
//             }
//             VolumeController.setTotalVolume((newLayoutX - startX) / (endX - startX));
//         });

//         button.setOnMouseReleased(e -> {
//             imageView.setViewport(new Rectangle2D(0 , 0, buttonWidth, buttonHeight));
//             button.setGraphic(imageView);
//         });
        
//         return button;
//     }

//     public Button createUrmButton(int type) {
//         Image urmButtonsImage = new Image(getClass().getResourceAsStream("urm_buttons.png"));
//         Buttons button = new Buttons();
//         double buttonWidth = urmButtonsImage.getWidth() / 3;
//         double buttonHeight = urmButtonsImage.getHeight() / 3;
//         ImageView imageView = new ImageView(urmButtonsImage);
//         imageView.setViewport(new Rectangle2D(0, type * buttonHeight, buttonWidth, buttonHeight));
//         button.setGraphic(imageView);

//         button.setStyle("-fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent;");

//         button.setOnMousePressed(e -> {
//             imageView.setViewport(new Rectangle2D(2 * buttonWidth, type * buttonHeight, buttonWidth, buttonHeight));
//             button.setGraphic(imageView);
//         });
//         button.setOnMouseReleased(e -> {
//             imageView.setViewport(new Rectangle2D(0 , type * buttonHeight, buttonWidth, buttonHeight));
//             button.setGraphic(imageView);
//         });
        
//         return button;
//     }
// }
