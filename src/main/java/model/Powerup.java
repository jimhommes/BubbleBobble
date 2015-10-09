package model;

import controller.LevelController;
import utility.Logger;

/**
 * Created by Jim on 10/7/2015.
 *
 * @author Jim
 * @version 1.0
 * @since 10/7/2015
 */
public class Powerup extends SpriteBase {

    private double destx;
    private double desty;
    private boolean ableToPickup;
    private LevelController levelController;
    private boolean pickedUp;
    private int kindRounded;

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
        super("../banana.gif", x, y, r, dx, dy, dr);
        this.ableToPickup = false;
        this.pickedUp = false;
        this.destx = destx;
        this.desty = desty;
        this.levelController = levelController;

        if (kind < 1) {
            kindRounded = (int) Math.ceil(kind * AMOUNT_OF_POWERUPS);
        } else {
            kindRounded = (int) kind;
        }

        switch (kindRounded) {
            case POWERUP_SPEED: setImage("../banana.gif");
                break;
            case POWERUP_LIFE: setImage("../heart.gif");
                break;
            case POWERUP_BUBBLE: setImage("../apple.gif");
                break;
            case POWERUP_MONSTER: setImage("../melon.png");
                break;
            case POWERUP_POINTS: setImage("../coin.gif");
                break;
            default:
                Logger.log("No suitable image found!");
        }


    }

    /**
     * This function calculates the movement to the destx and desty.
     */
    @Override
    public void move() {
        double diffX = destx - getX();
        double diffY = desty - getY();
        if (diffX < 1 && diffY < 1) {
            setDx(0);
            setDy(0);
            ableToPickup = true;
        } else {
            setDx(diffX / 20.0);
            setDy(diffY / 20.0);
        }

        super.move();
    }

    /**
     * This is the function that checks if there is a collision with a player.
     * @param player The player there might be a collision with.
     */
    public void causesCollision(SpriteBase player) {
        if (player.causesCollision(getX(), getX() + getWidth(),
                getY(), getY() + getHeight()) && ableToPickup) {
            pickedUp((Player) player);
        }
    }

    /**
     * The function that is called when there is a collision with a player.
     * The powerup should dissappear.
     */
    private void pickedUp(Player player) {
        if (!pickedUp) {
            levelController.getScreenController().removeSprite(this);
            pickedUp = true;
            Logger.log("Picked up Powerup.");

            switch (kindRounded) {
                case POWERUP_SPEED:
                    player.activateSpeedPowerup();
                    break;
                case POWERUP_LIFE:
                    //TODO: Call extra life function
                    break;
                case POWERUP_BUBBLE:
                    player.activateBubblePowerup();
                    break;
                case POWERUP_MONSTER:
                    levelController.getCurrLvl().getMonsters().forEach((monster) -> {
                        ((Monster) monster).activateMonsterPowerup();
                    });
                    break;
                case POWERUP_POINTS:
                    //TODO: the player scores 50 points
                    //player.scorePoints(50); (has to be merged first).
                    break;
                default:
                    Logger.log("Unknown Powerup int, should use static int.");
                    break;
            }

        }
    }

    /**
     * This returns the boolean whether this has been picked up.
     * @return True if it has been picked up.
     */
    public boolean getPickedUp() {
        return pickedUp;
    }

    /**
     * This function returns the ableToPickup.
     * @return True if able to pick up.
     */
    public boolean getAbleToPickup() {
        return ableToPickup;
    }

    /**
     * This function sets the boolean ableToPickUp.
     * @param ableToPickup True if able to pick up
     */
    public void setAbleToPickup(boolean ableToPickup) {
        this.ableToPickup = ableToPickup;
    }
}
