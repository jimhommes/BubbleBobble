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
public class Bubble extends SpriteBase {

    final static String BUBBLE_SPRITE = "../bubble.png";

    private int counter;

    private boolean firedRight;

    private boolean ableToCatch;

    /**
     * The bubble that will be shot to catch the monsters.
     * @param layer The level in where the layer is
     * @param image The image of the bubble
     * @param x The x coordinate 
     * @param y The y coordinate
     * @param r The rotation
     * @param dx The dx of x
     * @param dy The dy of y
     * @param dr The dr of r
     * @param firedRight If the bubble was fired to the right.
     */
    public Bubble(Pane layer,
                  Image image,
                  double x,
                  double y,
                  double r,
                  double dx,
                  double dy,
                  double dr,
                  boolean firedRight) {
        super(layer, image, x, y, r, dx, dy, dr);

        counter = 0;
        this.firedRight = firedRight;
        this.ableToCatch = true;
    }

    /**
     * This method is used to move the bubble.
     */
    public void move() {
        if (counter < 30) {
            counter++;
            if (firedRight) {
                dx = 7;
            } else {
                dx = -7;
            }
        } else {
            dx = 0;
            dy = -5;
            ableToCatch = false;
        }

        super.move();
    }

}