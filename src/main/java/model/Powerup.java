package model;

import controller.LevelController;

/**
 * Created by Jim on 10/7/2015.
 *
 * @author Jim
 * @version 1.0
 * @since 10/7/2015
 */
public class Powerup extends SpriteBase {

    private double destx;
    private double desty;
    private boolean ableToPickup;
    private LevelController levelController;
    private boolean pickedUp;

    /**
     * The constructor. It instantiates the class.
     *
     * @param x The x to spawn on.
     * @param y The y to spawn on.
     * @param r The r to spawn with.
     * @param dx The dx to spawn with.
     * @param dy The dy to spawn with.
     * @param dr The dr to spawn with.
     * @param destx The randomly calculated destination x.
     * @param desty The randomly calculated destination y.
     * @param levelController The levelcontroller that instantiates this powerup.
     */
    public Powerup(double x, double y, double r, double dx, double dy, double dr,
                   double destx, double desty, LevelController levelController) {
        super("../banana.gif", x, y, r, dx, dy, dr);
        this.ableToPickup = false;
        this.pickedUp = false;
        this.destx = destx;
        this.desty = desty;
        this.levelController = levelController;
    }

    /**
     * This function calculates the movement to the destx and desty.
     */
    @Override
    public void move() {
        double diffX = destx - getX();
        double diffY = desty - getY();
        if (diffX < 1 && diffY < 1) {
            setDx(0);
            setDy(0);
            ableToPickup = true;
        } else {
            setDx(diffX / 20.0);
            setDy(diffY / 20.0);
        }

        super.move();
    }

    /**
     * This is the function that checks if there is a collision with a player.
     * @param player The player there might be a collision with.
     */
    public void causesCollision(SpriteBase player) {
        if (player.causesCollision(getX(), getX() + getWidth(),
                getY(), getY() + getHeight()) && ableToPickup) {
            pickedUp();
        }
    }

    /**
     * The function that is called when there is a collision with a player.
     * The powerup should dissappear.
     */
    private void pickedUp() {
        //TODO: increase score
        if (!pickedUp) {
            levelController.getScreenController().removeSprite(this);
            pickedUp = true;
        }
    }

    /**
     * This returns the boolean whether this has been picked up.
     * @return True if it has been picked up.
     */
    public boolean getPickedUp() {
        return pickedUp;
    }

    /**
     * This function returns the ableToPickup.
     * @return True if able to pick up.
     */
    public boolean getAbleToPickup() {
        return ableToPickup;
    }

    /**
     * This function sets the boolean ableToPickUp.
     * @param ableToPickup True if able to pick up
     */
    public void setAbleToPickup(boolean ableToPickup) {
        this.ableToPickup = ableToPickup;
    }
}
