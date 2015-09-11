package model;

import controller.LevelController;

/**
 * Created by Jim on 9/8/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/8/2015
 */
public class Walker extends Monster {

    LevelController levelController;

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
    }

    /**
     * The movement of the monster.
     */
    public void move() {
        if (!isCaughtByBubble()) {
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
            if (!levelController.causesCollision(getX(), getX() + getWidth(), 
            		getY() - calculateGravity(), getY() + getHeight() - calculateGravity())) {
                setDy(-calculateGravity());
            } else {
                setDy(0);
            }
        } else {
            setDx(0);
            setDy(0);
            setX(getPrisonBubble().getX());
            setY(getPrisonBubble().getY());
        }
        super.move();
    }

    /**
     * Switching the direction that the monster is facing.
     */
    public void switchDirection() {
        setFacingRight(!isFacingRight());
    }
}
