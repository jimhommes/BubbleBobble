package model;

import controller.LevelController;

/**
 * This class creates a bubbe shot by the players.
 * @author jeffr_000
 *
 */
public class BubblePlayer extends Bubble {

  /**
   * The bubble that will be shot to catch the monsters.
   *
   * @param coordinates     The coordinates of the bubbles.
   * @param firedRight      If the bubble was fired to the right.
   * @param powerup         if the bubble is shot during bubble powerup.
   * @param levelController that controller of the level where the bubble is in.
   */
  public BubblePlayer(Coordinates coordinates, boolean firedRight, boolean powerup,
      LevelController levelController) {
    super(coordinates, firedRight, powerup, levelController);
    
    setSpriteBase(new SpriteBase("bubble.png", coordinates));
  }

}