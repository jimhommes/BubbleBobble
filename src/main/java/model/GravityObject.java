package model;

import controller.LevelController;

/**
 * Class that represents object affected by gravity.
 */
public abstract class GravityObject extends SpriteBase {

    private static final float GRAVITY_CONSTANT = 5.f;

    private LevelController levelController;

    /**
     * The constructor that takes all parameters and creates a SpriteBase.
     * @param imageLoc The path of the image the player takes.
     * @param x The start x coordinate.
     * @param y The start y coordinate.
     * @param r The r.
     * @param dx The dx.
     * @param dy The dy.
     * @param dr The dr.
     * @param levelController The levelController.
     */
    public GravityObject(String imageLoc,
                         double x,
                         double y,
                         double r,
                         double dx,
                         double dy,
                         double dr,
                         LevelController levelController) {

        super(imageLoc, x, y, r, dx, dy, dr);
        this.levelController = levelController;
    }

    /**
     * This method calculates the gravity.
     * @return The gravity constant.
     */
    public float calculateGravity() {
        return -GRAVITY_CONSTANT;
    }

    /**
     * Check for collision combined with jumping.
     * @param jumping The variable whether a GravityObject is jumping.
     * @param ableToJump The variable whether a GravityObject is able to jump.
     * @return The ableToJump variable.
     */
    public boolean moveCollisionChecker(boolean jumping, boolean ableToJump) {
        if (!causesCollisionWall(getX(),
                getX() + getWidth(),
                getY() - calculateGravity(),
                getY() + getHeight() - calculateGravity(), levelController)) {
            if (!jumping) {
                setY(getY() - calculateGravity());
            }
            ableToJump = false;
        } else {
            if (!jumping) {
                ableToJump = true;
            }
        }
        return ableToJump;
    }

}
