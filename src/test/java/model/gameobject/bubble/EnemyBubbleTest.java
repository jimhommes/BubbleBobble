package model.gameobject.bubble;

import controller.LevelController;
import controller.ScreenController;
import model.support.Coordinates;
import org.junit.Before;
import org.junit.Test;
import utility.Settings;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class tests what happens to the EnemyBubble.
 * @author jeffr_000
 *
 */
public class EnemyBubbleTest {

  private EnemyBubble enemyBubble;
  private LevelController levelController;
  private Coordinates coordinates;
  
  /**
   * This is done before every method.
   */
  @Before
  public void before() {
    levelController = mock(LevelController.class);
    ScreenController screenConroller = mock(ScreenController.class);
    when(levelController.getScreenController()).thenReturn(screenConroller);
    
    coordinates = new Coordinates(0, 0, 0, 0, 0, 0);
    enemyBubble = new EnemyBubble(coordinates, true, false, levelController);
  }
  
  /**
   * This test test the movement to the right.
   */
  @Test
  public void testMoveRight() {
    double newX = enemyBubble.getSpriteBase().getX() + Settings.BUBBLE_INIT_SPEED;
    enemyBubble.move();
    assertEquals(newX, enemyBubble.getSpriteBase().getX(), 0.001);
  }
  
  /**
   * This test tests the movement to the left.
   */
  @Test
  public void testMoveLeft() {
    enemyBubble = new EnemyBubble(coordinates, false, false, levelController);
    double newX = enemyBubble.getSpriteBase().getX() - Settings.BUBBLE_INIT_SPEED;
    enemyBubble.move();
    assertEquals(newX, enemyBubble.getSpriteBase().getX(), 0.001);
  }  
}