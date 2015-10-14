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
        
        this.spriteBase = new SpriteBase("../banana.gif", x, y, r, dx, dy, dr);

        this.addObserver(levelController);
        this.addObserver(levelController.getScreenController());

        if (kind < 1) {
            kindRounded = (int) Math.ceil(kind * AMOUNT_OF_POWERUPS);
        } else {
            kindRounded = (int) kind;
        }

        setCorrectImage(kindRounded);

    }

    private void setCorrectImage(int kindRounded) {
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
     * @param lvlController The levelController.
     */
    public void causesCollision(Player player, LevelController lvlController) {
        if (player.getSpriteBase().causesCollision(spriteBase.getX(),
                spriteBase.getX() + spriteBase.getWidth(),
                spriteBase.getY(), spriteBase.getY()
                        + spriteBase.getHeight()) && ableToPickup) {
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
                    lvlController.getCurrLvl().getMonsters()
                            .forEach(Monster::activateMonsterPowerup);
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

    /**
     * This function returns whether the powerup is able to be picked up.
     * @return True if able to pick up.
     */
    public boolean isAbleToPickup() {
        return ableToPickup;
    }

    /**
     * This function sets whether the powerup is able to be picked up.
     * @param ableToPickup True if able to pick up.
     */
    public void setAbleToPickup(boolean ableToPickup) {
        this.ableToPickup = ableToPickup;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function returns whether the powerup is picked up.
     * @return True if picked up.
     */
    public boolean isPickedUp() {

        if (pickedUp) {
            this.deleteObservers();
        }

        return pickedUp;

    }

    /**
     * This function sets whether the powerup is picked up.
     * @param pickedUp True if picked up.
     */
    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function returns the spritebase.
     * @return The spritebase.
     */
    public SpriteBase getSpriteBase() {
        return spriteBase;
    }
    
}
