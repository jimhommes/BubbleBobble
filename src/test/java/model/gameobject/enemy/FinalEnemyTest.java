package model.gameobject.enemy;

import controller.LevelController;
import controller.ScreenController;
import model.gameobject.bubble.PlayerBubble;
import model.support.Coordinates;
import org.junit.Before;
import org.junit.Test;
import utility.Settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class tests what happens to the FinalEnemy.
 * @author jeffr_000
 *
 */
public class FinalEnemyTest {

  private FinalEnemy finalEnemy;
  private LevelController levelController;
  
  /**
   * This method is runned before every test.
   */
  @Before
  public void before() {
    levelController = mock(LevelController.class);
    ScreenController screenController = mock(ScreenController.class);
    when(levelController.getScreenController()).thenReturn(screenController);
    Coordinates coordinates = new Coordinates(0, 100, 0, 0, 0, 0);

    finalEnemy = new FinalEnemy(coordinates, Settings.MONSTER_SPEED, 
                                false, levelController, true, 1);
  }
  
  /**
   * This test tests the movement up of the FinalEnemy.
   */
  @Test
  public void testMoveUp() {
    double newY = finalEnemy.getSpriteBase().getYCoordinate() - finalEnemy.getSpeed();
    finalEnemy.move();
    assertEquals(newY, finalEnemy.getSpriteBase().getYCoordinate(), 0.001);
  }
  
  /**
   * This test tests the movement down of the FinalEnemy.
   */
  @Test
  public void testMoveDown() {
    finalEnemy.getSpriteBase().setYCoordinate(Settings.SPRITE_SIZE);
    double newY = finalEnemy.getSpriteBase().getYCoordinate() + finalEnemy.getSpeed();
    finalEnemy.move();
    assertEquals(newY, finalEnemy.getSpriteBase().getYCoordinate(), 0.001);
  }
  
  /**
   * This test tests what happens when the enemy dies.
   */
  @Test
  public void testDie() {
    Coordinates coordinates = new Coordinates(finalEnemy.getSpriteBase().getXCoordinate(), 
                                              finalEnemy.getSpriteBase().getYCoordinate(),
                                              0, 0, 0, 0);
    PlayerBubble bubble = new PlayerBubble(coordinates, true, false, levelController);
    finalEnemy.checkCollision(bubble);
    assertTrue(finalEnemy.isDead());
  }

}