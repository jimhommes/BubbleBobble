package model.gameobject.bubble;

import java.util.ArrayList;

import controller.LevelController;
import model.gameobject.player.Player;
import model.support.Coordinates;
import model.support.SpriteBase;
import utility.Logger;
import utility.Settings;

/**
 * The class were the enemy bubbles are created.
 *
 */
public class EnemyBubble extends Bubble {

  private boolean right;
  private LevelController levelController;
  
  /**
   * Here is the bubble created that is shot by the enemy.
   * @param coordinates The coordinates of the bubbles.
   * @param firedRight If the bubble was fired to the right.
   * @param powerup if the bubble is shot during bubble powerup.
   * @param levelController that controller of the level where the bubble is in.
   */
  public EnemyBubble(Coordinates coordinates, boolean firedRight, boolean powerup,
                     LevelController levelController) {
    super(firedRight, powerup, levelController);
    
    this.right = firedRight;
    
    if (right) {
      coordinates.setXCoordinate(
              coordinates.getXCoordinate() + Settings.SPRITE_FINAL_ENEMY_SIZE + 1);
      setSpriteBase(new SpriteBase("FireRight.png", coordinates));
    } else {
      coordinates.setXCoordinate(coordinates.getXCoordinate() - Settings.SPRITE_SIZE - 1);
      setSpriteBase(new SpriteBase("FireLeft.png", coordinates));
    }
    
    this.levelController = levelController;
  }
  
  @Override
  public void move() {
    Double newX = getSpriteBase().getXCoordinate() + getSpriteBase().getDxCoordinate();
    Double newY = getSpriteBase().getYCoordinate() + getSpriteBase().getDyCoordinate();
    
    if (!newX.equals(getSpriteBase().getXCoordinate())
            || !newY.equals(getSpriteBase().getYCoordinate())) {
      Logger.log(String.format("Bubble moved from (%f, %f) to (%f, %f)",
          getSpriteBase().getXCoordinate(), getSpriteBase().getYCoordinate(), newX, newY));
    }
    
    moveHorizontally();
    getSpriteBase().move();
  }
  
  /**
   * This function moves the EnemyBubble horizontally.
   */
  private void moveHorizontally() {
    if (!causeCollisionPlayer()) {
      if (right) {
        getSpriteBase().setDxCoordinate(Settings.BUBBLE_INIT_SPEED);
      } else {
        getSpriteBase().setDxCoordinate(-Settings.BUBBLE_INIT_SPEED);
      }
    }  
  }

  /**
   * This function checks for each player a EnemyBubble collision.
   * @return a boolean.
   */
  private boolean causeCollisionPlayer() {

    ArrayList<Player> players = levelController.getPlayers();
    
    for (Player player : players) {
      double minX = player.getSpriteBase().getXCoordinate();
      double maxX = minX + Settings.SPRITE_SIZE;
      double minY = player.getSpriteBase().getYCoordinate();
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
      if (getSpriteBase().getXCoordinate() + Settings.SPRITE_SIZE >= playerMinX 
          && getSpriteBase().getXCoordinate() <= playerMaxX 
          && playerMinY <= getSpriteBase().getYCoordinate() + Settings.SPRITE_SIZE 
          && playerMaxY >= getSpriteBase().getYCoordinate()) {
        this.setIsPopped(true);
        return true;
      }
    } else {
      if (getSpriteBase().getXCoordinate() <= playerMaxX 
          && getSpriteBase().getXCoordinate() + Settings.SPRITE_SIZE >= playerMinX 
          && playerMinY <= getSpriteBase().getYCoordinate() + Settings.SPRITE_SIZE 
          && playerMaxY >= getSpriteBase().getYCoordinate()) {
        this.setIsPopped(true);
        return true;
      }
    }
    return false;
  }

}