package game;

import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Boundary {
    private Group boundaryGroup = new Group();
    private LevelBoundaryData lbd = new LevelBoundaryData();

    public Boundary(int level) {
        double[][] lineData = lbd.getLineData(level);
        for (double[] data : lineData) {
            Line line = new Line(data[0], data[1], data[2], data[3]);
            line.setStrokeWidth(3);
            line.setStroke(Color.TRANSPARENT);
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