package model;

import controller.LevelController;
import utility.Settings;
import java.util.Random;

/**
 *  Walker class which is a kind of monster, which just moves around and kills
 *  the player when they touch. It can be captured by a bubble shot by the player.
 */
public class Walker extends Monster {

    /**
     * This is the levelController.
     */
    private LevelController levelController;

    /**
     * This boolean indicates whether the monster is jumping.
     */
    private boolean jumping;

    /**
     * This boolean indicates whether the player is ready for a jump.
     */
    private boolean ableToJump;

    /**
     * This counter is used to check how long the player is in the air.
     */
    private int jumpCounter;

    /**
     * This is the minimal X coordinate the walker can move around in.
     */
    private double walkerMinX;

    /**
     * This is the maximal X coordinate the walker can move around in.
     */
    private double walkerMaxX;

    /**
     * This is the minimal Y coordinate the walker can move around in.
     */
    private double walkerMinY;

    /**
     * This is the maximal Y coordinate the walker can move around in.
     */
    private double walkerMaxY;

    private static final int JUMP_THRESHOLD = 5;

    /**
     * A walking monster.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param r The rotation of the walker.
     * @param dx The dx of x.
     * @param dy The dy of y.
     * @param dr The dr of r.
     * @param speed The speed at which the walker is going.
     * @param facingRight If the monster is facing right or not.
     * @param levelController The controllers that controls the level.
     */
    public Walker(double x,
                  double y,
                  double r,
                  double dx,
                  double dy,
                  double dr,
                  double speed,
                  boolean facingRight,
                  LevelController levelController) {
        super("../ZenChanRight.png", x, y, r, dx, dy, dr, speed, facingRight, levelController);

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
                setDy(0);
                jumping = false;
            }
            moveHorizontal();
            moveVertical();
            checkBounds(walkerMinX, walkerMaxX, walkerMinY, walkerMaxY, levelController);
        } else {
        	System.out.println(getX());
            setDx(0);
            setDy(0);
            setX(getPrisonBubble().getX());
            setY(getPrisonBubble().getY());
            System.out.println(getX());
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
                setDy(-Settings.JUMP_SPEED_WALKER);
                jumpCounter = 0;
            }
    }

    /**
     * This function handles the horizontal movement.
     */
    private void moveHorizontal() {
        if (isFacingRight()) {
            if (!levelController.causesCollision(getX() + getSpeed(),
                    getX() + getWidth() + getSpeed(), getY(), getY() + getHeight())) {
                setDx(getSpeed());
            } else {
                switchDirection();
            }
        } else {
            if (!levelController.causesCollision(getX() - getSpeed(),
                    getX() + getWidth() - getSpeed(), getY(), getY() + getHeight())) {
                setDx(-getSpeed());
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
    }

    private int randInt() {
        Random rand = new Random();
        int min = 1;
        int max = 200;
        int res = rand.nextInt((max - min) + 1) + min;
        return res;
    }

}
