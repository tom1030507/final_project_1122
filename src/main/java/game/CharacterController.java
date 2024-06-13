package game;

import javafx.scene.input.KeyCode;

public class CharacterController {

    private Character character;
    private Boundary boundary;
    private boolean isMovingRight = false;
    private boolean isMovingLeft = false;
    private boolean isAttacking = false;
    public boolean isStopped = false;

    private boolean lastDirectionWasRight = true;
    private boolean lastDirectionWasLeft = false;

    public CharacterController(Character character, int level) {
        this.character = character;
        this.boundary = new Boundary(level);
    }

    public void handleKeyPressed(KeyCode code) {
        if (character.getHealth() <= 0) return;
        switch (code) {
            case ESCAPE:
                isStopped = !isStopped;
                break;
            case RIGHT:
                isMovingRight = true;
                break;
            case LEFT:
                isMovingLeft = true;
                break;
            case SPACE:
                character.jump();
                break;
            case Z:
                if (!isAttacking) {
                    character.attack();
                }
                isAttacking = true;
                break;
            case F:
                character.setIsPressed(true);
                character.action();
                break;
            default:
                break;
        }
    }

    public void handleKeyReleased(KeyCode code) {
        if(character.getHealth() <= 0) return;
        switch (code) {
            case RIGHT:
                isMovingRight = false;
                character.stop();
                break;
            case LEFT:
                isMovingLeft = false;
                character.stop();
                break;
            case Z:
                isAttacking = false;
                break;
            case F:
                character.setIsPressed(false);
                break;
            default:
                break;
        }
    }

    public void update() {
        if (isStopped) {
            return;
        }
        if (isMovingRight && (!boundary.isWithinBounds(character.getCharacterBoundingBox()) || lastDirectionWasLeft)) {
            lastDirectionWasRight = true;
            lastDirectionWasLeft = false;
            character.moveRight();
        } else if (isMovingLeft && (!boundary.isWithinBounds(character.getCharacterBoundingBox()) || lastDirectionWasRight)) {
            lastDirectionWasRight = false;
            lastDirectionWasLeft = true;
            character.moveLeft();
        }
    }

    public void setStopStatus(boolean status){
        isStopped = status;
    }

    public boolean getStopStatus(){
        return isStopped;
    }
}