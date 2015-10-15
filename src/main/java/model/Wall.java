package model;

/**
 * @author Jim
 * @since 9/5/2015
 * @version 0.1
 */

/**
 * This class represents a wall in the game.
 */
public class Wall  {

    private SpriteBase spriteBase;

    /**
     * The constructor that implements the X and Y coordinate.
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param r The rotation
     * @param dx The dx of x
     * @param dy The dy of y
     * @param dr The dr of r
     */
    public Wall(Coordinates coordinates) {
        this.spriteBase = new SpriteBase("../BubbleBobbleWall32b.png", coordinates);
    }

    /**
     * This function returns the spriteBase.
     * @return The spriteBase.
     */
    public SpriteBase getSpriteBase() {
        return spriteBase;
    }

}
