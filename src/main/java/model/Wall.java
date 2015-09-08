package model;

/**
 * @author Jim
 * @since 9/5/2015
 * @version 0.1
 */


import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * The class that represents a wall in the game.
 */
public class Wall extends SpriteBase {

    /**
     * The X coordinate.
     */
    private int x;

    /**
     * The Y coordinate.
     */
    private int y;

    public static String WALL_SPRITE = "../BubbleBobbleWall32b.png";

    /**
     * The constructor that implements the X and Y coordinate.
     * @param x The X coordinate
     * @param y The Y coordinate
     */
    public Wall(Pane layer, Image image, double x, double y, double r,
                double dx, double dy, double dr) {
        super(layer, image, x, y, r, dx, dy, dr);
    }
}
