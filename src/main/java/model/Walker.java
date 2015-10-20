package model;

import controller.LevelController;
import utility.Settings;
import java.util.Random;

/**
 *  This class represents a type of monster, which just moves around and kills
 *  the player when they collide. It can be captured by a bubble shot by the player.
 */
public class Walker extends Monster {

    private LevelController levelController;
    private boolean jumping;
    private boolean ableToJump;
    private int jumpCounter;
    private double walkerMinX;
    private double walkerMaxX;
    private double walkerMinY;
    private double walkerMaxY;
    private static final int JUMP_THRESHOLD = 5;

    /**
     * A walking monster.
     * @param coordinates The coordinates of the walker monster.
     * @param speed The speed at which the walker is going.
     * @param facingRight If the monster is facing right or not.
     * @param levelController The controllers that controls the level.
     */
    public Walker(Coordinates coordinates,
                  double speed,
                  boolean facingRight,
                  LevelController levelController) {
        super(coordinates, speed, facingRight, levelController);

        this.levelController = levelController;
        this.jumpCounter = 20;
        this.ableToJump = false;
        this.jumping = false;

        walkerMinX = Level.SPRITE_SIZE;
        walkerMaxX = Settings.SCENE_WIDTH - Level.SPRITE_SIZE;
        walkerMinY = Level.SPRITE_SIZE;
        walkerMaxY = Settings.SCENE_HEIGHT - Level.SPRITE_SIZE;
    }

    /**
     * The movement of the monster.
     */
    public void move() {
        if (!isCaughtByBubble()) {

            ableToJump = moveCollisionChecker(jumping, ableToJump);

            double jumpMaxCounter = Math.ceil(Settings.JUMP_HEIGHT_WALKER
                    / Settings.JUMP_SPEED_WALKER);

            if (jumpCounter < jumpMaxCounter) {
                jumpCounter++;
            } else if (jumpCounter == jumpMaxCounter) {
                getSpriteBase().setDy(0);
                jumping = false;
            }
            moveHorizontal();
            moveVertical();
            getSpriteBase().checkBounds(walkerMinX, walkerMaxX,
                    walkerMinY, walkerMaxY, levelController);
        } else {
            getSpriteBase().setDx(0);
            getSpriteBase().setDy(0);
            getSpriteBase().setX(getPrisonBubble().getSpriteBase().getX());
            getSpriteBase().setY(getPrisonBubble().getSpriteBase().getY());
        }

        

        super.move();
    }

    /**
     * This function handles the vertical movement.
     */
    private void moveVertical() {
            if (ableToJump && randInt() < JUMP_THRESHOLD) {
                ableToJump = false;
                jumping = true;
                getSpriteBase().setDy(-Settings.JUMP_SPEED_WALKER);
                jumpCounter = 0;
            }
    }

    /**
     * This function handles the horizontal movement.
     */
    private void moveHorizontal() {
        double x = getSpriteBase().getX();
        double y = getSpriteBase().getY();

        if (isFacingRight()) {
            if (!getSpriteBase().causesCollisionWall(x + getSpeed(),
                    x + getSpriteBase().getWidth() + getSpeed(), y,
                    y + getSpriteBase().getHeight(), levelController)) {
                getSpriteBase().setDx(getSpeed());
            } else {
                switchDirection();
            }
        } else {
            if (!getSpriteBase().causesCollisionWall(x - getSpeed(),
                    x + getSpriteBase().getWidth() - getSpeed(), y,
                    y + getSpriteBase().getHeight(), levelController)) {
                getSpriteBase().setDx(-getSpeed());
            } else {
                switchDirection();
            }
        }
    }

    /**
     * Switching the direction that the monster is facing.
     */
    public void switchDirection() {
        setFacingRight(!isFacingRight());
        if (isFacingRight()) {
            getSpriteBase().setImage("ZenChanRight.png");
        } else {
            getSpriteBase().setImage("ZenChanLeft.png");
        }

    }

    /**
     * Generate a random int.
     * @return a random int.
     */
    private int randInt() {
        Random rand = new Random();
        int min = 1;
        int max = 200;
        return rand.nextInt((max - min) + 1) + min;
    }

}
