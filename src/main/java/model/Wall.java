package model;

/**
 * @author Jim
 * @since 9/5/2015
 * @version 0.1
 */


/**
 * The class that represents a wall in the game.
 */
public class Wall extends SpriteBase {

    /**
     * This is the location of the image.
     */
    public static final String WALL_SPRITE = "../BubbleBobbleWall32b.png";

    /**
     * The constructor that implements the X and Y coordinate.
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param r The rotation
     * @param dx The dx of x
     * @param dy The dy of y
     * @param dr The dr of r
     */
    public Wall(double x, double y, double r,
                double dx, double dy, double dr) {
        super("../BubbleBobbleWall32b.png", x, y, r, dx, dy, dr);
    }
}
