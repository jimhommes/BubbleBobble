package model.gameobject.bubble;

import controller.LevelController;
import model.support.Coordinates;
import model.support.SpriteBase;

/**
 * This class creates a bubbe shot by the players.
 * @author jeffr_000
 *
 */
public class PlayerBubble extends Bubble {

  /**
   * The bubble that will be shot to catch the monsters.
   *
   * @param coordinates     The coordinates of the bubbles.
   * @param firedRight      If the bubble was fired to the right.
   * @param powerup         if the bubble is shot during bubble powerup.
   * @param levelController that controller of the level where the bubble is in.
   */
  public PlayerBubble(Coordinates coordinates, boolean firedRight, boolean powerup,
                      LevelController levelController) {
    super(firedRight, powerup, levelController);
    
    setSpriteBase(new SpriteBase("bubble.png", coordinates));
  }

}