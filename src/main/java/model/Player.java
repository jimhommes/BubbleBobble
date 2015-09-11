package model;

import controller.LevelController;

import java.util.ArrayList;

/**
 * This is the player class. It has a sprite to display.
 */
public class Player extends GravityObject {

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
     * and the firerate of the bubbles.
     */
    private int counter;

    /**
     * This boolean indicates whether the player is dead.
     */
    private boolean isDead;

    /**
     * This boolean indicates wheter the player has died, and has no lifes left.
     */
    private boolean gameOver;

    /**
     * This is the levelController.
     */
    private LevelController levelController;

    /**
     * The constructor that takes all parameters and creates a SpriteBase.
     * @param x The start x coordinate.
     * @param y The start y coordinate.
     * @param r The r.
     * @param dx The dx.
     * @param dy The dy.
     * @param dr The dr.
     * @param speed The speed of the player.
     * @param input The input the player will use.
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

        super("../BubRight.png", x, y, r, dx, dy, dr);

        this.speed = speed;
        this.input = input;
        this.bubbles = new ArrayList<>();
        this.counter = 31;
        this.isDead = false;
        this.gameOver = false;
        this.facingRight = true;
        this.levelController = levelController;

    }

    /**
     * The function that processes the input.
     */
    public void processInput() {

    	if (!isDead) {
    		moveVertical();
            moveHorizontal();
            checkFirePrimary();
        } else {
            checkIfGameOver();
        }

    }

    /**
     * The move function that applies the movement to the player.
     */
    @Override
    public void move() {

        if (!levelController.causesCollision(getX(), getX() + getWidth(), 
        		getY() - calculateGravity(), getY() + getHeight() - calculateGravity())) {
            setY(getY() - calculateGravity());
        }

        super.move();

    }

    /**
     * This method checks if the monster has collides with the character.
     * @param monster is the monster that is being checked for collisions.
     */
    public void checkCollideMonster(final Monster monster) {
    	double monsterX = monster.getX();
    	double monsterMaxX = monsterX + monster.getWidth();
    	double monsterY = monster.getY();
    	double monsterMaxY = monsterY + monster.getHeight();

    	if (((monsterX > getX() && monsterX < getX() + getWidth())
    			|| (monsterMaxX > getX() && monsterMaxX < getX() + getWidth()) 
    			|| (getX() > monsterX && getX() < monsterMaxX) 
                || (getX() + getWidth() > monsterX && getX() + getWidth() < monsterMaxX))
    			&& ((monsterY > getY() && monsterY < getY() + getHeight())
    			|| (monsterMaxY > getY() && monsterMaxX < getY() + getHeight()) 
    			|| (getY() > monsterY && getY() < monsterMaxX) 
                || (getY() + getHeight() > monsterY && getY() + getHeight() < monsterMaxY))) {
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
        counter = 0;
        setImage("/BubbleBobbleLogo.png");
    }

    /**
     * This function checks how to move vertically.
     */
    private void moveVertical() {
        if (input.isMoveUp()) {
            if (!levelController.causesCollision(getX(),
                    getX() + getWidth(),
                    getY() - speed,
                    getY() + getHeight() - speed)) {
                setDy(-speed);
            } else {
                setDy(0);
            }
            if (facingRight) {
                setImage("/BubRight.png");
            } else {
                setImage("/BubLeft.png");
            }
        } else if (input.isMoveDown()) {
            if (!levelController.causesCollision(getX(),
                    getX() + getWidth(),
                    getY() + speed,
                    getY() + getHeight() + speed)) {
                setDy(speed);
            } else {
                setDy(0);
            }
            if (facingRight) {
                setImage("/BubRight.png");
            } else {
                setImage("/BubLeft.png");
            }
        } else {
            setDy(0d);
        }
    }

    /**
     * This function checks how to move horizontally.
     */
    private void moveHorizontal() {
        if (input.isMoveLeft()) {
            if (!levelController.causesCollision(getX() - speed,
                    getX() + getWidth() - speed,
                    getY(),
                    getY() + getHeight())) {
                setDx(-speed);
            } else {
                setDx(0);
            }

            setImage("/BubLeft.png");
            facingRight = false;
        } else if (input.isMoveRight()) {
            if (!levelController.causesCollision(getX() + speed,
                    getX() + getWidth() + speed,
                    getY(),
                    getY() + getHeight())) {
                setDx(speed);
            } else {
                setDx(0);
            }

            setImage("/BubRight.png");
            facingRight = true;
        } else {
            setDx(0d);
        }
    }

    /**
     * This function checks if the game is over. And if so, loads the gamover screen.
     */
    private void checkIfGameOver() {
        if (counter > 50) {
            gameOver = true;
            levelController.gameOver();
        } else {
            counter++;
        }
    }

    /**
     * This function checks if it should fire a bubble.
     */
    private void checkFirePrimary() {
        if (input.isFirePrimaryWeapon() && counter > 30) {
            Bubble bubble = new Bubble(getX(), getY(), 0, 0, 0, 0, facingRight, levelController);
            bubbles.add(bubble);
            levelController.getScreenController().addToSprites(bubble);
            counter = 0;
        } else {
            counter++;
        }
    }

    /**
     * This function returns the bubble list.
     * @return the bubbles.
     */
    public ArrayList<Bubble> getBubbles() {
        return bubbles;
    }

    /**
     * This method checks if the character is dead or not.
     * @return isDead when the character is dead.
     */
    public boolean getDead() {
        return isDead;
    }

    /**
     * This method  checks if there is a game over.
     * @return gameOver if the game is over.
     */
    public boolean getGameOver() {
        return gameOver;
    }

}