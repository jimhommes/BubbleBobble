package model;

import controller.LevelController;

import java.util.Random;

/**
 * Created by Jim on 9/8/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/8/2015
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

    private static final int JUMPTRESHOLD = 2;

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
        this.jumpCounter = 30;
        this.ableToJump = false;
        this.jumping = false;
    }

    /**
     * The movement of the monster.
     */
    public void move() {
        if (!isCaughtByBubble()) {
            if (jumpCounter < 12) {
                jumpCounter++;
            }

            if (jumpCounter == 12) {
                setDy(0);
                jumping = false;
            }
            if (!jumping) {
                ableToJump = true;
            }
            moveHorizontal();
            moveVertical();
        } else {
            setDx(0);
            setDy(0);
            setX(getPrisonBubble().getX());
            setY(getPrisonBubble().getY());
        }
        super.move();
    }

    /**
     * This function handles the vertical movement.
     */
    private void moveVertical() {
        if (!levelController.causesCollision(getX(), getX() + getWidth(),
                getY() - calculateGravity(), getY() + getHeight() - calculateGravity())) {
            if (ableToJump && randInt() < JUMPTRESHOLD) {
                ableToJump = false;
                jumping = true;
                setDy(calculateGravity());
                jumpCounter = 0;
//                setY(getY() - calculateGravity());
            } else {
                if (!jumping) {
                    setDy(-calculateGravity());
                }
            }
        } else {
            setDy(0);
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
        int max = 10;
        int res = rand.nextInt((max - min) + 1) + min;
        System.out.println(res);
        return res;
    }

}
