package model;

import controller.LevelController;
import javafx.animation.AnimationTimer;
import utility.Logger;
import utility.Settings;

import java.util.Observable;

/**
 * Created by Jim on 9/8/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/8/2015
 */

/**
 * This class is the base where the sprite is loaded.
 * Any instance that is represented by a sprite extends this class.
 */
public class Bubble extends Observable {

    private int counter;
    private boolean firedRight;
    private boolean isAbleToCatch;
    private boolean isPrisonBubble;
    private LevelController levelController;
    private boolean powerup;
    private SpriteBase spriteBase;
    private boolean isPopped;
    private AnimationTimer timer;

    /**
     * The bubble that will be shot to catch the monsters.
     * @param x The x coordinate 
     * @param y The y coordinate
     * @param r The rotation
     * @param dx The dx of x
     * @param dy The dy of y
     * @param dr The dr of r
     * @param firedRight If the bubble was fired to the right.
     * @param powerup if the bubble is shot during bubble powerup.
     * @param levelController that controller of the level where the bubble is in.
     */
    public Bubble(double x,
                  double y,
                  double r,
                  double dx,
                  double dy,
                  double dr,
                  boolean firedRight,
                  boolean powerup,
                  LevelController levelController) {

        counter = 0;
        this.firedRight = firedRight;
        this.isAbleToCatch = true;
        this.isPrisonBubble = false;
        this.levelController = levelController;
        this.isPopped = false;

        this.spriteBase = new SpriteBase("/bubble.png", x, y, r, dx, dy, dr);

        this.addObserver(levelController);
        this.addObserver(levelController.getScreenController());

        this.powerup = powerup;
        this.timer = createTimer();
        timer.start();
    }

    private AnimationTimer createTimer() {
        return new AnimationTimer() {
            @SuppressWarnings("unchecked")
            @Override
            public void handle(long now) {
                if (!levelController.getGamePaused()) {
                    move();
                }

                setChanged();
                notifyObservers();
            }
        };
    }

    /**
     * This method is used to check if a bubble is .
     */
    public void checkPop() {
        if (!isPopped) {
            isPopped = counter > Settings.BUBBLE_LIVE_TIME && !isPrisonBubble;
        }
    }

    /**
     * This method is used to move the bubble.
     */
    public void move() {

        counter++;

        if ((!this.powerup && counter < Settings.BUBBLE_FLY_TIME)
                || (this.powerup && counter < Settings.BUBBLE_POWERUP_FLY_TIME)) {
            moveHorizontally();
        } else {
            moveVertically();
        }

        Double newX = spriteBase.getX() + spriteBase.getDx();
        Double newY = spriteBase.getY() + spriteBase.getDy();

        if (!newX.equals(spriteBase.getX()) || !newY.equals(spriteBase.getY())) {
            Logger.log(String.format("Bubble moved from (%f, %f) to (%f, %f)",
                    spriteBase.getX(), spriteBase.getY(), newX, newY));
        }

        spriteBase.move();

        checkPop();

    }

    /**
     * This function handles the vertical movement, 
     * it allows the bubbles to float to the screen but stop there..
     */
    private void moveVertically() {
        spriteBase.setDx(0);
        if (!spriteBase.causesCollisionWall(spriteBase.getX(),
                spriteBase.getX() + spriteBase.getWidth(),
                spriteBase.getY() - Settings.BUBBLE_FLY_SPEED,
                spriteBase.getY() + spriteBase.getHeight() - Settings.BUBBLE_FLY_SPEED,
                levelController)) {
            spriteBase.setDy(-Settings.BUBBLE_FLY_SPEED);
            if (spriteBase.getY() < 0) {
                spriteBase.setY(Settings.SCENE_HEIGHT);
            }
        } else if (spriteBase.getY() <= 35) {
            spriteBase.setDy(0);
        }
        isAbleToCatch = false;
    }

    /**
     * This function handles the horizontal movement.
     */
    private void moveHorizontally() {
        if (firedRight) {
            if (!spriteBase.causesCollisionWall(spriteBase.getX() + Settings.BUBBLE_INIT_SPEED,
                    spriteBase.getX() + spriteBase.getWidth() + Settings.BUBBLE_INIT_SPEED,
                    spriteBase.getY(),
                    spriteBase.getY() + spriteBase.getHeight(), levelController)) {
                spriteBase.setDx(Settings.BUBBLE_INIT_SPEED);
            } else {
                spriteBase.setDx(0);
            }
        } else {
            if (!spriteBase.causesCollisionWall(spriteBase.getX() - Settings.BUBBLE_INIT_SPEED,
                    spriteBase.getX() + spriteBase.getWidth() - Settings.BUBBLE_INIT_SPEED,
                    spriteBase.getY(),
                    spriteBase.getY() + spriteBase.getHeight(), levelController)) {
                spriteBase.setDx(-Settings.BUBBLE_INIT_SPEED);
            } else {
                spriteBase.setDx(0);
            }
        }
    }

    /**
     * This function returns if the bubble is able to catch a monster.
     * @return True if able to catch a monster.
     */
    public boolean isAbleToCatch() {
        return isAbleToCatch;
    }

    /**
     * This function sets if the bubble is able to catch a monster.
     * @param ableToCatch True if able to catch a monster.
     */
    public void setAbleToCatch(boolean ableToCatch) {
        this.isAbleToCatch = ableToCatch;
    }

    /**
     * This function sets if the bubble is a prisonbubble.
     * A prisonbubble is a bubble that captured a monster.
     * @param prisonBubble True if a prisonbubble.
     */
    public void setPrisonBubble(boolean prisonBubble) {
        isPrisonBubble = prisonBubble;
    }

    /**
     * This function returns the sprite of this bubble.
     * @return The sprite.
     */
    public SpriteBase getSpriteBase() {
        return spriteBase;
    }

    /**
     * This function returns whether the bubble is popped.
     * @return True if popped.
     */
    public boolean getIsPopped() {
        if (isPopped) {
            this.deleteObservers();
        }
        return isPopped;
    }

    /**
     * This function sets whether the bubble is popped.
     * @param isPopped True if popped.
     */
    public void setIsPopped(boolean isPopped) {
        this.isPopped = isPopped;
    }
}
