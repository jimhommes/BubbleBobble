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
public class Monster extends SpriteBase {

    private final double speed;
    private boolean facingRight;

    public Monster(Pane layer, Image image, double x, double y, double r,
                   double dx, double dy, double dr, double speed, boolean facingRight) {
        super(layer,image,x,y,r,dx,dy,dr);

        this.speed = speed;
        this.facingRight = facingRight;
    }

    public void move() {
        if (facingRight) {
            dx += speed;
        } else {
            dx -= speed;
        }

        super.move();
    }

    public void switchDirection() {
        facingRight = !facingRight;
    }

}
