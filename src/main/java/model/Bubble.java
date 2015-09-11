package model;

import controller.LevelController;

/**
 * Created by Jim on 9/8/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/8/2015
 */
public class Bubble extends SpriteBase {

    /**
     * The counter that is needed for the movement of the bubble.
     */
    private int counter;

    /**
     * This boolean indicates whether it is fired to the right.
     */
    private boolean firedRight;

    /**
     * Only in the first stage of firing the bubble, the bubble is able to catch monsters.
     * This boolean indicates if the bubble can catch a monster.
     */
    private boolean ableToCatch;

    /**
     * The levelController that created the player.
     */
    private LevelController levelController;

    /**
     * The bubble that will be shot to catch the monsters.
     * @param x The x coordinate 
     * @param y The y coordinate
     * @param r The rotation
     * @param dx The dx of x
     * @param dy The dy of y
     * @param dr The dr of r
     * @param firedRight If the bubble was fired to the right.
     */
    public Bubble(double x,
                  double y,
                  double r,
                  double dx,
                  double dy,
                  double dr,
                  boolean firedRight,
                  LevelController levelController) {
        super("../bubble.png", x, y, r, dx, dy, dr);

        counter = 0;
        this.firedRight = firedRight;
        this.ableToCatch = true;
        this.levelController = levelController;

    }

    /**
     * This method is used to move the bubble.
     */
    public void move() {
        if (counter < 30) {
            counter++;
            if (firedRight) {
                if(!levelController.causesCollision(getX() + Settings.BUBBLE_INIT_SPEED,
                        getX() + getWidth() + Settings.BUBBLE_INIT_SPEED,
                        getY(),
                        getY() + getHeight())) {
                    setDx(Settings.BUBBLE_INIT_SPEED);
                } else {
                    setDx(0);
                }
            } else {
                if(!levelController.causesCollision(getX() - Settings.BUBBLE_INIT_SPEED,
                        getX() + getWidth() - Settings.BUBBLE_INIT_SPEED,
                        getY(),
                        getY() + getHeight())) {
                    setDx(-Settings.BUBBLE_INIT_SPEED);
                } else {
                    setDx(0);
                }
            }
        } else {
            setDx(0);
            if(!levelController.causesCollision(getX(), getX() + getWidth(),
                    getY() - Settings.BUBBLE_FLY_SPEED,
                    getY() + getHeight() - Settings.BUBBLE_FLY_SPEED)) {
                setDy(-Settings.BUBBLE_FLY_SPEED);
            }
            ableToCatch = false;
        }

        super.move();
    }

    /**
     * This method sets the boolean to whether it is able to be caught.
     * @param bool set to the boolean of whether the monsters can be caught.
     */
    public void setAbleToCatch(final boolean bool) {
        ableToCatch = bool;
    }

    /**
     * This method checks to see if a monster is able to be caught.
     * @return true if the monster can be caught.
     */
    public boolean getAbleToCatch() {
        return ableToCatch;
    }
}
