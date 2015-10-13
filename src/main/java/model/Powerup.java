package model;

import controller.LevelController;
import utility.Logger;

import java.util.Observable;

/**
 * Created by Jim on 10/7/2015.
 *
 * @author Jim
 * @version 1.0
 * @since 10/7/2015
 */
public class Powerup extends Observable {

    private double destx;
    private double desty;
    private boolean ableToPickup;
    private boolean pickedUp;
    private int kindRounded;
    private SpriteBase spriteBase;

    public static final int AMOUNT_OF_POWERUPS = 5;
    public static final int POWERUP_SPEED = 1;
    public static final int POWERUP_LIFE = 2;
    public static final int POWERUP_BUBBLE = 3;
    public static final int POWERUP_MONSTER = 4;
    public static final int POWERUP_POINTS = 5;
    public static final int POWERUP_THRESHOLD = 10;

    /**
     * The constructor. It instantiates the class.
     *
     * @param kind The kind of Powerup and effect it has.
     *             If it is < 1 then it is random, but from 2 and up it can be forced.
     *             Then a static value should be used.
     * @param x The x to spawn on.
     * @param y The y to spawn on.
     * @param r The r to spawn with.
     * @param dx The dx to spawn with.
     * @param dy The dy to spawn with.
     * @param dr The dr to spawn with.
     * @param destx The randomly calculated destination x.
     * @param desty The randomly calculated destination y.
     * @param levelController The levelcontroller that instantiates this powerup.
     */
    public Powerup(double kind, double x, double y, double r, double dx, double dy, double dr,
                   double destx, double desty, LevelController levelController) {
        this.ableToPickup = false;
        this.pickedUp = false;
        this.destx = destx;
        this.desty = desty;
        
        this.spriteBase = new SpriteBase("/banana.gif", x, y, r, dx, dy, dr);

        this.addObserver(levelController);
        this.addObserver(levelController.getScreenController());

        if (kind < 1) {
            kindRounded = (int) Math.ceil(kind * AMOUNT_OF_POWERUPS);
        } else {
            kindRounded = (int) kind;
        }

        switch (kindRounded) {
            case POWERUP_SPEED: spriteBase.setImage("../banana.gif");
                break;
            case POWERUP_LIFE: spriteBase.setImage("../heart.gif");
                break;
            case POWERUP_BUBBLE: spriteBase.setImage("../apple.gif");
                break;
            case POWERUP_MONSTER: spriteBase.setImage("../melon.png");
                break;
            case POWERUP_POINTS: spriteBase.setImage("../coin.gif");
                break;
            default:
                Logger.log("No suitable image found!");
        }

    }

    /**
     * This function calculates the movement to the destx and desty.
     */
    public void move() {

        double diffX = destx - spriteBase.getX();
        double diffY = desty - spriteBase.getY();
        if (diffX < POWERUP_THRESHOLD && diffY < POWERUP_THRESHOLD) {
            spriteBase.setDx(0);
            spriteBase.setDy(0);
            ableToPickup = true;
        } else {
            spriteBase.setDx(diffX / 20.0);
            spriteBase.setDy(diffY / 20.0);
        }

        spriteBase.move();

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This is the function that checks if there is a collision with a player.
     * @param player The player there might be a collision with.
     */
    public void causesCollision(Player player, LevelController lvlController) {
        if (player.getSpriteBase().causesCollision(spriteBase.getX(), spriteBase.getX() + spriteBase.getWidth(),
                spriteBase.getY(), spriteBase.getY() + spriteBase.getHeight()) && ableToPickup) {
            pickedUp(player, lvlController);
        }
    }

    /**
     * The function that is called when there is a collision with a player.
     * The powerup should disappear.
     */
    private void pickedUp(Player player, LevelController lvlController) {
        if (!pickedUp) {
            setPickedUp(true);

            Logger.log("Picked up Powerup.");

            switch (kindRounded) {
                case POWERUP_SPEED:
                    player.activateSpeedPowerup();
                    break;
                case POWERUP_LIFE:
                    player.addLife();
                    break;
                case POWERUP_BUBBLE:
                    player.activateBubblePowerup();
                    break;
                case POWERUP_MONSTER:
                    lvlController.getCurrLvl().getMonsters().forEach(Monster::activateMonsterPowerup);
                    break;
                case POWERUP_POINTS:
                    player.scorePoints(50);
                    break;
                default:
                    Logger.log("Unknown Powerup int, should use static int.");
                    break;
            }

        }
    }

    public double getDestx() {
        return destx;
    }

    public void setDestx(double destx) {
        this.destx = destx;

        this.setChanged();
        this.notifyObservers();
    }

    public double getDesty() {
        return desty;
    }

    public void setDesty(double desty) {
        this.desty = desty;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isAbleToPickup() {
        return ableToPickup;
    }

    public void setAbleToPickup(boolean ableToPickup) {
        this.ableToPickup = ableToPickup;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isPickedUp() {

        if (pickedUp) {
            this.deleteObservers();
        }

        return pickedUp;

    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;

        this.setChanged();
        this.notifyObservers();
    }

    public int getKindRounded() {
        return kindRounded;
    }

    public void setKindRounded(int kindRounded) {
        this.kindRounded = kindRounded;

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
