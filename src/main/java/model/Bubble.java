package model;

import controller.LevelController;
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

    private double xLocation;
    private double yLocation;
    private double rotation;
    private double dX;
    private double dY;
    private double dR;

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

        this.xLocation = x;
        this.yLocation = y;
        this.rotation = r;
        this.dX = dx;
        this.dY = dy;
        this.dR = dr;

        counter = 0;
        this.firedRight = firedRight;
        this.isAbleToCatch = true;
        this.isPrisonBubble = false;
        this.levelController = levelController;

        this.spriteBase = new SpriteBase("/bubble.png", x, y, r, dx, dy, dr);

        this.addObserver(levelController);
        this.addObserver(levelController.getScreenController());

        this.powerup = powerup;
    }

    /**
     * This method is used to check if a bubble is .
     * @return true if Bubble extends life_time of Bubble
     */
    public boolean checkPop() {
        return (counter > Settings.BUBBLE_LIVE_TIME);
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

        Double newX = getxLocation() + getdX();
        Double newY = getyLocation() + getdY();

        if (!newX.equals(getxLocation()) || !newY.equals(getyLocation())) {
            Logger.log(String.format("Bubble moved from (%f, %f) to (%f, %f)",
                    getxLocation(), getyLocation(), newX, newY));
        }
    }

    /**
     * This function handles the vertical movement, 
     * it allows the bubbles to float to the screen but stop there..
     */
    private void moveVertically() {
        setdX(0);
        if (!spriteBase.causesCollisionWall(getxLocation(), getxLocation() + spriteBase.getWidth(),
                getyLocation() - Settings.BUBBLE_FLY_SPEED,
                getyLocation() + spriteBase.getHeight() - Settings.BUBBLE_FLY_SPEED, levelController)) {
            setdY(-Settings.BUBBLE_FLY_SPEED);
            if (getyLocation() < 0) {
                setyLocation(Settings.SCENE_HEIGHT);
            }
        } else if (getyLocation() <= 35) {
            setdY(0);
        }
        isAbleToCatch = false;
    }

    /**
     * This function handles the horizontal movement.
     */
    private void moveHorizontally() {
        if (firedRight) {
            if (!spriteBase.causesCollisionWall(getxLocation() + Settings.BUBBLE_INIT_SPEED,
                    getxLocation() + spriteBase.getWidth() + Settings.BUBBLE_INIT_SPEED,
                    getyLocation(),
                    getyLocation() + spriteBase.getHeight(), levelController)) {
                setdX(Settings.BUBBLE_INIT_SPEED);
            } else {
                setdX(0);
            }
        } else {
            if (!spriteBase.causesCollisionWall(getxLocation() - Settings.BUBBLE_INIT_SPEED,
                    getxLocation() + spriteBase.getWidth() - Settings.BUBBLE_INIT_SPEED,
                    getyLocation(),
                    getyLocation() + spriteBase.getHeight(), levelController)) {
                setdX(-Settings.BUBBLE_INIT_SPEED);
            } else {
                setdX(0);
            }
        }
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isFiredRight() {
        return firedRight;
    }

    public void setFiredRight(boolean firedRight) {
        this.firedRight = firedRight;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isAbleToCatch() {
        return isAbleToCatch;
    }

    public void setAbleToCatch(boolean ableToCatch) {
        this.isAbleToCatch = ableToCatch;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isPrisonBubble() {
        return isPrisonBubble;
    }

    public void setPrisonBubble(boolean prisonBubble) {
        isPrisonBubble = prisonBubble;

        this.setChanged();
        this.notifyObservers();
    }

    public LevelController getLevelController() {
        return levelController;
    }

    public void setLevelController(LevelController levelController) {
        this.levelController = levelController;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isPowerup() {
        return powerup;
    }

    public void setPowerup(boolean powerup) {
        this.powerup = powerup;

        this.setChanged();
        this.notifyObservers();
    }

    public double getxLocation() {
        return xLocation;
    }

    public void setxLocation(double xLocation) {
        this.xLocation = xLocation;

        this.setChanged();
        this.notifyObservers();
    }

    public double getyLocation() {
        return yLocation;
    }

    public void setyLocation(double yLocation) {
        this.yLocation = yLocation;

        this.setChanged();
        this.notifyObservers();
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;

        this.setChanged();
        this.notifyObservers();
    }

    public double getdX() {
        return dX;
    }

    public void setdX(double dX) {
        this.dX = dX;

        this.setChanged();
        this.notifyObservers();
    }

    public double getdY() {
        return dY;
    }

    public void setdY(double dY) {
        this.dY = dY;

        this.setChanged();
        this.notifyObservers();
    }

    public double getdR() {
        return dR;
    }

    public void setdR(double dR) {
        this.dR = dR;

        this.setChanged();
        this.notifyObservers();
    }

    public SpriteBase getSpriteBase() {
        return spriteBase;
    }

    public void setSpriteBase(SpriteBase spriteBase) {
        this.spriteBase = spriteBase;

        this.setChanged();
        this.notifyObservers();
    }

}
