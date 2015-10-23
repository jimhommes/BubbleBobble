package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import controller.LevelController;
import controller.ScreenController;
import utility.Settings;

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
    double newY = finalEnemy.getSpriteBase().getY() - finalEnemy.getSpeed();
    finalEnemy.move();
    assertEquals(newY, finalEnemy.getSpriteBase().getY(), 0.001);
  }
  
  /**
   * This test tests the movement down of the FinalEnemy.
   */
  @Test
  public void testMoveDown() {
    finalEnemy.getSpriteBase().setY(Settings.SPRITE_SIZE);
    double newY = finalEnemy.getSpriteBase().getY() + finalEnemy.getSpeed();
    finalEnemy.move();
    assertEquals(newY, finalEnemy.getSpriteBase().getY(), 0.001);
  }
  
  /**
   * This test tests what happens when the enemy dies.
   */
  @Test
  public void testDie() {
    Coordinates coordinates = new Coordinates(finalEnemy.getSpriteBase().getX(), 
                                              finalEnemy.getSpriteBase().getY(),
                                              0, 0, 0, 0);
    BubblePlayer bubble = new BubblePlayer(coordinates, true, false, levelController);
    finalEnemy.checkCollision(bubble);
    assertTrue(finalEnemy.isDead());
  }

}