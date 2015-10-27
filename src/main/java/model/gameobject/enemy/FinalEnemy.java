package model.gameobject.enemy;

import controller.LevelController;
import model.gameobject.bubble.Bubble;
import model.gameobject.bubble.EnemyBubble;
import model.gameobject.player.Player;
import model.support.Coordinates;
import model.support.SpriteBase;
import utility.Settings;

import java.util.ArrayList;

/**
 * This class creates the final enemy.
 * @author jeffr_000
 *
 */
public class FinalEnemy extends Monster {

  private LevelController levelController;
  private boolean movingUp;
  private int counter;  
  private double finalEnemyMinY;
  private double finalEnemyMaxY;
  private int lives;
  private Bubble hitBy;
  
  /**
   * The final monster that tries to kill the player.
   * @param coordinates     The coordinates of the monster.
   * @param speed           The speed at which the monster is travelling.
   * @param isFacingRight   Whether the monster is facing to the right or not.
   * @param levelController Is the controller that controls the level.
   * @param movingUp        This holds the direction the monster is moving in.
   * @param lives           The number of lives from the monster.
   */
  public FinalEnemy(Coordinates coordinates,
                    double speed,
                    boolean isFacingRight,
                    LevelController levelController,
                    boolean movingUp,
                    int lives) {
    super(coordinates, speed, isFacingRight, levelController);
    
    setSpriteBase(new SpriteBase("FinalEnemyLeft.png", coordinates));
    getSpriteBase().setXCoordinate(Settings.SCENE_WIDTH / 2 - Settings.SPRITE_FINAL_ENEMY_SIZE / 2);

    this.movingUp = movingUp;
    this.counter = 31;
    this.levelController = levelController;
    this.lives = lives;
    
    finalEnemyMinY = Settings.SPRITE_SIZE;
    finalEnemyMaxY = Settings.SCENE_HEIGHT - Settings.SPRITE_SIZE;
    
    hitBy = null;
  }
  
  @Override
  public void move() {
    checkBubble();
    double random = Math.random();
    if (random >= 0.99) {
      switchDirection("FinalEnemy");
    }
    moveVertical();
    super.move();
    fire();
    counter++;
  }
  
  /**
   * This method removes the bubble that hitted the enemy.
   */
  private void checkBubble() {
    if (hitBy != null) {
      hitBy.setIsPopped(true);
    }
  }

  /**
   * This method moves the final enemy horizontally.
   */
  private void moveVertical() {
    checkBounds();
    if (movingUp) {
      getSpriteBase().setDyCoordinate(-getSpeed());
    } else if (!movingUp) {
      getSpriteBase().setDyCoordinate(getSpeed());
    }
  }
  
  /**
   * Thismethod checks if the monster still is in the playing field.
   */
  private void checkBounds() {
    if (getSpriteBase().getYCoordinate() <= finalEnemyMinY) {
      movingUp = false;
    } else if (getSpriteBase().getYCoordinate() >= finalEnemyMaxY - Settings.SPRITE_FINAL_ENEMY_SIZE) {
      movingUp = true;
    }
  }
  
  /**
   * This method makes the final enemy able to shoot.
   */
  private void fire() {
    if (playerInRange() && counter > 30) {
      Coordinates bubbleCoordinates = 
          new Coordinates(getSpriteBase().getXCoordinate(), 
              getSpriteBase().getYCoordinate() + Settings.SPRITE_FINAL_ENEMY_SIZE, 0, 0, 0, 0);
      EnemyBubble bubble = new EnemyBubble(bubbleCoordinates,
          isFacingRight(), false, levelController);
      levelController.addBubble(bubble);
      counter = 0;
    }
  }
  
  /**
   * This function checks if the enemy faces the same side as the player.
   * @return boolean.
   */
  private boolean playerInRange() {
    ArrayList<Player> players = levelController.getPlayers();
    for (Player player : players) {
      double playerX = player.getSpriteBase().getXCoordinate();
      double playerY = player.getSpriteBase().getYCoordinate();
      if (playerY > getSpriteBase().getYCoordinate() 
          && playerY < getSpriteBase().getYCoordinate() + Settings.SPRITE_FINAL_ENEMY_SIZE) {
        if (playerX < getSpriteBase().getXCoordinate() && !isFacingRight()) {
          return true;
        } else if (playerX > getSpriteBase().getXCoordinate() && isFacingRight()) {
          return true;
        }
      }
    }
    return false;
  }
  
  @Override
  public void checkCollision(final Bubble bubble) {
    double bubbleMinX = bubble.getSpriteBase().getXCoordinate();
    double bubbleMinY = bubble.getSpriteBase().getYCoordinate();
    double bubbleMaxX = bubbleMinX + bubble.getSpriteBase().getWidth();
    double bubbleMaxY = bubbleMinY + bubble.getSpriteBase().getHeight();
    if (bubble.isAbleToCatch()) {
      if (((bubbleMinX >= getSpriteBase().getXCoordinate()
        && bubbleMinX <= getSpriteBase().getXCoordinate() + getSpriteBase().getWidth())
        || (bubbleMaxX >= getSpriteBase().getXCoordinate()
        && bubbleMaxX <= getSpriteBase().getXCoordinate() + getSpriteBase().getWidth()))
        && ((bubbleMinY >= getSpriteBase().getYCoordinate()
        && bubbleMinY <= getSpriteBase().getYCoordinate() + getSpriteBase().getHeight())
        || bubbleMaxY >= getSpriteBase().getYCoordinate()
        && bubbleMaxY <= getSpriteBase().getYCoordinate() + getSpriteBase().getHeight())) {
        hitBy = bubble;
        loseLife();
      }
    }
  }

  /**
   * This function makes the enemy lose a life.
   */
  private void loseLife() {
    lives--;
    if (lives == 0) {
      this.die();
    }
  }
  
  /**
   * This function kills the enemy if it has zero lifes.
   */
  public void die() {
    setDead(true);
    ArrayList<Player> players = levelController.getPlayers();
    for (Player player : players) {
      player.scorePoints(100);
    }
    setChanged();
    notifyObservers();
    destroy();
  }
  
  /**
   * This function gets the counter.
   * @return the counter.
   */
  public int getCounter() {
    return counter;
  }
  
  /**
   * this function shows the number of lives.
   * @return the lives.
   */
  public int showLives() {
    return lives;
  }
}