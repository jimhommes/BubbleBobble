package model;

import controller.LevelController;
import javafx.animation.AnimationTimer;
import utility.Logger;
import utility.Settings;

/**
 * This class is where the monsters are created.
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
    private AnimationTimer timer;

    /**
     * The monster that is trying to catch the character.
     *
     * @param coordinates     The coordinates of the monster.
     * @param speed           The speed at which the monster is travelling.
     * @param isFacingRight   Whether the monster is facing to the right or not.
     * @param levelController is the controller that controls the level.
     */
    public Monster(Coordinates coordinates,
                   double speed, boolean isFacingRight,
                   LevelController levelController) {

        this.speed = speed;
        this.isFacingRight = isFacingRight;
        this.isCaughtByBubble = false;
        this.levelController = levelController;
        this.isDead = false;
        this.isReducedSpeed = false;

        this.spriteBase = new SpriteBase("/ZenChanLeft.png", coordinates);

        this.addObserver(levelController);
        this.addObserver(levelController.getScreenController());
        this.timer = createTimer();
        timer.start();
    }

    private AnimationTimer createTimer() {
        return new AnimationTimer() {
            @SuppressWarnings("unchecked")
            @Override
            public void handle(long now) {
                if (!levelController.getGamePaused()) {
                    levelController.getBubbles().forEach(Monster.this::checkCollision);
                    move();
                }

                setChanged();
                notifyObservers();
            }
        };

    }

    /**
     * The movement of the monster.
     */
    public void move() {
        spriteBase.move();

        Double newX = spriteBase.getX() + spriteBase.getDx();
        Double newY = spriteBase.getY() + spriteBase.getDy();

        if (!newX.equals(spriteBase.getX()) || !newY.equals(spriteBase.getY())) {
            Logger.log(String.format("Monster moved from (%f, %f) to (%f, %f)",
                    spriteBase.getX(), spriteBase.getY(), newX, newY));
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
            if (((bubbleX >= spriteBase.getX()
                    && bubbleX <= spriteBase.getX() + spriteBase.getWidth())
                    || (bubbleX2 >= spriteBase.getX()
                    && bubbleX2 <= spriteBase.getX() + spriteBase.getWidth()))
                    && ((bubbleY >= spriteBase.getY()
                    && bubbleY <= spriteBase.getY() + spriteBase.getHeight())
                    || bubbleY2 >= spriteBase.getY()
                    && bubbleY2 <= spriteBase.getY() + spriteBase.getHeight())) {
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
                prisonBubble.setIsPopped(true);
                levelController.spawnPowerup(this);

                setChanged();
                notifyObservers();
                destroy();

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
    void checkPowerups() {

    }

    /**
     * Check for collision combined with jumping.
     *
     * @param jumping    The variable whether a GravityObject is jumping.
     * @param ableToJump The variable whether a GravityObject is able to jump.
     * @return The ableToJump variable.
     */
    public boolean moveCollisionChecker(boolean jumping, boolean ableToJump) {
        if (!spriteBase.causesCollisionWall(spriteBase.getX(),
                spriteBase.getX() + spriteBase.getWidth(),
                spriteBase.getY() - calculateGravity(),
                spriteBase.getY() + spriteBase.getHeight() - calculateGravity(), levelController)) {
            if (!jumping) {
                spriteBase.setY(spriteBase.getY() - calculateGravity());
            }
            ableToJump = false;
        } else {
            if (!jumping) {
                ableToJump = true;
            }
        }
        return ableToJump;
    }

    /**
     * This function returns the speed.
     *
     * @return The speed.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * This function sets the speed.
     *
     * @param speed The speed.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * This function returns whether the monster faces right.
     *
     * @return True if facing right.
     */
    public boolean isFacingRight() {
        return isFacingRight;
    }

    /**
     * This function sets whether the monster faces right.
     *
     * @param facingRight True if facing right.
     */
    public void setFacingRight(boolean facingRight) {
        this.isFacingRight = facingRight;
    }

    /**
     * This function returns the prisonbubble.
     *
     * @return The prisonbubble.
     */
    public Bubble getPrisonBubble() {
        return prisonBubble;
    }

    /**
     * This function returns whether the monster is caught by a bubble.
     *
     * @return True if caught by bubble.
     */
    public boolean isCaughtByBubble() {
        return isCaughtByBubble;
    }

    /**
     * This function returns whether the monster is dead.
     *
     * @return True if dead.
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * This function sets whether the monster is dead.
     * If the monster is killed the function die() should be used.
     *
     * @param dead True if dead.
     */
    public void setDead(boolean dead) {
        this.isDead = dead;
    }

    /**
     * This function returns true if the monster has reduced speed.
     *
     * @return True if it has reduced speed.
     */
    public boolean isReducedSpeed() {
        return isReducedSpeed;
    }

    /**
     * This function sets whether the monster has reduced speed.
     *
     * @param reducedSpeed True if it has reduced speed.
     */
    public void setReducedSpeed(boolean reducedSpeed) {
        this.isReducedSpeed = reducedSpeed;
    }

    /**
     * This function returns the sprite base of the monster.
     *
     * @return The sprite base.
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
