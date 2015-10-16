package model;

import controller.LevelController;
import javafx.animation.AnimationTimer;
import model.powerups.Immortality;
import model.powerups.PlayerEnhancement;
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
    private double[] location;

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

        this.speed = speed;
        this.input = input;

        this.levelController = levelController;
        this.lives = lives;
        this.playerNumber = playerNumber;

        this.xStartLocation = 64;
        this.yStartLocation = 64;

        this.powerups = new ArrayList<>();
        this.spriteBase = new SpriteBase("/Bub" + playerNumber + "Left.png", coordinates);
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

        playerMinX = Level.SPRITE_SIZE;
        playerMaxX = Settings.SCENE_WIDTH - Level.SPRITE_SIZE;
        playerMinY = Level.SPRITE_SIZE;
        playerMaxY = Settings.SCENE_HEIGHT - Level.SPRITE_SIZE;

        this.addObserver(levelController);
        this.addObserver(levelController.getScreenController());
        this.timer = createTimer();
        timer.start();

        width = Settings.SPRITE_SIZE;
        height = Settings.SPRITE_SIZE;
        location = spriteBase.getLocation();
    }

    private AnimationTimer createTimer() {
        return new AnimationTimer() {
            @SuppressWarnings("unchecked")
            @Override
            public void handle(long now) {

                    if (!levelController.getLevelControllerMethods().getGamePaused() && !isDead()) {
                        processInput();
                        move();
                        levelController.getCurrLvl().getMonsters().forEach(
                                Player.this::checkCollideMonster
                        );
                    }
                    if (isDead()) {
                        spriteBase.setImage("/Bub" + playerNumber + "Death.png");
                    }

                    setChanged();
                    notifyObservers();
                }

        };
    }

    /**
     * The function that processes the input.
     */
    public void processInput() {
    	getLocation();
        if (!isDead() && !isDelayed) {
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
        setLocation(location);
        checkPowerups();
        
        
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
        if (!wallCollision(location[0],
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

        Double newX = location[0] + location[1];
        Double newY = location[2] + location[3];

        if (!newX.equals(location[0]) || !newY.equals(location[2])) {
            Logger.log(String.format("Player moved from (%f, %f) to (%f, %f)",
                    location[0], location[2], newX, newY));
        }

    }

    /**
     * This function applies gravity.
     */
    private void applyGravity() {
        double x = location[0];
        double y = location[2];
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
     * @return True if collision.
     */
    @SuppressWarnings("unchecked")
    private boolean causesBubbleCollision() {
        ArrayList<Bubble> bubbles = levelController.getBubbles();
        double x = location[0];
        double x1 = x + width;
        double y = location[2];
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
            }
        }
    }

    /**
     * This method is used when the character is killed.
     */
    public void die() {
        this.loseLife();
        this.scorePoints(Settings.POINTS_PLAYER_DIE);
        location[1] = 0;
        location[3] = 0;
        setLocation(location);

        if (this.getLives() == 0) {
            counter = 0;
            setLocation(location);
            spriteBase.setImage("/Bub" + playerNumber + "Death.png");
            setChanged();
            notifyObservers();
            destroy();
        } else {
            isDelayed = true;
            delayRespawn();
        }
        
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

        location[1] = 0;
        location[3] = 0;
        location[0] = xStartLocation;
        location[2] = yStartLocation;
        setLocation(location);
    }

    /**
     * This function checks how to move vertically.
     */
    private void moveVertical() {
        if (input.isMoveUp() && isAbleToJump) {
            jump();
        }
        setImage();
        
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

        if (!wallCollision(x + speed,
                x + width + speed,
                y, y + height, levelController)) {
            location[1] = speed;
        } else if (wallCollision(x, x + width,
                y, y + height, levelController)) {
            location[1] = speed;
        } else {
            if (!isJumping) {
                location[1] = 0;
            }
        }
        isFacingRight = true;
        setImage();
    }

    /**
     * This function handles moving to the left.
     */
    private void moveLeft() {
        double x = location[0];
        double y = location[2];

        if (!wallCollision(x - speed,
                x + width - speed,
                y,
                y + height, levelController)) {
            location[1] = -speed;
        } else if (wallCollision(x, x + width,
                y, y + height, levelController)) {
            location[1] = -speed;
        } else {
            if (!isJumping) {
                location[1] = 0;
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
    private void checkFirePrimary() {
        if (input.isFirePrimaryWeapon() && counter > 30) {
        	Coordinates bubbleCoordinates = 
        			new Coordinates(location[0], location[2], 0, 0, 0, 0);
            Bubble bubble = new Bubble(bubbleCoordinates,
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
    public boolean isDead() {
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
     * This method gets the location from the SpriteBase.
     * @return location
     */
    public double[] getLocation() {
        location = spriteBase.getLocation();
        return spriteBase.getLocation();
    }
    
    /**
     * This method sets the new location in the SpriteBase.
     * @param newLocation the location
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
        	if (!wallCollision(x,
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
    private void setImage() {
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
}