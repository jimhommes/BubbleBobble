package model;

import controller.LevelController;
import utility.Logger;
import utility.Settings;

/**
 * Created by Jim on 9/8/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/8/2015
 */
public class Monster extends GravityObject {

    private final LevelController levelController;

    private double speed;
    private boolean isFacingRight;
    private Bubble prisonBubble;
    private boolean isCaughtByBubble;
    private boolean isDead;
    private boolean isReducedSpeed;
    private SpriteBase spriteBase;

    private double xLocation;
    private double yLocation;
    private double rotation;
    private double dX;
    private double dY;
    private double dR;

    /**
     * The monster that is trying to catch the character.
     *
     * @param x               The x coordinate.
     * @param y               The y coordinate.
     * @param r               The rotation.
     * @param dx              The dx of x.
     * @param dy              The dy of y.
     * @param dr              The dr of r.
     * @param speed           The speed at which the monster is travelling.
     * @param isFacingRight     Whether the monster is facing to the right or not.
     * @param levelController is the controller that controls the level.
     */
    public Monster(double x, double y, double r,
                   double dx, double dy, double dr,
                   double speed, boolean isFacingRight,
                   LevelController levelController) {

        this.speed = speed;
        this.isFacingRight = isFacingRight;
        this.isCaughtByBubble = false;
        this.levelController = levelController;
        this.isDead = false;
        this.isReducedSpeed = false;

        this.xLocation = x;
        this.yLocation = y;
        this.rotation = r;
        this.dX = dx;
        this.dY = dy;
        this.dR = dr;

        this.spriteBase = new SpriteBase("/ZenChanLeft.png", x, y, r, dx, dy, dr);

        this.addObserver(levelController);
        this.addObserver(levelController.getScreenController());
    }

    /**
     * The movement of the monster.
     */
    public void move() {
        Double newX = getxLocation() + getdX();
        Double newY = getyLocation() + getdY();

        if (!newX.equals(getxLocation()) || !newY.equals(getyLocation())) {
            Logger.log(String.format("Monster moved from (%f, %f) to (%f, %f)",
                    getxLocation(), getyLocation(), newX, newY));
        }

        checkPowerups();
        if (this.isReducedSpeed) {
            setSpeed(Settings.MONSTER_SLOWDOWN_FACTOR * Settings.MONSTER_SPEED);
        }
    }

    /**
     * Checks to see if the bubble has collided with the monster.
     *
     * @param bubble the bubble that is shot from the character.
     */
    public void checkCollision(final Bubble bubble) {
        if (bubble.isAbleToCatch() && !isCaughtByBubble) {
            double bubbleX = bubble.getSpriteBase().getX();
            double bubbleY = bubble.getSpriteBase().getY();
            double bubbleX2 = bubbleX + bubble.getSpriteBase().getWidth();
            double bubbleY2 = bubbleY + bubble.getSpriteBase().getHeight();
            if (((bubbleX >= getxLocation() && bubbleX <= getxLocation() + spriteBase.getWidth())
                    || (bubbleX2 >= getxLocation() && bubbleX2 <= getxLocation() + spriteBase.getWidth()))
                    && ((bubbleY >= getyLocation() && bubbleY <= getyLocation() + spriteBase.getHeight())
                    || bubbleY2 >= getyLocation() && bubbleY2 <= getyLocation() + spriteBase.getHeight())) {
                prisonBubble = bubble;
                prisonBubble.setAbleToCatch(false);
                prisonBubble.setPrisonBubble(true);
                isCaughtByBubble = true;

                Logger.log("Monster is caught by bubble!");
            }
        }
    }

    /**
     * This method is used when the monsters are killed.
     *
     * @param killer The player that killed the monster.
     */
    public void die(Player killer) {
        if (!isDead) {
        	setDead(true);

            if (killer != null) {
                killer.scorePoints(Settings.POINTS_KILL_MONSTER);
                levelController.spawnPowerup(this);
//                notifyAllObservers(killer.getSpriteBase(), 2);
                Logger.log("Monster was killed!");
            } else {
                Logger.log("Monster died!");
            }
        }
    }

    /**
     * Activate the reduced speed powerup.
     */
    public void activateMonsterPowerup() {
        setReducedSpeed(true);
    }

    /**
     * Check if the powerups expired.
     * Is used in subclass.
     */
    public void checkPowerups() {

    }

    /**
     * Check for collision combined with jumping.
     * @param jumping The variable whether a GravityObject is jumping.
     * @param ableToJump The variable whether a GravityObject is able to jump.
     * @return The ableToJump variable.
     */
    public boolean moveCollisionChecker(boolean jumping, boolean ableToJump) {
        if (!spriteBase.causesCollisionWall(getxLocation(),
                getxLocation() + spriteBase.getWidth(),
                getyLocation() - calculateGravity(),
                getyLocation() + spriteBase.getHeight() - calculateGravity(), levelController)) {
            if (!jumping) {
                setyLocation(getyLocation() - calculateGravity());
            }
            ableToJump = false;
        } else {
            if (!jumping) {
                ableToJump = true;
            }
        }
        return ableToJump;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;

        this.setChanged();
        this.notifyObservers();
    }

    public LevelController getLevelController() {
        return levelController;
    }

    public boolean isFacingRight() {
        return isFacingRight;
    }

    public void setFacingRight(boolean facingRight) {
        this.isFacingRight = facingRight;

        this.setChanged();
        this.notifyObservers();
    }

    public Bubble getPrisonBubble() {
        return prisonBubble;
    }

    public void setPrisonBubble(Bubble prisonBubble) {
        this.prisonBubble = prisonBubble;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isCaughtByBubble() {
        return isCaughtByBubble;
    }

    public void setCaughtByBubble(boolean caughtByBubble) {
        this.isCaughtByBubble = caughtByBubble;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        this.isDead = dead;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isReducedSpeed() {
        return isReducedSpeed;
    }

    public void setReducedSpeed(boolean reducedSpeed) {
        this.isReducedSpeed = reducedSpeed;

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
