package model;

/**
 * @author Jim
 * @since 9/5/2015
 * @version 0.1
 */


/**
 * The class that represents a wall in the game.
 */
public class Wall  {

    private SpriteBase spriteBase;

    /**
     * The constructor that implements the X and Y coordinate.
     * @param coordinates The coordinates of the wall.
     */
    public Wall(Coordinates coordinates) {
        this.spriteBase = new SpriteBase("BubbleBobbleWall32b.png", coordinates);
    }

    /**
     * This function returns the spriteBase.
     * @return The spriteBase.
     */
    public SpriteBase getSpriteBase() {
        return spriteBase;
    }

}
