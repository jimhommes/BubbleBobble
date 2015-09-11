package model;

/**
 * @author Jim
 * @since 9/5/2015
 * @version 0.1
 */


import javafx.scene.layout.Pane;

/**
 * The class that represents a wall in the game.
 */
public class Wall extends SpriteBase {

    public static final String WALL_SPRITE = "../BubbleBobbleWall32b.png";

    /**
     * The constructor that implements the X and Y coordinate.
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param layer The wall layer
     * @param imageLoc The path of the image used for the wall
     * @param r The rotation
     * @param dx The dx of x
     * @param dy The dy of y
     * @param dr The dr of r
     */
    public Wall(Pane layer, String imageLoc, double x, double y, double r,
                double dx, double dy, double dr) {
        super(imageLoc, x, y, r, dx, dy, dr);
    }
}
