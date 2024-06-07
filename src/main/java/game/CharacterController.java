package game;

import javafx.scene.input.KeyCode;

public class CharacterController {

    private Character character;
    private Boundary boundary = new Boundary();
    private boolean moveRight = false;
    private boolean moveLeft = false;
    private boolean attacking = false;

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
            if(!attacking){
                character.attack();
            }
            attacking=true;
        }
    }

    public void handleKeyReleased(KeyCode code) {
        if (code == KeyCode.RIGHT) {
            moveRight = false;
            character.stop();
        } else if (code == KeyCode.LEFT) {
            moveLeft = false;
            character.stop();
        } else if (code == KeyCode.Z) {
            attacking = false;
        }
    }

    private boolean lastMoveRight = true;
    private boolean lastMoveLeft = false;

    public void update() {
        if (moveRight && (!boundary.isWithinBounds(character.getBoundingBox()) || lastMoveLeft)) {
            lastMoveRight = true;
            lastMoveLeft = false;
            character.move_right();
        } else if (moveLeft && (!boundary.isWithinBounds(character.getBoundingBox()) || lastMoveRight)) {
            lastMoveRight = false;
            lastMoveLeft = true;
            character.move_left();
        }

        character.applyGravity();
    }
}