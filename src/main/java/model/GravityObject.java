package model;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Created by toinehartman on 08/09/15.
 */
public abstract class GravityObject extends SpriteBase {

    private final static float GRAVITY_CONSTANT = 2.f;

    private double playerMinX;
    private double playerMaxX;
    private double playerMinY;
    private double playerMaxY;
    private Input input;
    private double speed;

    /**
     * The constructor that takes all parameters and creates a SpriteBase.
     * @param layer The layer the player moves in.
     * @param image The image the player takes.
     * @param x The start x coordinate.
     * @param y The start y coordinate.
     * @param r The r.
     * @param dx The dx.
     * @param dy The dy.
     * @param dr The dr.
     */
    public GravityObject(Pane layer,
                  Image image,
                  double x,
                  double y,
                  double r,
                  double dx,
                  double dy,
                  double dr) {

        super(layer, image, x, y, r, dx, dy, dr);

    }

    /**
     * This method calculates the gravity.
     * @return The gravity constant.
     */
    public float calculateGravity() {
        return -GRAVITY_CONSTANT;
    }
}
