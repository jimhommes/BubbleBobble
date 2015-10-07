package model;

import controller.LevelController;

import java.util.ArrayList;

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
     * The constructor. It needs all the parameters and creates the image where planned.
     *
     * @param x         The x coordinate.
     * @param y         The y coordinate.
     * @param r         The r coordinate.
     * @param dx        The difference in x.
     * @param dy        The difference in y.
     * @param dr        The difference in r.
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

    public void causesCollision(SpriteBase player) {
        if(player.causesCollision(getX(), getX() + getWidth(), getY(), getY() + getHeight()) && ableToPickup) {
            pickedUp();
        }
    }

    private void pickedUp() {
        //increase score
        levelController.getScreenController().removeSprite(this);
        pickedUp = true;
    }

    public boolean getPickedUp() {
        return pickedUp;
    }
}
