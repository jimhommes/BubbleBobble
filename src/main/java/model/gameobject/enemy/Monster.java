package model.gameobject.enemy;

import controller.LevelController;
import javafx.animation.AnimationTimer;
import model.gameobject.GravityObject;
import model.gameobject.player.Player;
import model.gameobject.bubble.Bubble;
import model.support.Coordinates;
import model.support.SpriteBase;
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
    @SuppressWarnings("unused")
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

        this.addObserver(levelController);
        this.addObserver(levelController.getScreenController());
        this.timer = createTimer();
        timer.start();
    }

    /**
     * This function returns the timer of the monster.
     * @return Timer of the monster.
     */
    public AnimationTimer createTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!levelController.getLevelControllerMethods().getGamePaused()) {
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
                Logger.log("Monster was killed!");
            } else {
                Logger.log("Monster died!");
            }

            if (prisonBubble != null) {
                prisonBubble.setIsPopped(true);
            }

            if (Settings.getBoolean("USE_POWERUPS", true)) {
                levelController.spawnPowerup(this);
            }

            setChanged();
            notifyObservers();
            destroy();
        }
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
     * This function returns the sprite base of the monster.
     *
     * @return The sprite base.
     */
    public SpriteBase getSpriteBase() {
        return spriteBase;
    }

    /**
     * Multiply the speed by a factor.
     * @param factor the factor.
     */
    public void factorSpeed(double factor) {
        this.setSpeed(factor * this.getSpeed());
    }

    /**
     * This function forces the player to die entirely.
     */
    public void destroy() {
        this.deleteObservers();
        timer.stop();
    }
    
    /**
     * Switching the direction that the monster is facing.
     * @param monster in the monster that is changing direction.
     */
    public void switchDirection(String monster) {
      isFacingRight = !isFacingRight;
        if (isFacingRight) {
            spriteBase.setImage(monster + "Right.png");
        } else {
            spriteBase.setImage(monster + "Left.png");
        }
    }

    /**
     * This sets the prison bubble (Should only be used for testing).
     * @param prisonBubble The prison bubble.
     */
    public void setPrisonBubble(Bubble prisonBubble) {
        this.prisonBubble = prisonBubble;
    }

    /**
     * This sets the spriteBase (Should only be used for testing).
     * @param spriteBase The sprite Base.
     */
    public void setSpriteBase(SpriteBase spriteBase) {
        this.spriteBase = spriteBase;
    }
}