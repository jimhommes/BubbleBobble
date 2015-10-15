package model;

import controller.LevelController;
import javafx.animation.AnimationTimer;
import utility.Logger;

import java.util.Observable;

/**
 * This class creates the different types of power ups that are used in the game.
 */
public class Powerup extends Observable {

    private final LevelController levelController;
    private double destx;
    private double desty;
    private boolean ableToPickup;
    private boolean pickedUp;
    private int kindRounded;
    private SpriteBase spriteBase;

    private static final int AMOUNT_OF_POWERUPS = 5;
    private static final int POWERUP_SPEED = 1;
    private static final int POWERUP_LIFE = 2;
    private static final int POWERUP_BUBBLE = 3;
    private static final int POWERUP_MONSTER = 4;
    private static final int POWERUP_POINTS = 5;
    private static final int POWERUP_THRESHOLD = 10;

    private AnimationTimer timer;

    /**
     * The constructor. It instantiates the class.
     *
     * @param kind The kind of Powerup and effect it has.
     *             If it is < 1 then it is random, but from 2 and up it can be forced.
     *             Then a static value should be used.
     * @param coordinates The coordinates of the PowerUp.
     * @param destx The randomly calculated destination x.
     * @param desty The randomly calculated destination y.
     * @param levelController The levelcontroller that instantiates this powerup.
     */
    public Powerup(double kind, Coordinates coordinates,
                   double destx, double desty, LevelController levelController) {
        this.ableToPickup = false;
        this.pickedUp = false;
        this.destx = destx;
        this.desty = desty;
        
        this.levelController = levelController;
        this.spriteBase = new SpriteBase("../banana.gif", coordinates);

        this.addObserver(levelController);
        this.addObserver(levelController.getScreenController());

        if (kind < 1) {
            kindRounded = (int) Math.ceil(kind * AMOUNT_OF_POWERUPS);
        } else {
            kindRounded = (int) kind;
        }

        setCorrectImage(kindRounded);

        this.timer = createTimer();
        timer.start();

    }

    private AnimationTimer createTimer() {
        return new AnimationTimer() {
            @SuppressWarnings("unchecked")
            @Override
            public void handle(long now) {
                if (!levelController.getGamePaused()) {
                    levelController.getPlayers().forEach(Powerup.this::causesCollision);
                    move();
                }

                setChanged();
                notifyObservers();
            }
        };
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
    }

    /**
     * This is the function that checks if there is a collision with a player.
     * @param player The player there might be a collision with.
     */
    public void causesCollision(Player player) {
        if (player.getSpriteBase().causesCollision(spriteBase.getX(),
                spriteBase.getX() + spriteBase.getWidth(),
                spriteBase.getY(), spriteBase.getY()
                        + spriteBase.getHeight()) && ableToPickup) {
            pickedUp(player);
        }
    }

    /**
     * The function that is called when there is a collision with a player.
     * The powerup should disappear.
     */
    private void pickedUp(Player player) {
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
                    levelController.getCurrLvl().getMonsters()
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
    }

    /**
     * This function returns whether the powerup is picked up.
     * @return True if picked up.
     */
    public boolean getPickedUp() { return pickedUp; }

    /**
     * This function sets whether the powerup is picked up.
     * @param pickedUp True if picked up.
     */
    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;

        if (pickedUp) {
            setChanged();
            notifyObservers();
            destroy();
        }
    }

    /**
     * This function returns the spritebase.
     * @return The spritebase.
     */
    public SpriteBase getSpriteBase() {
        return spriteBase;
    }

    /**
     * This function forces the player to die entirely.
     */
    public void destroy() {
        this.deleteObservers();
        timer.stop();
    }
    
}
