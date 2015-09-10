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

    public static String BUBBLE_SPRITE = "../bubbleWithoutBackground.png";

    private int counter;

    private boolean firedRight;

    private boolean ableToCatch;

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
