package game;

import javafx.scene.input.KeyCode;

public class CharacterController {

    private Character character;
    private Boundary boundary = new Boundary();
    private boolean moveRight = false;
    private boolean moveLeft = false;

    public CharacterController(Character character) {
        this.character = character;
    }

    public void handleKeyPressed(KeyCode code) {
        if (code == KeyCode.RIGHT) {
            moveRight = true;
        } else if (code == KeyCode.LEFT) {
            moveLeft = true;
        } else if (code == KeyCode.SPACE) {
            character.move_jump();
        } else if (code == KeyCode.Z){
            character.attack();
        }
    }

    public void handleKeyReleased(KeyCode code) {
        if (code == KeyCode.RIGHT) {
            moveRight = false;
            character.stop();
        } else if (code == KeyCode.LEFT) {
            moveLeft = false;
            character.stop();
        }
    }

    private boolean lastMoveRight = true;
    private boolean lastMoveLeft = false;

    public void update() {
        if (moveRight && (!boundary.isWithinBounds(character.getBoundingBox()) || lastMoveLeft)) {
            // System.out.println("move right");
            lastMoveRight = true;
            lastMoveLeft = false;
            character.move_right();
        } else if (moveLeft && (!boundary.isWithinBounds(character.getBoundingBox()) || lastMoveRight)) {
            // System.out.println("move left");
            lastMoveRight = false;
            lastMoveLeft = true;
            character.move_left();
        }

        character.applyGravity();
    }
}