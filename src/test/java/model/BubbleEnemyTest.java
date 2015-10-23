package model;

import controller.LevelController;
import controller.ScreenController;
import org.junit.Before;
import org.junit.Test;
import utility.Settings;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class tests what happens to the BubbleEnemy.
 * @author jeffr_000
 *
 */
public class BubbleEnemyTest {

  private BubbleEnemy bubbleEnemy;
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
    bubbleEnemy = new BubbleEnemy(coordinates, true, false, levelController);
  }
  
  /**
   * This test test the movement to the right.
   */
  @Test
  public void testMoveRight() {
    double newX = bubbleEnemy.getSpriteBase().getX() + Settings.BUBBLE_INIT_SPEED;
    bubbleEnemy.move();
    assertEquals(newX, bubbleEnemy.getSpriteBase().getX(), 0.001);
  }
  
  /**
   * This test tests the movement to the left.
   */
  @Test
  public void testMoveLeft() {
    bubbleEnemy = new BubbleEnemy(coordinates, false, false, levelController);
    double newX = bubbleEnemy.getSpriteBase().getX() - Settings.BUBBLE_INIT_SPEED;
    bubbleEnemy.move();
    assertEquals(newX, bubbleEnemy.getSpriteBase().getX(), 0.001);
  }  
}