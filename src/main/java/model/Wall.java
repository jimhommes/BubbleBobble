package model;

/**
 * @author Jim
 * @since 9/5/2015
 * @version 0.1
 */


/**
 * The class that represents a wall in the game.
 */
public class Wall {

    /**
     * The X coordinate.
     */
    private int x;

    /**
     * The Y coordinate.
     */
    private int y;

    /**
     * The constructor that implements the X and Y coordinate.
     * @param x The X coordinate
     * @param y The Y coordinate
     */
    public Wall(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
}
