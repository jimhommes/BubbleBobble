package model;

import controller.LevelController;
import utility.Logger;
import utility.Settings;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the player class, that creates are interacts with the player sprite.
 */
public class Player extends GravityObject {

    private static final int TIME_IMMORTAL = 3;
    private final int playerNumber;

    private boolean isJumping;
    private Input input;
    private double speed;
    private ArrayList<Bubble> bubbles;
    private boolean isFacingRight;
    private int counter;
    private boolean isDead;
    private boolean isGameOver;
    private boolean isImmortal;
    private boolean isDelayed;
    private Timer immortalTimer;
    private LevelController levelController;
    private boolean isAbleToJump;
    private boolean isAbleToDoubleJump;

    private double playerMinX;
    private double playerMaxX;
    private double playerMinY;
    private double playerMaxY;

    private boolean doubleSpeed;
    private int doubleSpeedCounter;

    private boolean bubblePowerup;
    private int bubblePowerupCounter;

    private double xStartLocation;
    private double yStartLocation;

    private int score;
    private int lives;

    private SpriteBase spriteBase;
    
    private double width;
    private double height;
    private double[] location;

    /**
     * The constructor of the Player class.
     *
     * @param levelController The levelController.
     * @param coordinates The coordinates of the player.
     * @param speed The speed.
     * @param lives The amount of lives.
     * @param input The input.
     * @param playerNumber The number of the player.
     */
    public Player(LevelController levelController,
                 Coordinates coordinates,
                  double speed,
                  int lives,
                  Input input,
                  int playerNumber) {

        this.speed = speed;
        this.input = input;
        this.bubbles = new ArrayList<>();
        this.counter = 31;
        this.isAbleToJump = false;
        this.isAbleToDoubleJump = false;
        this.isJumping = false;
        this.isDead = false;
        this.isGameOver = false;
        this.isFacingRight = true;
        this.levelController = levelController;
        this.lives = lives;
        this.score = 0;
        this.playerNumber = playerNumber;

        playerMinX = Level.SPRITE_SIZE;
        playerMaxX = Settings.SCENE_WIDTH - Level.SPRITE_SIZE;
        playerMinY = Level.SPRITE_SIZE;
        playerMaxY = Settings.SCENE_HEIGHT - Level.SPRITE_SIZE;

        xStartLocation = coordinates.getX();
        yStartLocation = coordinates.getY();
        
        this.spriteBase = new SpriteBase("/Bub" + playerNumber + "Left.png", coordinates);
        this.addObserver(levelController);
        this.addObserver(levelController.getScreenController());
        
        width = Settings.SPRITE_SIZE;
        height = Settings.SPRITE_SIZE;
        location = spriteBase.getLocation();
    }

    /**
     * The function that processes the input.
     */
    public void processInput() {
    	getLocation();
        if (!isDead && !isDelayed) {
            if (isJumping && location[3] <= 0) {
                location[3] = location[3] + 0.6;
            } else if (isJumping && location[3] > 0) {
                location[3] = location[3] + 0.6;
                setJumping(false);
            } else {
                location[3] = 0;
            }

            moveVertical();
            moveHorizontal();
            checkFirePrimary();
            
        } else {
            if (!isDelayed) {
                checkIfGameOver();
            }
        }
        
        applyGravity();
        checkBounds();
        //spriteBase.checkBounds(playerMinX, playerMaxX, playerMinY, playerMaxY, levelController);
        setLocation(location);
        checkPowerups();
        
        
    }

    /**
     * Check for collision combined with jumping.
     *
     * @param jumping    The variable whether a GravityObject is jumping.
     * @param ableToJump The variable whether a GravityObject is able to jump.
     * @return The ableToJump variable.
     */
    public boolean moveCollisionChecker(boolean jumping, boolean ableToJump) {
        if (!spriteBase.causesCollisionWall(location[0],
                location[0] + width,
                location[2] - calculateGravity(),
                location[2] + height - calculateGravity(), levelController)) {
            if (!jumping) {
                location[3] = location[3] - calculateGravity();
            }
            setAbleToJump(false);
        } else {
            if (!jumping) {
                setAbleToJump(true);
            }
        }
        return ableToJump;
    }

    private void checkPowerups() {
        if (doubleSpeed) {
            doubleSpeedCounter++;
            if (doubleSpeedCounter >= Settings.PLAYER_DOUBLESPEED_DURATION) {
                setDoubleSpeed(false);
                setSpeed(Settings.PLAYER_SPEED);
                setDoubleSpeedCounter(0);
            }
        } else {
            setDoubleSpeedCounter(0);
        }

        if (bubblePowerup) {
            bubblePowerupCounter++;
            if (bubblePowerupCounter >= Settings.BUBBLE_POWERUP_DURATION) {
                setBubblePowerup(false);
                setBubblePowerupCounter(0);
            }
        } else {
            setBubblePowerupCounter(0);
        }
    }

    /**
     * The move function that applies the movement to the player.
     */
    public void move() {
        applyGravity();
        spriteBase.move();

        Double newX = location[0] + location[1];
        Double newY = location[2] + location[3];

        if (!newX.equals(location[0]) || !newY.equals(location[2])) {
            Logger.log(String.format("Player moved from (%f, %f) to (%f, %f)",
                    location[0], location[2], newX, newY));
        }

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function applies gravity.
     */
    private void applyGravity() {
        double x = location[0];
        double y = location[2];
        if (!spriteBase.causesCollisionWall(x, x + width,
                y - calculateGravity(), y + height - calculateGravity(),
                levelController)
                || spriteBase.causesCollisionWall(x, x + width,
                y, y + height, levelController)) {
            if (!isJumping) {
                if (isAbleToDoubleJump
                        && causesBubbleCollision(x, x + width,
                        y - calculateGravity(),
                        y + height - calculateGravity())) {
                    setAbleToJump(true);
                    setAbleToDoubleJump(false);
                } else if (isAbleToDoubleJump) {
                    setAbleToJump(false);
                }
                location[2] = y - calculateGravity();
            } else {
                setAbleToJump(false);
            }
        } else {
            if (!isJumping) {
                setAbleToJump(true);
                setAbleToDoubleJump(true);
            }
        }
    }

    /**
     * This function checks if the player collides with a bubble.
     *
     * @param x  Minimal x.
     * @param x1 Maximal x.
     * @param y  Minimal y.
     * @param y2 Maximal y.
     * @return True if collision.
     */
    @SuppressWarnings("unchecked")
    private boolean causesBubbleCollision(double x, double x1, double y, double y2) {
        ArrayList<Bubble> bubbles = new ArrayList<>();
        levelController.getPlayers().forEach(player -> {
            Player p = player;
            bubbles.addAll(p.getBubbles());
        });


        if (bubbles.size() == 0) {
            return false;
        } else {
            boolean res = false;
            for (Bubble bubble : bubbles) {
                if (bubble.getSpriteBase().causesCollision(x, x1, y, y2)
                        && !bubble.isAbleToCatch()) {
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
        double x = location[0];
        double y = location[2];
        
        if (monster.getSpriteBase().causesCollision(x, x + width,
                y, y + height)
                && !isDelayed) {
        	
            if (!monster.isCaughtByBubble()) {
                if (!isImmortal) {
                    this.die();
                }
            } else {
                monster.die(this);
                monster.getPrisonBubble().setIsPopped(true);
                bubbles.remove(monster.getPrisonBubble());
            }
        }
    }

    /**
     * This method is used when the character is killed.
     */
    public void die() {
    	location[1] = 0;
        location[3] = 0;
        setLocation(location);
        if (this.getLives() <= 1 && !this.isDead) {
            this.isDead = true;
            counter = 0;
            setLocation(location);
            spriteBase.setImage("/Bub" + playerNumber + "Death.png");
        } else {
            isDelayed = true;
            spriteBase.setImage("/Bub" + playerNumber + "Death.png");
            this.loseLife();
            this.scorePoints(Settings.POINTS_PLAYER_DIE);
            delayRespawn();
        }
        
    }

    private void delayRespawn() {
        Timer delayTimer = new Timer();
        delayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isDelayed = false;
                isImmortal = true;

                location[1] = 0;
                location[3] = 0;
                location[0] = xStartLocation;
                location[2] = yStartLocation;
                immortalTimer = new Timer();
                immortalTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isImmortal = false;
                        immortalTimer.cancel();
                    }
                }, 1000 * TIME_IMMORTAL);
            }
        }, 1000);

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function checks how to move vertically.
     */
    private void moveVertical() {
        if (input.isMoveUp() && isAbleToJump) {
            jump();
        }
        if (isFacingRight) {
            if (isImmortal) {
                spriteBase.setImage("/Bub" + playerNumber + "RightRed.png");
            } else {
                spriteBase.setImage("/Bub" + playerNumber + "Right.png");
            }
        } else {
            if (isImmortal) {
                spriteBase.setImage("/Bub" + playerNumber + "LeftRed.png");
            } else {
                spriteBase.setImage("/Bub" + playerNumber + "Left.png");
            }
        }
    }

    private void jump() {
        setAbleToJump(false);
        setJumping(true);
        location[3] = -Settings.JUMP_SPEED;
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
            location[1] = 0d;
        }
    }

    /**
     * This function handles moving to the right.
     */
    private void moveRight() {
        double x = location[0];
        double y = location[2];

        if (!spriteBase.causesCollisionWall(x + speed,
                x + width + speed,
                y, y + height, levelController)) {
            location[1] = speed;
        } else if (spriteBase.causesCollisionWall(x, x + width,
                y, y + height, levelController)) {
            location[1] = speed;
        } else {
            if (!isJumping) {
                location[1] = 0;
            }
        }

        if (isImmortal) {
            spriteBase.setImage("/Bub" + playerNumber + "RightRed.png");
        } else {
            spriteBase.setImage("/Bub" + playerNumber + "Right.png");
        }
        isFacingRight = true;
    }

    /**
     * This function handles moving to the left.
     */
    private void moveLeft() {
        double x = location[0];
        double y = location[2];

        if (!spriteBase.causesCollisionWall(x - speed,
                x + width - speed,
                y,
                y + height, levelController)) {
            location[1] = -speed;
        } else if (spriteBase.causesCollisionWall(x, x + width,
                y, y + height, levelController)) {
            location[1] = -speed;
        } else {
            if (!isJumping) {
                location[1] = 0;
            }
        }

        if (isImmortal) {
            spriteBase.setImage("/Bub" + playerNumber + "LeftRed.png");
        } else {
            spriteBase.setImage("/Bub" + playerNumber + "Left.png");
        }
        setFacingRight(false);
    }

    /**
     * This function checks if the game is over. And if so, loads the game over screen.
     */
    private void checkIfGameOver() {
        if (counter > 50) {
            setGameOver(true);
        } else {
            counter++;
        }
    }

    /**
     * This function checks if it should fire a bubble.
     */
    private void checkFirePrimary() {
        if (input.isFirePrimaryWeapon() && counter > 30) {
        	Coordinates bubbleCoordinates = 
        			new Coordinates(spriteBase.getX(), spriteBase.getY(), 0, 0, 0, 0);
            Bubble bubble = new Bubble(bubbleCoordinates,
                    isFacingRight, bubblePowerup, levelController);
            bubbles.add(bubble);

            this.setChanged();
            this.notifyObservers();

            counter = 0;
        } else {
            counter++;
        }
    }

    /**
     * Add/subtract points to/from the player's score.
     *
     * @param points the amount of scored points.
     */
    public void scorePoints(int points) {
        this.setScore(this.getScore() + points);

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Reduce the player's lives by 1.
     */
    private void loseLife() {
        this.setLives(getLives() - 1);
    }

    /**
     * Add 1 to the player's lives.
     */
    public void addLife() {
        this.setLives(getLives() + 1);
    }

    /**
     * Get the number of lives.
     *
     * @return the number of lives.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Set the number of lives.
     *
     * @param lives the number of lives.
     */
    public void setLives(int lives) {
        this.lives = lives;

        this.setChanged();
        this.notifyObservers();
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
        setBubblePowerup(true);
    }

    /**
     * This function returns whether the player is jumping.
     *
     * @return True if jumping.
     */
    public boolean isJumping() {
        return isJumping;
    }

    /**
     * This function sets whether the player is jumping.
     *
     * @param jumping True if jumping.
     */
    public void setJumping(boolean jumping) {
        isJumping = jumping;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function returns the input.
     *
     * @return The input.
     */
    public Input getInput() {
        return input;
    }

    /**
     * This function sets the input.
     *
     * @param input The input.
     */
    public void setInput(Input input) {
        this.input = input;

        this.setChanged();
        this.notifyObservers();
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

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function returns the bubbles.
     *
     * @return The bubbles.
     */
    public ArrayList<Bubble> getBubbles() {
        return bubbles;
    }

    /**
     * This function sets the bubbles.
     *
     * @param bubbles The bubbles.
     */
    public void setBubbles(ArrayList<Bubble> bubbles) {
        this.bubbles = bubbles;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function sets whether the player is facing right.
     *
     * @param facingRight True if facing right.
     */
    public void setFacingRight(boolean facingRight) {
        isFacingRight = facingRight;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function returns whether the player is dead.
     *
     * @return True if dead.
     */
    public boolean isDead() {

        if (isDead) {
            this.deleteObservers();
        }

        return isDead;
    }

    /**
     * This function returns whether the player is game over.
     * AKA no lives left.
     *
     * @return True if game over.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * This function sets whether the player is game over.
     *
     * @param gameOver True if gameover.
     */
    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function returns the levelcontroller.
     *
     * @return The levelcontroller.
     */
    public LevelController getLevelController() {
        return levelController;
    }

    /**
     * This function sets the levelcontroller.
     *
     * @param levelController The levelcontroller.
     */
    public void setLevelController(LevelController levelController) {
        this.levelController = levelController;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function sets if the player is able to jump.
     *
     * @param ableToJump True if able to jump.
     */
    public void setAbleToJump(boolean ableToJump) {
        isAbleToJump = ableToJump;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function sets if the player is able to double jump.
     *
     * @param ableToDoubleJump True if able to double jump.
     */
    public void setAbleToDoubleJump(boolean ableToDoubleJump) {
        isAbleToDoubleJump = ableToDoubleJump;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function sets the double speed.
     *
     * @param doubleSpeed True if double speed.
     */
    public void setDoubleSpeed(boolean doubleSpeed) {
        this.doubleSpeed = doubleSpeed;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function sets the double speed counter.
     *
     * @param doubleSpeedCounter The double speed counter.
     */
    public void setDoubleSpeedCounter(int doubleSpeedCounter) {
        this.doubleSpeedCounter = doubleSpeedCounter;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function sets the bubble powerup.
     *
     * @param bubblePowerup True if bubble powerup.
     */
    public void setBubblePowerup(boolean bubblePowerup) {
        this.bubblePowerup = bubblePowerup;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function sets the bubble powerup counter.
     *
     * @param bubblePowerupCounter The powerup counter.
     */
    public void setBubblePowerupCounter(int bubblePowerupCounter) {
        this.bubblePowerupCounter = bubblePowerupCounter;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function returns the score.
     *
     * @return The score.
     */
    public int getScore() {
        return score;
    }

    /**
     * This function sets the score.
     *
     * @param score The score.
     */
    public void setScore(int score) {
        this.score = score;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * This function returns the sprite.
     *
     * @return The sprite.
     */
    public SpriteBase getSpriteBase() {
        return spriteBase;
    }

    /**
     * This function checks whether the bubbles are popped.
     */
    public void checkBubbles() {
        ArrayList<Bubble> nBubbles = new ArrayList<>();
        bubbles.forEach(bubble -> {
            if (!bubble.getIsPopped()) {
                nBubbles.add(bubble);
            }
        });
        bubbles = nBubbles;
    }

    /**
     * This function returns the player number.
     * @return The player number.
     */
    public int getPlayerNumber() {
        return playerNumber;
    }
    
    /**
     * This method gets the location from the SpriteBase.
     */
    public double[] getLocation() {
    	location = spriteBase.getLocation();
    	return location;
    }
    
    /**
     * This method sets the new location in the SpriteBase.
     */
    public void setLocation(double[] newLocation) {
    	spriteBase.setLocation(newLocation);
    }
    
    /**
     * This function returns the player if it is out of bounds.
     */
    public void checkBounds() {
    	double x = location[0];
    	double y = location[2];
        if (x < playerMinX) {
            location[0] = playerMinX;
        } else if (x + width > playerMaxX) {
            location[0] = playerMaxX - width;
        }

        if (y < playerMinY) {
        	if (!spriteBase.causesCollisionWall(x,
                    x + width,
                    y,
                    y + height, levelController)) {
        		location[2] = playerMaxY - height;
        	} else {
        		location[2] = playerMinY;
        	}	
        } else if (y + height > playerMaxY) {
            location[2] = playerMinY;
        }
    }
}