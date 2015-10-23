package model;

import java.util.ArrayList;

import controller.LevelController;
import utility.Logger;
import utility.Settings;

/**
 * The class were the enemy bubbles are created.
 * @author jeffr_000
 *
 */
public class BubbleEnemy extends Bubble {

  private boolean right;
  private LevelController levelController;
  
  /**
   * Here is the bubble created that is shot by the enemy.
   * @param coordinates The coordinates of the bubbles.
   * @param firedRight If the bubble was fired to the right.
   * @param powerup if the bubble is shot during bubble powerup.
   * @param levelController that controller of the level where the bubble is in.
   */
  public BubbleEnemy(Coordinates coordinates, boolean firedRight, boolean powerup,
      LevelController levelController) {
    super(coordinates, firedRight, powerup, levelController);
    
    this.right = firedRight;
    
    if (right) {
      coordinates.setX(coordinates.getX() + Settings.SPRITE_FINAL_ENEMY_SIZE + 1);
      setSpriteBase(new SpriteBase("FireRight.png", coordinates));
    } else {
      coordinates.setX(coordinates.getX() - Settings.SPRITE_SIZE - 1);
      setSpriteBase(new SpriteBase("FireLeft.png", coordinates));
    }
    
    this.levelController = levelController;
  }
  
  @Override
  public void move() {
    Double newX = getSpriteBase().getX() + getSpriteBase().getDx();
    Double newY = getSpriteBase().getY() + getSpriteBase().getDy();
    
    if (!newX.equals(getSpriteBase().getX()) || !newY.equals(getSpriteBase().getY())) {
      Logger.log(String.format("Bubble moved from (%f, %f) to (%f, %f)",
          getSpriteBase().getX(), getSpriteBase().getY(), newX, newY));
    }
    
    moveHorizontally();
    getSpriteBase().move();
  }
  
  /**
   * This function moves the BubbleEnemy horizontally.
   */
  private void moveHorizontally() {
    if (!causeCollisionPlayer()) {
      if (right) {
        getSpriteBase().setDx(Settings.BUBBLE_INIT_SPEED);
      } else {
        getSpriteBase().setDx(-Settings.BUBBLE_INIT_SPEED);
      }
    }  
  }

  /**
   * This function checks for each player a BubbleEnemy collision.
   * @return a boolean.
   */
  private boolean causeCollisionPlayer() {

    ArrayList<Player> players = levelController.getPlayers();
    
    for (Player player : players) {
      double minX = player.getSpriteBase().getX();
      double maxX = minX + Settings.SPRITE_SIZE;
      double minY = player.getSpriteBase().getY();
      double maxY = minY + Settings.SPRITE_SIZE;
      if (checkPlayerCollision(minX, maxX, minY, maxY) && !player.getIsDelayed()) {
        player.die();
        return true;
      }
    }
    return false;
  }
  
  /**
   * This player checks is the bubble from the enemy collides with a player.
   * @param playerMinX player's minimal x.
   * @param playerMaxX player's maximal x.
   * @param playerMinY player's minimal y.
   * @param playerMaxY player's maximal y.
   * @return a boolean.
   */
  private boolean checkPlayerCollision(double playerMinX, 
                                       double playerMaxX, 
                                       double playerMinY, 
                                       double playerMaxY) {
    if (right) {
      if (getSpriteBase().getX() + Settings.SPRITE_SIZE >= playerMinX 
          && getSpriteBase().getX() <= playerMaxX 
          && playerMinY <= getSpriteBase().getY() + Settings.SPRITE_SIZE 
          && playerMaxY >= getSpriteBase().getY()) {
        this.setIsPopped(true);
        return true;
      }
    } else if (!right) {
      System.out.println(getSpriteBase().getX());
      System.out.println(playerMaxX);
      if (getSpriteBase().getX() <= playerMaxX 
          && getSpriteBase().getX() + Settings.SPRITE_SIZE >= playerMinX 
          && playerMinY <= getSpriteBase().getY() + Settings.SPRITE_SIZE 
          && playerMaxY >= getSpriteBase().getY()) {
        this.setIsPopped(true);
        return true;
      }
    }
    return false;
  }

}