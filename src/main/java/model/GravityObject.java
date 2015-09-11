package model;

/**
 * Created by toinehartman on 08/09/15.
 */
public abstract class GravityObject extends SpriteBase {

    private static final float GRAVITY_CONSTANT = 5.f;

    /**
     * The constructor that takes all parameters and creates a SpriteBase.
     * @param imageLoc The path of the image the player takes.
     * @param x The start x coordinate.
     * @param y The start y coordinate.
     * @param r The r.
     * @param dx The dx.
     * @param dy The dy.
     * @param dr The dr.
     */
    public GravityObject(String imageLoc,
                  double x,
                  double y,
                  double r,
                  double dx,
                  double dy,
                  double dr) {

        super(imageLoc, x, y, r, dx, dy, dr);

    }

    /**
     * This method calculates the gravity.
     * @return The gravity constant.
     */
    public float calculateGravity() {
        return -GRAVITY_CONSTANT;
    }
}
