package model;

import controller.LevelController;
import utility.Logger;
import utility.Settings;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This is the player class. It has a sprite to display.
 */
public class Player extends GravityObject {

    /**
     * This boolean indicates whether the player is jumping.
     */
    private boolean jumping;

    /**
     * The input that defines the movement of the player.
     */
    private Input input;

    /**
     * The speed of the player.
     */
    private double speed;

    /**
     * The bubbles the player fired.
     */
    private ArrayList<Bubble> bubbles;

    /**
     * The boolean for which dir the player is facing.
     */
    private boolean facingRight;

    /**
     * The counter that is needed to define the movement of the character,
     * and the fire rate of the bubbles.
     */
    private int counter;

    /**
     * This boolean indicates whether the player is dead.
     */
    private boolean isDead;

    /**
     * This boolean indicates whether the player has died, and has no lives left.
     */
    private boolean gameOver;

    /**
     * This is the levelController.
     */
    private LevelController levelController;

    /**
     * This boolean indicates whether the player is ready for a jump.
     */
    private boolean ableToJump;

    /**
     * This is the minimal X coordinate the player can move around in.
     */
    private double playerMinX;

    /**
     * This is the maximal X coordinate the player can move around in.
     */
    private double playerMaxX;

    /**
     * This is the minimal Y coordinate the player can move around in.
     */
    private double playerMinY;

    /**
     * This is the maximal Y coordinate the player can move around in.
     */
    private double playerMaxY;

    /**
     * This indicates whether the player is able to double jump.
     */
    private boolean isAbleToDoubleJump;

    private boolean doubleSpeed;
    private int doubleSpeedCounter;
    private int durationDoubleSpeed = 200;

    private boolean bubblePowerup;
    private int bubblePowerupCounter;
    private int durationBubblePowerup = 400;

    /**
     * The constructor that takes all parameters and creates a SpriteBase.
     *
     * @param x               The start x coordinate.
     * @param y               The start y coordinate.
     * @param r               The r.
     * @param dx              The dx.
     * @param dy              The dy.
     * @param dr              The dr.
     * @param speed           The speed of the player.
     * @param input           The input the player will use.
     * @param levelController The controller that controls the level.
     */
    public Player(double x,
                  double y,
                  double r,
                  double dx,
                  double dy,
                  double dr,
                  double speed,
                  Input input,
                  LevelController levelController) {

        super("../BubRight.png", x, y, r, dx, dy, dr, levelController);

        this.speed = speed;
        this.input = input;
        this.bubbles = new ArrayList<>();
        this.counter = 31;
        this.ableToJump = false;
        this.isAbleToDoubleJump = false;
        this.jumping = false;
        this.isDead = false;
        this.gameOver = false;
        this.facingRight = true;
        this.levelController = levelController;


        playerMinX = Level.SPRITE_SIZE;
        playerMaxX = Settings.SCENE_WIDTH - Level.SPRITE_SIZE;
        playerMinY = Level.SPRITE_SIZE;
        playerMaxY = Settings.SCENE_HEIGHT - Level.SPRITE_SIZE;

        attach(levelController);
        attach(levelController.getScreenController());
    }

    /**
     * The function that processes the input.
     */
    public void processInput() {

        if (!isDead) {
            if (jumping && getDy() <= 0) {
                setDy(getDy() + 0.6);
            } else if (jumping && getDy() > 0) {
                setDy(getDy() + 0.6);
                jumping = false;
            } else {
                setDy(0);
            }

            moveVertical();
            moveHorizontal();
            checkFirePrimary();
        } else {
            checkIfGameOver();
        }

        checkBounds(playerMinX, playerMaxX, playerMinY, playerMaxY, levelController);
        checkPowerups();
    }

    private void checkPowerups() {
        if (doubleSpeed) {
            doubleSpeedCounter++;
            if (doubleSpeedCounter >= durationDoubleSpeed) {
                doubleSpeed = false;
                speed = Settings.PLAYER_SPEED;
                doubleSpeedCounter = 0;
            }
        } else {
            doubleSpeedCounter = 0;
        }

        if (bubblePowerup) {
            bubblePowerupCounter++;
            if (bubblePowerupCounter >= durationBubblePowerup) {
                bubblePowerup = false;

                bubblePowerupCounter = 0;
            }
        } else {
            bubblePowerupCounter = 0;
        }
    }

    /**
     * The move function that applies the movement to the player.
     */
    @Override
    public void move() {
        applyGravity();

        Double newX = getX() + getDx();
        Double newY = getY() + getDy();

        if (!newX.equals(getX()) || !newY.equals(getY())) {
            Logger.log(String.format("Player moved from (%f, %f) to (%f, %f)",
                    getX(), getY(), newX, newY));
        }

        super.move();
    }

    /**
     * This function applies gravity.
     */
    private void applyGravity() {
        if (!causesCollisionWall(getX(), getX() + getWidth(),
                getY() - calculateGravity(), getY() + getHeight() - calculateGravity(), 
                levelController)
                || causesCollisionWall(getX(), getX() + getWidth(),
                getY(), getY() + getHeight(), levelController)) {
            if (!jumping) {
                if (isAbleToDoubleJump
                        && causesBubbleCollision(getX(), getX() + getWidth(),
                        getY() - calculateGravity(),
                        getY() + getHeight() - calculateGravity())) {
                    ableToJump = true;
                    isAbleToDoubleJump = false;
                } else if (isAbleToDoubleJump) {
                    ableToJump = false;
                }
                setY(getY() - calculateGravity());
            } else {
                ableToJump = false;
            }
        } else {
            if (!jumping) {
                ableToJump = true;
                isAbleToDoubleJump = true;
            }
        }
    }


    /**
     * This function checks if the player collides with a bubble.
     * @param x Minimal x.
     * @param x1 Maximal x.
     * @param y Minimal y.
     * @param y2 Maximal y.
     * @return True if collision.
     */
    @SuppressWarnings("unchecked")
	private boolean causesBubbleCollision(double x, double x1, double y, double y2) {
        ArrayList<Bubble> bubbles = new ArrayList<>();
        levelController.getPlayers().forEach(player -> {
            Player p = (Player) player;
            bubbles.addAll(p.getBubbles());
        });


        if (bubbles.size() == 0) {
            return false;
        } else {
            boolean res = false;
            for (Bubble bubble : bubbles) {
                if (bubble.causesCollision(x, x1, y, y2) && !bubble.getAbleToCatch()) {
                    res = true;
                }
            }
            return res;
        }
    }

    /**
     * This method checks if the monster has collides with the character.
     *
     * @param monster is the monster that is being checked for collisions.
     */
    public void checkCollideMonster(final Monster monster) {

        if (monster.causesCollision(getX(), getX() + getWidth(), getY(), getY() + getHeight())) {
            if (!monster.isCaughtByBubble()) {
                die();
            } else {
                monster.die();
            }
        }

    }

    /**
     * This method is used when the character is killed.
     */
    public void die() {
        this.isDead = true;
        setDx(0);
        setDy(0);
        counter = 0;
        notifyAllObservers(this, 1);
        
    }

    /**
     * This method will remove a bubble from the ArrayList of Bubbles shot by the Player.
     */
    public void checkBubbles() {

        Iterator<Bubble> i = bubbles.iterator();

        while (i.hasNext()) {

            Bubble bubble = i.next();
            if (bubble.checkPop() && !bubble.getIsPrisonBubble()) {

                i.remove();
                notifyAllObservers(bubble, 1);
                
                Logger.log("Bubble is popped");
            }
        }

    }

    /**
     * This function checks how to move vertically.
     */
    private void moveVertical() {
        if (input.isMoveUp() && ableToJump) {
            jump();
        }
        if (facingRight) {
            setImage("/BubRight.png");
        } else {
            setImage("/BubLeft.png");
        }
    }

    private void jump() {
        ableToJump = false;
        jumping = true;
        setDy(-Settings.JUMP_SPEED);
    }


    /**
     * This function checks how to move horizontally.
     */
    private void moveHorizontal() {
        if (input.isMoveLeft()) {
            moveLeft();
        } else if (input.isMoveRight()) {
            moveRight();
        } else {
            setDx(0d);
        }
    }

    /**
     * This function handles moving to the right.
     */
    private void moveRight() {
        if (!causesCollisionWall(getX() + speed,
                getX() + getWidth() + speed,
                getY(),
                getY() + getHeight(), levelController)) {
            setDx(speed);
        } else if (causesCollisionWall(getX(), getX() + getWidth(),
                getY(), getY() + getHeight(), levelController)) {
            setDx(speed);
        } else {
            if (!jumping) {
                setDx(0);
            }
        }

        setImage("/BubRight.png");
        facingRight = true;
    }

    /**
     * This function handles moving to the left.
     */
    private void moveLeft() {
        if (!causesCollisionWall(getX() - speed,
                getX() + getWidth() - speed,
                getY(),
                getY() + getHeight(), levelController)) {
            setDx(-speed);
        } else if (causesCollisionWall(getX(), getX() + getWidth(),
                getY(), getY() + getHeight(), levelController)) {
            setDx(-speed);
        } else {
            if (!jumping) {
                setDx(0);
            }
        }

        setImage("/BubLeft.png");
        facingRight = false;
    }

    /**
     * This function checks if the game is over. And if so, loads the game over screen.
     */
    private void checkIfGameOver() {
        if (counter > 50) {
            gameOver = true;
            notifyAllObservers(this, 1);
        } else {
            counter++;
        }
    }

    /**
     * This function checks if it should fire a bubble.
     */
    private void checkFirePrimary() {
        if (input.isFirePrimaryWeapon() && counter > 30) {
            Bubble bubble = new Bubble(getX(), getY(), 0, 0, 0, 0,
                    facingRight, bubblePowerup, levelController);
            bubbles.add(bubble);
            notifyAllObservers(bubble, 2);
            
            counter = 0;
        } else {
            counter++;
        }
    }

    /**
     * This function returns the bubble list.
     *
     * @return the bubbles.
     */
    public ArrayList<Bubble> getBubbles() {
        return bubbles;
    }

    /**
     * This method checks if the character is dead or not.
     *
     * @return isDead when the character is dead.
     */
    public boolean getDead() {
        return isDead;
    }

    /**
     * This method  checks if there is a game over.
     *
     * @return gameOver if the game is over.
     */
    public boolean getGameOver() {
        return gameOver;
    }

    /**
     * This sets if the player is jumping or not.
     * @param jumping if the player is jumping.
     */
    public void setJumping(boolean jumping) {
    	this.jumping = jumping;
    }

    /**
     * This gets if the player is jumping or not.
     * @return true is jumping;
     */
    public boolean getJumping() {
    	return jumping;
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
     * Set the input for a player.
     *
     * @param input The input for a player.
     */
    public void setInput(Input input) {
        this.input = input;
    }
    
    /**
     * This returns the input.
     * @return input the Input.
     */
    public Input getInput() {
        return input;
    }

    /**
     * This is called when the speed powerup is picked up.
     * It doubles the speed for a while.
     */
    public void activateSpeedPowerup() {
        doubleSpeed = true;
        speed = 2 * Settings.PLAYER_SPEED;
    }

    /**
     * This activates the bubble powerup.
     * Bubbles fly horizontally longer.
     */
    public void activateBubblePowerup() {
        bubblePowerup = true;
    }
}