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
public class Monster extends GravityObject {

    private final double speed;
    private boolean facingRight;

    /**
     * The monster that is trying to catch the character.
     * @param layer The level in where the game is played.
     * @param image The image of the monster.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param r The rotation.
     * @param dx The dx of x.
     * @param dy The dy of y.
     * @param dr The dr of r.
     * @param speed The speed at which the monster is travelling.
     * @param facingRight Whether the monster is facing to the right or not.
     */
    public Monster(Pane layer, Image image, double x, double y, double r,
                   double dx, double dy, double dr, double speed, boolean facingRight) {
        super(layer, image, x, y, r, dx, dy, dr);

        this.speed = speed;
        this.facingRight = facingRight;
    }

    /**
     * The movement of the monster.
     */
    public void move() {
        if (facingRight) {
            dx = speed;
        } else {
            dx = -speed;
        }

        dy = -calculateGravity();

        super.move();
    }

    /**
     * Switching the direction that the monster is facing.
     */
    public void switchDirection() {
        facingRight = !facingRight;
    }

}