package model;

import controller.LevelController;

/**
 * Created by Jim on 9/8/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/8/2015
 */
public class Monster extends GravityObject {

    private final double speed;
    private final LevelController levelController;
    private boolean facingRight;
    private Bubble prisonBubble;
    private boolean caughtByBubble;
    private boolean dead;

    /**
     * The monster that is trying to catch the character.
     *
     * @param imagePath       the path to the image.
     * @param x               The x coordinate.
     * @param y               The y coordinate.
     * @param r               The rotation.
     * @param dx              The dx of x.
     * @param dy              The dy of y.
     * @param dr              The dr of r.
     * @param speed           The speed at which the monster is travelling.
     * @param facingRight     Whether the monster is facing to the right or not.
     * @param levelController is the controller that controls the level.
     */
    public Monster(String imagePath, double x, double y, double r,
                   double dx, double dy, double dr, double speed, boolean facingRight,
                   LevelController levelController) {
        super(imagePath, x, y, r, dx, dy, dr);

        this.speed = speed;
        this.facingRight = facingRight;
        this.caughtByBubble = false;
        this.levelController = levelController;
        this.dead = false;
    }

    /**
     * The movement of the monster.
     */
    public void move() {
        super.move();
    }

    /**
     * Checks to see if the bubble has collided with the monster.
     *
     * @param bubble the bubble that is shot from the character.
     */
    public void checkCollision(final Bubble bubble) {
        if (bubble.getAbleToCatch() && !caughtByBubble) {
            double bubbleX = bubble.getX();
            double bubbleY = bubble.getY();
            double bubbleX2 = bubbleX + getWidth();
            double bubbleY2 = bubbleY + getHeight();
            if (((bubbleX >= getX() && bubbleX <= getX() + getWidth())
                    || (bubbleX2 >= getX() && bubbleX2 <= getX() + getWidth()))
                    && ((bubbleY >= getY() && bubbleY <= getY() + getHeight())
                    || bubbleY2 >= getY() && bubbleY2 <= getY() + getHeight())) {
                prisonBubble = bubble;
                prisonBubble.setAbleToCatch(false);
                caughtByBubble = true;
            }
        }
    }

    /**
     * This method is used when the monsters are killed.
     */
    public void die() {
        if (!dead) {
            levelController.getScreenController().removeSprite(this);
            levelController.getScreenController().removeSprite(prisonBubble);
            dead = true;
        }
    }

    /**
     * This function returns the speed.
     *
     * @return The speed.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * This function sets the boolean that indicates if the monster is facing right.
     *
     * @return True if facing right.
     */
    public boolean isFacingRight() {
        return facingRight;
    }

    /**
     * This functions sets the boolean if the monster is facing right.
     *
     * @param facingRight The boolean if the monster is facing right.
     */
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    /**
     * This function returns the bubble that imprisons the monster.
     *
     * @return The bubble that imprisons the monster.
     */
    public Bubble getPrisonBubble() {
        return prisonBubble;
    }

    /**
     * This boolean indicates if the monster is caught by a bubble.
     *
     * @return True if caught by a bubble.
     */
    public boolean isCaughtByBubble() {
        return caughtByBubble;
    }

    /**
     * This method checks if the player is dead.
     *
     * @return true if they are dead.
     */
    public boolean isDead() {
        return dead;
    }
}
