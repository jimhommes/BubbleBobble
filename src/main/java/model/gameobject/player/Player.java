package model.gameobject.player;

import controller.HighscoreEntryController;
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
    private boolean isGameOver;
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
        this.isGameOver = false;
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
            @SuppressWarnings("unchecked")
            @Override
            public void handle(long now) {

                    if (!levelController.getLevelControllerMethods().getGamePaused() && !noLivesLeft()) {
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
     * The function that processes the input.
     */
    public void processInput() {
        if (!noLivesLeft() && !isDelayed) {
            if (isJumping && spriteBase.getDy() <= 0) {
                spriteBase.setDy(spriteBase.getDy() + 0.6);
            } else if (isJumping && spriteBase.getDy() > 0) {
                spriteBase.setDy(spriteBase.getDy() + 0.6);
                setJumping(false);
            } else {
                spriteBase.setDy(0);
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
        if (!wallCollision(spriteBase.getX(),
                spriteBase.getX() + width,
                spriteBase.getY() - calculateGravity(),
                spriteBase.getY() + height - calculateGravity(), levelController)) {
            if (!jumping) {
                spriteBase.setDy(spriteBase.getDy() - calculateGravity());
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

        Double newX = spriteBase.getX() + spriteBase.getDx();
        Double newY = spriteBase.getY() + spriteBase.getDy();

        if (!newX.equals(spriteBase.getX()) || !newY.equals(spriteBase.getY())) {
            Logger.log(String.format("Player moved from (%f, %f) to (%f, %f)",
                    spriteBase.getX(), spriteBase.getY(), newX, newY));
        }

    }

    /**
     * This function applies gravity.
     */
    public void applyGravity() {
        double x = spriteBase.getX();
        double y = spriteBase.getY();
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
                spriteBase.setDy(-calculateGravity());
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
    @SuppressWarnings("unchecked")
    public boolean causesBubbleCollision() {
        ArrayList<Bubble> bubbles = levelController.getBubbles();
        double x = spriteBase.getX();
        double x1 = x + width;
        double y = spriteBase.getY();
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
        double x = spriteBase.getX();
        double y = spriteBase.getY();
        
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
    	spriteBase.setDx(0);
    	spriteBase.setDy(0);

    	if (this.getLives() == 0) {
    		counter = 0;
    		setChanged();
    		notifyObservers();
    		destroy();
            addHighscore();
    	} else {
    		isDelayed = true;
    		delayRespawn();
    	}
    }

    /**
     * Add the final score to the highscoresList.
     */
    public void addHighscore() {
        ArrayList<HighscoreEntryController> highscores = Settings.getHighscores();
        highscores.add(new HighscoreEntryController(Settings.getName(playerNumber - 1),
                Integer.toString(this.getScore())));
        highscores.sort((HighscoreEntryController o1,
                         HighscoreEntryController o2)->o2.getScore() - o1.getScore());
        while (highscores.size() > 10) {
            highscores.remove(10);
        }
        Settings.setHighscores(highscores);
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

        spriteBase.setDx(0);
        spriteBase.setDy(0);
        spriteBase.setX(xStartLocation);
        spriteBase.setY(yStartLocation);
        isDead = false;
    }

    /**
     * This function checks how to move vertically.
     */
    public void moveVertical() {
        if (input.isMoveUp() && isAbleToJump) {
            jump();
        }
        setImage();
        
    }

    private void jump() {
        setAbleToJump(false);
        setJumping(true);
        spriteBase.setDy(-Settings.JUMP_SPEED);
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
            spriteBase.setDx(0d);
        }
    }

    /**
     * This function handles moving to the right.
     */
    private void moveRight() {
        double x = spriteBase.getX();
        double y = spriteBase.getY();

        if (!wallCollision(x + speed,
                x + width + speed,
                y, y + height, levelController)) {
            spriteBase.setDx(speed);
        } else if (wallCollision(x, x + width,
                y, y + height, levelController)) {
            spriteBase.setDx(speed);
        } else {
            if (!isJumping) {
                spriteBase.setDx(0);
            }
        }
        isFacingRight = true;
        setImage();
    }

    /**
     * This function handles moving to the left.
     */
    private void moveLeft() {
        double x = spriteBase.getX();
        double y = spriteBase.getY();

        if (!wallCollision(x - speed,
                x + width - speed,
                y,
                y + height, levelController)) {
            spriteBase.setDx(-speed);
        } else if (wallCollision(x, x + width,
                y, y + height, levelController)) {
            spriteBase.setDx(-speed);
        } else {
            if (!isJumping) {
                spriteBase.setDx(0);
            }
        }
        setFacingRight(false);
        setImage();
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
    public void checkFirePrimary() {
        if (input.isFirePrimaryWeapon() && counter > 30) {
        	Coordinates bubbleCoordinates = 
        			new Coordinates(spriteBase.getX(), spriteBase.getY(), 0, 0, 0, 0);
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
     * This function returns the player if it is out of bounds.
     */
    public void checkBounds() {
    	double x = spriteBase.getX();
    	double y = spriteBase.getY();
        if (x < playerMinX) {
            spriteBase.setX(playerMinX);
        } else if (x + width > playerMaxX) {
            spriteBase.setX(playerMaxX - width);
        }

        if (y < playerMinY) {
        	if (!wallCollision(x,
                    x + width,
                    y,
                    y + height, levelController)) {
        		spriteBase.setY(playerMaxY - height);
        	} else {
        		spriteBase.setY(playerMinY);
        	}	
        } else if (y + height > playerMaxY) {
            spriteBase.setY(playerMinY);
        }
    }

    /**
     * Set the player immortal.
     * @param immortal the immortality.
     */
    public void setImmortal(boolean immortal) {
        isImmortal = immortal;
    }

    /**
     * This function forces the player to die entirely.
     */
    public void destroy() {
        this.deleteObservers();
        timer.stop();
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
     * This functions tells if the player is delayed.
     * @return delayed.
     */
    public boolean isDelayed() {
      return isDelayed;
    }
}