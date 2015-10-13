package model;

import controller.LevelController;
import utility.Logger;
import utility.Settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the player class. It has a sprite to display.
 */
public class Player extends GravityObject {

    private static final int TIME_IMMORTAL = 3;

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
    private Timer delayTimer;
    private LevelController levelController;
    private boolean isAbleToJump;
    private boolean isAbleToDoubleJump;

    private double xLocation;
    private double yLocation;
    private double rotation;
    private double dX;
    private double dY;
    private double dR;

    private double playerMinX;
    private double playerMaxX;
    private double playerMinY;
    private double playerMaxY;

    private boolean doubleSpeed;
    private int doubleSpeedCounter;
    private int durationDoubleSpeed = 200;

    private boolean bubblePowerup;
    private int bubblePowerupCounter;
    private int durationBubblePowerup = 400;

    private double xStartLocation;
    private double yStartLocation;

    private int score;
    private int lives;

    private SpriteBase spriteBase;

    public Player(LevelController levelController,
                  double x,
                  double y,
                  double r,
                  double dx,
                  double dy,
                  double dr,
                  double speed,
                  int lives,
                  Input input) {

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


        playerMinX = Level.SPRITE_SIZE;
        playerMaxX = Settings.SCENE_WIDTH - Level.SPRITE_SIZE;
        playerMinY = Level.SPRITE_SIZE;
        playerMaxY = Settings.SCENE_HEIGHT - Level.SPRITE_SIZE;

        xStartLocation = x;
        yStartLocation = y;

        this.spriteBase = new SpriteBase("/BubLeft.png", x, y, r, dx, dy, dr);

        this.addObserver(levelController);
        this.addObserver(levelController.getScreenController());
    }

    /**
     * The function that processes the input.
     */
    public void processInput() {

        if (!isDead && !isDelayed) {
            if (isJumping && getdY() <= 0) {
                setdY(getdY() + 0.6);
            } else if (isJumping && getdY() > 0) {
                setdY(getdY() + 0.6);
                setJumping(false);
            } else {
                setdY(0);
            }

            moveVertical();
            moveHorizontal();
            checkFirePrimary();
        } else {
            if (!isDelayed) {
                checkIfGameOver();
            }
        }

        spriteBase.checkBounds(playerMinX, playerMaxX, playerMinY, playerMaxY, levelController);
        checkPowerups();
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
            if (doubleSpeedCounter >= durationDoubleSpeed) {
                setDoubleSpeed(false);
                setSpeed(Settings.PLAYER_SPEED);
                setDoubleSpeedCounter(0);
            }
        } else {
            setDoubleSpeedCounter(0);
        }

        if (bubblePowerup) {
            bubblePowerupCounter++;
            if (bubblePowerupCounter >= durationBubblePowerup) {
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

        Double newX = getxLocation() + getdX();
        Double newY = getyLocation() + getdY();

        if (!newX.equals(getxLocation()) || !newY.equals(getyLocation())) {
            Logger.log(String.format("Player moved from (%f, %f) to (%f, %f)",
                    getxLocation(), getyLocation(), newX, newY));
        }
    }

    /**
     * This function applies gravity.
     */
    private void applyGravity() {
        if (!spriteBase.causesCollisionWall(getxLocation(), getxLocation() + spriteBase.getWidth(),
                getyLocation() - calculateGravity(), getyLocation() + spriteBase.getHeight() - calculateGravity(),
                levelController)
                || spriteBase.causesCollisionWall(getxLocation(), getxLocation() + spriteBase.getWidth(),
                getyLocation(), getyLocation() + spriteBase.getHeight(), levelController)) {
            if (!isJumping) {
                if (isAbleToDoubleJump
                        && causesBubbleCollision(getxLocation(), getxLocation() + spriteBase.getWidth(),
                        getyLocation() - calculateGravity(),
                        getyLocation() + spriteBase.getHeight() - calculateGravity())) {
                    setAbleToJump(true);
                    setAbleToDoubleJump(false);
                } else if (isAbleToDoubleJump) {
                    setAbleToJump(false);
                }
                setyLocation(getyLocation() - calculateGravity());
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
                if (bubble.getSpriteBase().causesCollision(x, x1, y, y2) && !bubble.isAbleToCatch()) {
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

        if (monster.getSpriteBase().causesCollision(getxLocation(), getxLocation() + spriteBase.getWidth(), getyLocation(), getyLocation() + spriteBase.getHeight())
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
        if (this.getLives() <= 1 && !this.isDead) {
            this.isDead = true;
            setdX(0);
            setdY(0);
            counter = 0;
            spriteBase.setImage("/BubbleBobbleDeath.png");
        } else {
            isDelayed = true;
            spriteBase.setImage("/BubbleBobbleDeath.png");
            this.loseLife();
            this.scorePoints(Settings.POINTS_PLAYER_DIE);
            delayRespawn();
        }
    }

    private void delayRespawn() {
        delayTimer = new Timer();
        delayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isDelayed = false;
                isImmortal = true;

                setdX(0);
                setdY(0);
                setxLocation(xStartLocation);
                setyLocation(yStartLocation);
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
     * This method will remove a bubble from the ArrayList of Bubbles shot by the Player.
     */
    public void checkBubbles() {

        Iterator<Bubble> i = bubbles.iterator();

        while (i.hasNext()) {

            Bubble bubble = i.next();
            if (bubble.checkPop() && !bubble.isPrisonBubble()) {

                i.remove();
                this.setChanged();
                this.notifyObservers();
                
                Logger.log("Bubble is popped");
            }
        }

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
                spriteBase.setImage("/BubRightRed.png");
            } else {
                spriteBase.setImage("/BubRight.png");
            }
        } else {
            if (isImmortal) {
                spriteBase.setImage("/BubLeftRed.png");
            } else {
                spriteBase.setImage("/BubLeft.png");
            }
        }
    }

    private void jump() {
        setAbleToJump(false);
        setJumping(true);
        setdY(-Settings.JUMP_SPEED);
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
            setdX(0d);
        }
    }

    /**
     * This function handles moving to the right.
     */
    private void moveRight() {
        if (!spriteBase.causesCollisionWall(getxLocation() + speed,
                getxLocation() + spriteBase.getWidth() + speed,
                getyLocation(),
                getyLocation() + spriteBase.getHeight(), levelController)) {
            setdX(speed);
        } else if (spriteBase.causesCollisionWall(getxLocation(), getxLocation() + spriteBase.getWidth(),
                getyLocation(), getyLocation() + spriteBase.getHeight(), levelController)) {
            setdX(speed);
        } else {
            if (!isJumping) {
                setdX(0);
            }
        }

        if (isImmortal) {
            spriteBase.setImage("/BubRightRed.png");
        } else {
            spriteBase.setImage("/BubRight.png");
        }
        isFacingRight = true;
    }

    /**
     * This function handles moving to the left.
     */
    private void moveLeft() {
        if (!spriteBase.causesCollisionWall(getxLocation() - speed,
                getxLocation() + spriteBase.getWidth() - speed,
                getyLocation(),
                getyLocation() + spriteBase.getHeight(), levelController)) {
            setdX(-speed);
        } else if (spriteBase.causesCollisionWall(getxLocation(), getxLocation() + spriteBase.getWidth(),
                getyLocation(), getyLocation() + spriteBase.getHeight(), levelController)) {
            setdX(-speed);
        } else {
            if (!isJumping) {
                setdX(0);
            }
        }

        if (isImmortal) {
            spriteBase.setImage("/BubLeftRed.png");
        } else {
            spriteBase.setImage("/BubLeft.png");
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
            Bubble bubble = new Bubble(getxLocation(), getyLocation(), 0, 0, 0, 0,
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
     * @param points the amount of scored points.
     * @return the Player instance for chaining.
     */
    public Player scorePoints(int points) {
        this.setScore(this.getScore() + points);

        this.setChanged();
        this.notifyObservers();

        return this;
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
     * @return the number of lives.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Set the number of lives.
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

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;

        this.setChanged();
        this.notifyObservers();
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;

        this.setChanged();
        this.notifyObservers();
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;

        this.setChanged();
        this.notifyObservers();
    }

    public ArrayList<Bubble> getBubbles() {
        return bubbles;
    }

    public void setBubbles(ArrayList<Bubble> bubbles) {
        this.bubbles = bubbles;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isFacingRight() {
        return isFacingRight;
    }

    public void setFacingRight(boolean facingRight) {
        isFacingRight = facingRight;

        this.setChanged();
        this.notifyObservers();
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isImmortal() {
        return isImmortal;
    }

    public void setImmortal(boolean immortal) {
        isImmortal = immortal;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isDelayed() {
        return isDelayed;
    }

    public void setDelayed(boolean delayed) {
        isDelayed = delayed;

        this.setChanged();
        this.notifyObservers();
    }

    public Timer getImmortalTimer() {
        return immortalTimer;
    }

    public void setImmortalTimer(Timer immortalTimer) {
        this.immortalTimer = immortalTimer;

        this.setChanged();
        this.notifyObservers();
    }

    public Timer getDelayTimer() {
        return delayTimer;
    }

    public void setDelayTimer(Timer delayTimer) {
        this.delayTimer = delayTimer;

        this.setChanged();
        this.notifyObservers();
    }

    public LevelController getLevelController() {
        return levelController;
    }

    public void setLevelController(LevelController levelController) {
        this.levelController = levelController;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isAbleToJump() {
        return isAbleToJump;
    }

    public void setAbleToJump(boolean ableToJump) {
        isAbleToJump = ableToJump;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isAbleToDoubleJump() {
        return isAbleToDoubleJump;
    }

    public void setAbleToDoubleJump(boolean ableToDoubleJump) {
        isAbleToDoubleJump = ableToDoubleJump;

        this.setChanged();
        this.notifyObservers();
    }

    public double getPlayerMinX() {
        return playerMinX;
    }

    public void setPlayerMinX(double playerMinX) {
        this.playerMinX = playerMinX;

        this.setChanged();
        this.notifyObservers();
    }

    public double getPlayerMaxX() {
        return playerMaxX;
    }

    public void setPlayerMaxX(double playerMaxX) {
        this.playerMaxX = playerMaxX;

        this.setChanged();
        this.notifyObservers();
    }

    public double getPlayerMinY() {
        return playerMinY;
    }

    public void setPlayerMinY(double playerMinY) {
        this.playerMinY = playerMinY;

        this.setChanged();
        this.notifyObservers();
    }

    public double getPlayerMaxY() {
        return playerMaxY;
    }

    public void setPlayerMaxY(double playerMaxY) {
        this.playerMaxY = playerMaxY;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isDoubleSpeed() {
        return doubleSpeed;
    }

    public void setDoubleSpeed(boolean doubleSpeed) {
        this.doubleSpeed = doubleSpeed;

        this.setChanged();
        this.notifyObservers();
    }

    public int getDoubleSpeedCounter() {
        return doubleSpeedCounter;
    }

    public void setDoubleSpeedCounter(int doubleSpeedCounter) {
        this.doubleSpeedCounter = doubleSpeedCounter;

        this.setChanged();
        this.notifyObservers();
    }

    public int getDurationDoubleSpeed() {
        return durationDoubleSpeed;
    }

    public void setDurationDoubleSpeed(int durationDoubleSpeed) {
        this.durationDoubleSpeed = durationDoubleSpeed;

        this.setChanged();
        this.notifyObservers();
    }

    public boolean isBubblePowerup() {
        return bubblePowerup;
    }

    public void setBubblePowerup(boolean bubblePowerup) {
        this.bubblePowerup = bubblePowerup;

        this.setChanged();
        this.notifyObservers();
    }

    public int getBubblePowerupCounter() {
        return bubblePowerupCounter;
    }

    public void setBubblePowerupCounter(int bubblePowerupCounter) {
        this.bubblePowerupCounter = bubblePowerupCounter;

        this.setChanged();
        this.notifyObservers();
    }

    public int getDurationBubblePowerup() {
        return durationBubblePowerup;
    }

    public void setDurationBubblePowerup(int durationBubblePowerup) {
        this.durationBubblePowerup = durationBubblePowerup;

        this.setChanged();
        this.notifyObservers();
    }

    public double getxStartLocation() {
        return xStartLocation;
    }

    public void setxStartLocation(double xStartLocation) {
        this.xStartLocation = xStartLocation;

        this.setChanged();
        this.notifyObservers();
    }

    public double getyStartLocation() {
        return yStartLocation;
    }

    public void setyStartLocation(double yStartLocation) {
        this.yStartLocation = yStartLocation;

        this.setChanged();
        this.notifyObservers();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;

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