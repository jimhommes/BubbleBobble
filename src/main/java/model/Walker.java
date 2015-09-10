package model;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Created by Jim on 9/8/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/8/2015
 */
public class Walker extends Monster {

    public static String WALKER_IMAGE_LEFT = "../ZenChanLeft.png";
    public static String WALKER_IMAGE_RIGHT = "../ZenChanRight.png";

    /**
     * A walking monster.
     * @param layer The level in which the monster is walking.
     * @param image The image of the walking monster.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param r The rotation of the walker.
     * @param dx The dx of x.
     * @param dy The dy of y.
     * @param dr The dr of r.
     * @param speed The speed at which the walker is going.
     * @param facingRight If the monster is facing right or not.
     */
    public Walker(Pane layer,
                  Image image,
                  double x,
                  double y,
                  double r,
                  double dx,
                  double dy,
                  double dr,
                  double speed,
                  boolean facingRight) {
        super(layer, image, x, y, r, dx, dy, dr, speed, facingRight);
    }
}
