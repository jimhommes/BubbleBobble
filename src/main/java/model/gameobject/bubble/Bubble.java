package model.gameobject.bubble;

import controller.LevelController;
import javafx.animation.AnimationTimer;
import model.support.Coordinates;
import model.support.SpriteBase;
import utility.Logger;
import utility.Settings;

import java.util.Observable;

/**
 * This class is where the bubbles are created.
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
     *
     * @param firedRight      If the bubble was fired to the right.
     * @param powerup         if the bubble is shot during bubble powerup.
     * @param levelController that controller of the level where the bubble is in.
     */
    public Bubble(boolean firedRight,
                  boolean powerup,
                  LevelController levelController) {

        counter = 0;
        this.firedRight = firedRight;
        this.isAbleToCatch = true;
        this.isPrisonBubble = false;
        this.levelController = levelController;
        this.isPopped = false;
        
        this.addObserver(levelController);
        this.addObserver(levelController.getScreenController());

        this.powerup = powerup;
        this.timer = createTimer();
        timer.start();
    }

    /**
     * The timer of the Bubble.
     * @return The timer.
     */
    public AnimationTimer createTimer() {
        return new AnimationTimer() {
            @SuppressWarnings("unchecked")
            @Override
            public void handle(long now) {
                if (!levelController.getLevelControllerMethods().getGamePaused()) {
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
            setIsPopped(counter > Settings.BUBBLE_LIVE_TIME && !isPrisonBubble);
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

        logMove();
        spriteBase.move();
        checkPop();
    }

    private void logMove() {
        Double newX = spriteBase.getX() + spriteBase.getDx();
        Double newY = spriteBase.getY() + spriteBase.getDy();

        if (!newX.equals(spriteBase.getX()) || !newY.equals(spriteBase.getY())) {
            Logger.log(String.format("Bubble moved from (%f, %f) to (%f, %f)",
                    spriteBase.getX(), spriteBase.getY(), newX, newY));
        }
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
     *
     * @return True if able to catch a monster.
     */
    public boolean isAbleToCatch() {
        return isAbleToCatch;
    }

    /**
     * This function sets if the bubble is able to catch a monster.
     *
     * @param ableToCatch True if able to catch a monster.
     */
    public void setAbleToCatch(boolean ableToCatch) {
        this.isAbleToCatch = ableToCatch;
    }

    /**
     * This function sets if the bubble is a prisonbubble.
     * A prisonbubble is a bubble that captured a monster.
     *
     * @param prisonBubble True if a prisonbubble.
     */
    public void setPrisonBubble(boolean prisonBubble) {
        isPrisonBubble = prisonBubble;
    }

    /**
     * This function returns the sprite of this bubble.
     *
     * @return The sprite.
     */
    public SpriteBase getSpriteBase() {
        return spriteBase;
    }

    /**
     * This function returns whether the bubble is popped.
     *
     * @return True if popped.
     */
    public boolean getIsPopped() {
        return isPopped;
    }

    /**
     * This function sets if the bubble is popped or not.
     *
     * @param isPopped True if popped.
     */
    public void setIsPopped(boolean isPopped) {
        this.isPopped = isPopped;
        if (isPopped) {
            setChanged();
            notifyObservers();
            destroy();
        }
    }

    /**
     * This function forces the player to die entirely.
     */
    public void destroy() {
        this.deleteObservers();
        timer.stop();
    }

    /**
     * This function is only for testing purposes.
     */
    public void forceUpdate() {
        setChanged();
        notifyObservers();
    }

    /**
     * This is for testing purposes only.
     * @param spriteBase The spritebase to be set.
     */
    public void setSpriteBase(SpriteBase spriteBase) {
        this.spriteBase = spriteBase;
    }

    /**
     * This is for testing purposes only.
     * @param counter The counter to be set.
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * This function returns if the bubble is a prison bubble.
     * @return True if prison bubble.
     */
    public boolean getPrisonBubble() {
        return isPrisonBubble;
    }
}