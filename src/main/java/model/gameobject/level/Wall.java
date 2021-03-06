package model.gameobject.level;

import model.support.Coordinates;
import model.support.SpriteBase;

/**
 * This class represents a wall in the game.
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
