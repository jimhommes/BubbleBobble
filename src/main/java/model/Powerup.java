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

    private double xLocation;
    private double yLocation;
    private double rotation;
    private double dX;
    private double dY;
    private double dR;

    public static final int AMOUNT_OF_POWERUPS = 5;
    public static final int POWERUP_SPEED = 1;
    public static final int POWERUP_LIFE = 2;
    public static final int POWERUP_BUBBLE = 3;
    public static final int POWERUP_MONSTER = 4;
    public static final int POWERUP_POINTS = 5;

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

        this.xLocation = x;
        this.yLocation = y;
        this.rotation = r;
        this.dX = dx;
        this.dY = dy;
        this.dR = dr;

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
        double diffX = destx - getxLocation();
        double diffY = desty - getyLocation();
        if (diffX < 1 && diffY < 1) {
            setdX(0);
            setdY(0);
            ableToPickup = true;
        } else {
            setdX(diffX / 20.0);
            setdY(diffY / 20.0);
        }
    }

    /**
     * This is the function that checks if there is a collision with a player.
     * @param player The player there might be a collision with.
     */
    public void causesCollision(Player player, LevelController lvlController) {
        if (player.getSpriteBase().causesCollision(getxLocation(), getxLocation() + spriteBase.getWidth(),
                getyLocation(), getyLocation() + spriteBase.getHeight()) && ableToPickup) {
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
                    lvlController.getCurrLvl().getMonsters().forEach((monster) -> {
                        ((Monster) monster).activateMonsterPowerup();
                    });
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
}
