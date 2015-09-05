package model; /**
 * Created by Jim on 9/5/2015.
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
