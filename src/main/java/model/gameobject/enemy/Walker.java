package model.gameobject.enemy;

import controller.LevelController;
import model.support.Coordinates;
import model.support.SpriteBase;
import utility.Settings;
import java.util.Random;

/**
 *  This class represents a type of monster, which just moves around and kills
 *  the player when they collide. It can be captured by a bubble shot by the player.
 */
public class Walker extends Monster {

    private LevelController levelController;
    private boolean jumping;
    private boolean ableToJump;
    private int jumpCounter;
    private double walkerMinX;
    private double walkerMaxX;
    private double walkerMinY;
    private double walkerMaxY;
    private static final int JUMP_THRESHOLD = 5;

    /**
     * A walking monster.
     * @param coordinates The coordinates of the walker monster.
     * @param speed The speed at which the walker is going.
     * @param facingRight If the monster is facing right or not.
     * @param levelController The controllers that controls the level.
     */
    public Walker(Coordinates coordinates,
                  double speed,
                  boolean facingRight,
                  LevelController levelController) {
        super(coordinates, speed, facingRight, levelController);

        setSpriteBase(new SpriteBase("ZenChanLeft.png", coordinates));
        
        this.levelController = levelController;
        this.jumpCounter = 20;
        this.ableToJump = false;
        this.jumping = false;

        walkerMinX = Settings.SPRITE_SIZE / 2;
        walkerMaxX = Settings.SCENE_WIDTH - Settings.SPRITE_SIZE / 2;
        walkerMinY = Settings.SPRITE_SIZE / 2;
        walkerMaxY = Settings.SCENE_HEIGHT - Settings.SPRITE_SIZE / 2;
    }

    /**
     * The movement of the monster.
     */
    public void move() {
        if (!isCaughtByBubble()) {

            ableToJump = moveCollisionChecker(jumping, ableToJump);

            double jumpMaxCounter = Math.ceil(Settings.JUMP_HEIGHT_WALKER
                    / Settings.JUMP_SPEED_WALKER);

            if (jumpCounter < jumpMaxCounter) {
                jumpCounter++;
            } else if (jumpCounter == jumpMaxCounter) {
                getSpriteBase().setDyCoordinate(0);
                jumping = false;
            }
            moveHorizontal();
            moveVertical();
            getSpriteBase().checkBounds(walkerMinX, walkerMaxX,
                    walkerMinY, walkerMaxY, levelController);
        } else {
            getSpriteBase().setDxCoordinate(0);
            getSpriteBase().setDyCoordinate(0);
            getSpriteBase().setXCoordinate(getPrisonBubble().getSpriteBase().getXCoordinate());
            getSpriteBase().setYCoordinate(getPrisonBubble().getSpriteBase().getYCoordinate());
        }

        

        super.move();
    }

    /**
     * This function handles the vertical movement.
     */
    private void moveVertical() {
            if (ableToJump && randInt() < JUMP_THRESHOLD) {
                ableToJump = false;
                jumping = true;
                getSpriteBase().setDyCoordinate(-Settings.JUMP_SPEED_WALKER);
                jumpCounter = 0;
            }
    }

    /**
     * This function handles the horizontal movement.
     */
    private void moveHorizontal() {
        double x = getSpriteBase().getXCoordinate();
        double y = getSpriteBase().getYCoordinate();

        if (isFacingRight()) {
            if (!getSpriteBase().causesCollisionWall(x + getSpeed(),
                    x + getSpriteBase().getWidth() + getSpeed(), y,
                    y + getSpriteBase().getHeight(), levelController)) {
                getSpriteBase().setDxCoordinate(getSpeed());
            } else {
                switchDirection("ZenChan");
            }
        } else {
            if (!getSpriteBase().causesCollisionWall(x - getSpeed(),
                    x + getSpriteBase().getWidth() - getSpeed(), y,
                    y + getSpriteBase().getHeight(), levelController)) {
                getSpriteBase().setDxCoordinate(-getSpeed());
            } else {
                switchDirection("ZenChan");
            }
        }
    }

    /**
     * Generate a random int.
     * @return a random int.
     */
    private int randInt() {
        Random rand = new Random();
        int min = 1;
        int max = 200;
        return rand.nextInt((max - min) + 1) + min;
    }

}