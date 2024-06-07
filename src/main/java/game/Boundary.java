package game;

import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Boundary {
    private Group boundaryGroup = new Group();

    public Boundary() {
        double[][] lineData = { { 72.5, 0, 72.5, 700 },
                                { 0, 641, 1300, 641 },
                                { 0, 50, 1300, 50 },
                                { 0, 405, 453, 405 },
                                { 0, 487, 453, 487 },
                                { 453, 405, 453, 487 },
                                { 1220, 0, 1210, 700 },
                                { 918, 550, 918, 700 },
                                { 918, 550, 1300, 550 },
                                { 1059, 457, 1300, 457 },
                                { 1059, 457, 1059, 700 },
                                { 940, 195, 1300, 195 },
                                { 940, 273, 1300, 273 },
                                { 940, 195, 940, 273 },
                                { 348, 0, 348, 155 },
                                { 433, 0, 433, 155 },
                                { 348, 155, 433, 155 } };
        for (double[] data : lineData) {
            Line line = new Line(data[0], data[1], data[2], data[3]);
            line.setStrokeWidth(1);
            line.setStroke(Color.WHITE);
            boundaryGroup.getChildren().add(line);
        } 
    }

    public Group getBoundary() {
        return this.boundaryGroup;
    }

    public boolean isWithinBounds(Bounds otherBounds) { // 判斷是否在邊界內
        for (Node node : boundaryGroup.getChildren()) {
            Line line = (Line) node;
            BoundingBox newBounds = new BoundingBox(otherBounds.getMinX() - 4, otherBounds.getMinY(), otherBounds.getWidth() + 8, otherBounds.getHeight());
            if (line.getBoundsInParent().intersects(newBounds)) { // 如果有交集
                // System.out.println(line.getBoundsInParent());
                // System.out.println(newBounds);
                // System.out.println("collision");
                return true;
            }
        }
        return false;
    }
}