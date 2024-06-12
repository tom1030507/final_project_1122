package game;

import javafx.scene.input.KeyCode;

public class CharacterController {

    private Character character;
    private Boundary boundary;
    private boolean moveRight = false;
    private boolean moveLeft = false;
    private boolean attacking = false;
    boolean stop=false;

    public CharacterController(Character character, int level) {
        this.character = character;
        this.boundary = new Boundary(level);
    }

    public void handleKeyPressed(KeyCode code) {
        System.out.println(code);
        if (character.health<=0) return;
        if (code == KeyCode.ESCAPE){
            stop = !stop;
        } else if(code == KeyCode.ENTER){
            stop = false;
        } else if (code == KeyCode.RIGHT) {
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
        } else if (code == KeyCode.F){
            character.press=true;
            character.action();
        }
    }

    public void handleKeyReleased(KeyCode code) {
        if(character.health<=0) return;
        if (code == KeyCode.RIGHT) {
            moveRight = false;
            character.stop();
        } else if (code == KeyCode.LEFT) {
            moveLeft = false;
            character.stop();
        } else if (code == KeyCode.Z) {
            attacking = false;
        }  else if (code == KeyCode.F){
            character.press=false;
        }
    }

    private boolean lastMoveRight = true;
    private boolean lastMoveLeft = false;

    public void update() {
        if (stop) {
            return;
        }
        if (moveRight && (!boundary.isWithinBounds(character.getBoundingBox()) || lastMoveLeft)) {
            lastMoveRight = true;
            lastMoveLeft = false;
            character.move_right();
        } else if (moveLeft && (!boundary.isWithinBounds(character.getBoundingBox()) || lastMoveRight)) {
            lastMoveRight = false;
            lastMoveLeft = true;
            character.move_left();
        }
    }

    public void setStopStatus(boolean status){
        stop = status;
    }
}