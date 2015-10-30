package model.gameobject.player;

import controller.LevelController;
import javafx.animation.AnimationTimer;
import model.gameobject.GravityObject;
import model.gameobject.bubble.Bubble;
import model.gameobject.bubble.PlayerBubble;
import model.gameobject.enemy.Monster;
import model.support.Coordinates;
import model.support.Input;
import model.support.SpriteBase;
import model.gameobject.powerup.Immortality;
import model.gameobject.powerup.PlayerEnhancement;
import utility.Logger;
import utility.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

/**
 * This is the player class, that creates are interacts with the player sprite.
 */
public class Player extends GravityObject {

    private final int playerNumber;

    private boolean isJumping;
    private Input input;
    private double speed;
    private boolean isFacingRight;
    private int counter;
    private boolean isDelayed;
    private LevelController levelController;
    private boolean isAbleToJump;
    private boolean isAbleToDoubleJump;
    private boolean isDead;

    private double playerMinX;
    private double playerMaxX;
    private double playerMinY;
    private double playerMaxY;

    private boolean bubblePowerup;
    private boolean isImmortal;

    private double xStartLocation;
    private double yStartLocation;

    private int score;
    private int lives;

    private SpriteBase spriteBase;
    
    private double width;
    private double height;

    private List<PlayerEnhancement> powerups;

    private AnimationTimer timer;

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
    public Player(LevelController levelController, Coordinates coordinates, double speed,
                  int lives, Input input, int playerNumber) {
        this.spriteBase = new SpriteBase("Bub" + playerNumber + "Left.png", coordinates);
        this.speed = speed;
        this.input = input;

        this.levelController = levelController;
        this.lives = lives;
        this.playerNumber = playerNumber;

        this.xStartLocation = Settings.SPRITE_SIZE;
        this.yStartLocation = Settings.SPRITE_SIZE;

        this.powerups = new ArrayList<>();
        this.isDead = false;
        this.setUp();
    }

    private void setUp() {
        this.score = 0;
        this.counter = 31;
        this.isAbleToJump = false;
        this.isAbleToDoubleJump = false;
        this.isJumping = false;
        this.isFacingRight = true;

        playerMinX = Settings.SPRITE_SIZE / 2;
        playerMaxX = Settings.SCENE_WIDTH - Settings.SPRITE_SIZE / 2;
        playerMinY = Settings.SPRITE_SIZE / 2;
        playerMaxY = Settings.SCENE_HEIGHT - Settings.SPRITE_SIZE / 2;

        this.addObserver(levelController);
        this.addObserver(levelController.getScreenController());
        this.timer = createTimer();
        timer.start();

        width = Settings.SPRITE_SIZE;
        height = Settings.SPRITE_SIZE;
    }

    /**
     * This function returns the timer of the player.
     * @return Timer of the player.
     */
    public AnimationTimer createTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {

                    if (!levelController.getLevelControllerMethods().getGamePaused()
                            && !noLivesLeft()) {
                        processInput();
                        move();
                        levelController.getCurrLvl().getMonsters().forEach(
                                Player.this::checkCollideMonster
                        );
                    }

                    setImage();
                    setChanged();
                    notifyObservers();
                }

        };
    }

    /**
     * This method sets the image.
     */
    public void setImage() {
        if (!isDead) {
            if (isFacingRight) {
                if (isImmortal) {
                    spriteBase.setImage("Bub" + playerNumber + "RightImmortal.png");
                } else {
                    spriteBase.setImage("Bub" + playerNumber + "Right.png");
                }
            } else {
                if (isImmortal) {
                    spriteBase.setImage("Bub" + playerNumber + "LeftImmortal.png");
                } else {
                    spriteBase.setImage("Bub" + playerNumber + "Left.png");
                }
            }
        } else {
            if (isFacingRight) {
                spriteBase.setImage("Bub" + playerNumber + "RightDeath.png");
            } else {
                spriteBase.setImage("Bub" + playerNumber + "LeftDeath.png");
            }
        }
    }

    /**
     * The function that processes the input.
     */
    public void processInput() {
        if (!noLivesLeft() && !isDelayed) {
            if (isJumping && spriteBase.getDyCoordinate() <= 0) {
                spriteBase.setDyCoordinate(spriteBase.getDyCoordinate() + 0.6);
            } else if (isJumping && spriteBase.getDyCoordinate() > 0) {
                spriteBase.setDyCoordinate(spriteBase.getDyCoordinate() + 0.6);
                setJumping(false);
            } else {
                spriteBase.setDyCoordinate(0);
            }

            moveVertical();
            moveHorizontal();
            checkFirePrimary();
            
        }
        
        applyGravity();
        checkBounds();
        checkPowerups();
    }

    /**
     * This function is only for testing purposes.
     */
    public void forceUpdate() {
        setChanged();
        notifyObservers();
    }

    /**
     * This function forces the player to die entirely.
     */
    public void destroy() {
        this.deleteObservers();
        timer.stop();
    }

    /**
     * This method check if there is a collision between SpriteBase and Wall.
     * @param minX minimal x coordinate.
     * @param maxX maximal x coordinate.
     * @param minY minimal y coordinate.
     * @param maxY maximal y coordinate.
     * @param levelController the LevelController.
     * @return true if there is a collision.
     */
    private boolean wallCollision(double minX, double maxX, double minY,
                                  double maxY, LevelController levelController) {
        return spriteBase.causesCollisionWall(minX, maxX, minY, maxY, levelController);
    }

    /**
     * This function returns the player if it is out of bounds.
     */
    public void checkBounds() {
        double x = spriteBase.getXCoordinate();
        double y = spriteBase.getYCoordinate();
        if (x < playerMinX) {
            spriteBase.setXCoordinate(playerMinX);
        } else if (x + width > playerMaxX) {
            spriteBase.setXCoordinate(playerMaxX - width);
        }

        if (y < playerMinY) {
            if (!wallCollision(x,
                    x + width,
                    y,
                    y + height, levelController)) {
                spriteBase.setYCoordinate(playerMaxY - height);
            } else {
                spriteBase.setYCoordinate(playerMinY);
            }
        } else if (y + height > playerMaxY) {
            spriteBase.setYCoordinate(playerMinY);
        }
    }

    /**
     * Interface to create a PlayerEnhancement from a labda.
     */
    interface PlayerEnhancementCreator {
        /**
         * Create the PlayerEnhancement.
         * @param p the subject
         * @return the PlayerEnhancement
         */
        PlayerEnhancement create(Player p);
    }

    /**
     * Add an active powerup to the player.
     * @param p PlayerEnhancementCreator interface
     */
    public void addPowerup(PlayerEnhancementCreator p) {
        this.powerups.add(p.create(this));
    }

    /**
     * Check for collision combined with jumping.
     *
     * @param jumping    The variable whether a GravityObject is jumping.
     * @param ableToJump The variable whether a GravityObject is able to jump.
     * @return The ableToJump variable.
     */
    public boolean moveCollisionChecker(boolean jumping, boolean ableToJump) {
        if (!wallCollision(spriteBase.getXCoordinate(),
                spriteBase.getXCoordinate() + width,
                spriteBase.getYCoordinate() - calculateGravity(),
                spriteBase.getYCoordinate() + height - calculateGravity(), levelController)) {
            if (!jumping) {
                spriteBase.setDyCoordinate(spriteBase.getDyCoordinate() - calculateGravity());
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
        this.powerups = this.powerups.stream()
                .filter(PlayerEnhancement::check)
                .collect(Collectors.toList());
    }

    /**
     * The move function that applies the movement to the player.
     */
    public void move() {
        applyGravity();
        spriteBase.move();

        Double newX = spriteBase.getXCoordinate() + spriteBase.getDxCoordinate();
        Double newY = spriteBase.getYCoordinate() + spriteBase.getDyCoordinate();

        if (!newX.equals(spriteBase.getXCoordinate())
                || !newY.equals(spriteBase.getYCoordinate())) {
            Logger.log(String.format("Player moved from (%f, %f) to (%f, %f)",
                    spriteBase.getXCoordinate(), spriteBase.getYCoordinate(), newX, newY));
        }

    }

    /**
     * This function applies gravity.
     */
    public void applyGravity() {
        double x = spriteBase.getXCoordinate();
        double y = spriteBase.getYCoordinate();
        if (!wallCollision(x, x + width,
                y - calculateGravity(), y + height - calculateGravity(),
                levelController)
                || wallCollision(x, x + width,
                y, y + height, levelController)) {
            if (!isJumping) {
                if (isAbleToDoubleJump
                        && causesBubbleCollision()) {
                    setAbleToJump(true);
                    setAbleToDoubleJump(false);
                } else if (isAbleToDoubleJump) {
                    setAbleToJump(false);
                }
                spriteBase.setDyCoordinate(-calculateGravity());
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
     * @return True if collision.
     */
    public boolean causesBubbleCollision() {
        ArrayList<Bubble> bubbles = levelController.getBubbles();
        double x = spriteBase.getXCoordinate();
        double x1 = x + width;
        double y = spriteBase.getYCoordinate();
        double y2 = y + height;

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
        double x = spriteBase.getXCoordinate();
        double y = spriteBase.getYCoordinate();
        
        if (monster.getSpriteBase().causesCollision(x, x + width,
                y, y + height)
                && !isDelayed) {
        	
            if (!monster.isCaughtByBubble()) {
                if (!isImmortal) {
                    this.die();
                }
            } else {
                monster.die(this);
            }
        }
    }

    /**
     * This method is used when the character is killed.
     */
    public void die() {
        isDead = true;
    	this.loseLife();
    	this.scorePoints(Settings.POINTS_PLAYER_DIE);
    	spriteBase.setDxCoordinate(0);
    	spriteBase.setDyCoordinate(0);

    	if (this.getLives() == 0) {
    		counter = 0;
    		setChanged();
    		notifyObservers();
    		destroy();
    	} else {
    		isDelayed = true;
    		delayRespawn();
    	}
    }

    /**
     * Add the final score to the highscoresList.
     */
    public void addHighscore() {
        Settings.setHighscores(playerNumber, this.getScore());
    }

    private void delayRespawn() {
        Timer delayTimer = new Timer();
        delayTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                respawn();

            }
        }, 1000);
    }

    private void respawn() {
        isDelayed = false;
        this.addPowerup(Immortality::new);

        spriteBase.setDxCoordinate(0);
        spriteBase.setDyCoordinate(0);
        spriteBase.setXCoordinate(xStartLocation);
        spriteBase.setYCoordinate(yStartLocation);
        isDead = false;
    }

    /**
     * This function checks how to move vertically.
     */
    public void moveVertical() {
        if (input.isMoveUp() && isAbleToJump) {
            jump();
        }
    }

    private void jump() {
        setAbleToJump(false);
        setJumping(true);
        spriteBase.setDyCoordinate(-Settings.JUMP_SPEED);
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
            spriteBase.setDxCoordinate(0d);
        }
    }

    /**
     * This function handles moving to the right.
     */
    private void moveRight() {
        double x = spriteBase.getXCoordinate();
        double y = spriteBase.getYCoordinate();

        if (!wallCollision(x + speed,
                x + width + speed,
                y, y + height, levelController)) {
            spriteBase.setDxCoordinate(speed);
        } else if (wallCollision(x, x + width,
                y, y + height, levelController)) {
            spriteBase.setDxCoordinate(speed);
        } else {
            if (!isJumping) {
                spriteBase.setDxCoordinate(0);
            }
        }
        isFacingRight = true;
    }

    /**
     * This function handles moving to the left.
     */
    private void moveLeft() {
        double x = spriteBase.getXCoordinate();
        double y = spriteBase.getYCoordinate();

        if (!wallCollision(x - speed,
                x + width - speed,
                y,
                y + height, levelController)) {
            spriteBase.setDxCoordinate(-speed);
        } else if (wallCollision(x, x + width,
                y, y + height, levelController)) {
            spriteBase.setDxCoordinate(-speed);
        } else {
            if (!isJumping) {
                spriteBase.setDxCoordinate(0);
            }
        }
        setFacingRight(false);
    }

    /**
     * This function checks if it should fire a bubble.
     */
    public void checkFirePrimary() {
        if (input.isFirePrimaryWeapon() && counter > 30) {
        	Coordinates bubbleCoordinates = 
        			new Coordinates(spriteBase.getXCoordinate(),
                            spriteBase.getYCoordinate(), 0, 0, 0, 0);
            PlayerBubble bubble = new PlayerBubble(bubbleCoordinates,
                    isFacingRight, bubblePowerup, levelController);
            levelController.addBubble(bubble);

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
    }

    /**
     * Multiply the speed of the player by a factor.
     * @param factor the factor.
     */
    public void factorSpeed(double factor) {
        this.setSpeed(factor * this.getSpeed());
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
     * This function sets whether the player is facing right.
     *
     * @param facingRight True if facing right.
     */
    public void setFacingRight(boolean facingRight) {
        isFacingRight = facingRight;
    }

    /**
     * This function returns whether the player is dead.
     *
     * @return True if dead.
     */
    public boolean noLivesLeft() {
        return getLives() == 0;
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
    }

    /**
     * This function sets if the player is able to jump.
     *
     * @param ableToJump True if able to jump.
     */
    public void setAbleToJump(boolean ableToJump) {
        isAbleToJump = ableToJump;
    }

    /**
     * This function sets if the player is able to double jump.
     *
     * @param ableToDoubleJump True if able to double jump.
     */
    public void setAbleToDoubleJump(boolean ableToDoubleJump) {
        isAbleToDoubleJump = ableToDoubleJump;
    }

    /**
     * This function sets the bubble powerup.
     *
     * @param bubblePowerup True if bubble powerup.
     */
    public void setBubblePowerup(boolean bubblePowerup) {
        this.bubblePowerup = bubblePowerup;
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
     * This function returns the player number.
     * @return The player number.
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Set the player immortal.
     * @param immortal the immortality.
     */
    public void setImmortal(boolean immortal) {
        isImmortal = immortal;
    }

    /**
     * This function sets the spritebase.
     * @param spriteBase The spritebase.
     */
    public void setSpriteBase(SpriteBase spriteBase) {
        this.spriteBase = spriteBase;
    }

    /**
     * This function sets if the player is jumping.
     * @param isJumping True if jumping.
     */
    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    /**
     * This function returns whether the player is jumping.
     * @return True if jumping.
     */
    public boolean getIsJumping() {
        return isJumping;
    }

    /**
     * This function returns the powerups.
     * @return The powerups.
     */
    public List<PlayerEnhancement> getPowerups() {
        return powerups;
    }

    /**
     * This function returns the abletojump.
     * @return True if able to jump.
     */
    public boolean getAbleToJump() {
        return isAbleToJump;
    }

    /**
     * This function returns true if the player has a delayed respawn.
     * @return True if delayed respawn.
     */
    public boolean getIsDelayed() {
        return isDelayed;
    }

    /**
     * This function returns true if the player is able to double jump.
     * @return True if able to double jump.
     */
    public boolean getAbleToDoubleJump() {
        return isAbleToDoubleJump;
    }

    /**
     * This function sets the counter.
     * This should be used for testing purposes only.
     * @param counter The counter.
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * This function returns the counter.
     * @return The counter.
     */
    public int getCounter() {
        return counter;
    }

    /**
     * This function returns if it has the bubble powerup.
     * @return True if bubblepowerup.
     */
    public boolean getBubblePowerup() {
        return bubblePowerup;
    }

}