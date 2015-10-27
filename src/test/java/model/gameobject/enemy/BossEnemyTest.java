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
 */
public class BossEnemyTest {

  private BossEnemy bossEnemy;
  private LevelController levelController;
  
  /**
   * This method is run before every test.
   */
  @Before
  public void before() {
    levelController = mock(LevelController.class);
    ScreenController screenController = mock(ScreenController.class);
    when(levelController.getScreenController()).thenReturn(screenController);
    Coordinates coordinates = new Coordinates(0, 100, 0, 0, 0, 0);

    bossEnemy = new BossEnemy(coordinates, Settings.MONSTER_SPEED, 
                                false, levelController, true, 1);
  }
  
  /**
   * This test tests the movement up of the FinalEnemy.
   */
  @Test
  public void testMoveUp() {
    double newY = bossEnemy.getSpriteBase().getYCoordinate() - bossEnemy.getSpeed();
    bossEnemy.move();
    assertEquals(newY, bossEnemy.getSpriteBase().getYCoordinate(), 0.001);
  }
  
  /**
   * This test tests the movement down of the FinalEnemy.
   */
  @Test
  public void testMoveDown() {
	bossEnemy.getSpriteBase().setYCoordinate(Settings.SPRITE_SIZE);
    double newY = bossEnemy.getSpriteBase().getYCoordinate() + bossEnemy.getSpeed();
    bossEnemy.move();
    assertEquals(newY, bossEnemy.getSpriteBase().getYCoordinate(), 0.001);
  }
  
  /**
   * This test tests what happens when the enemy dies.
   */
  @Test
  public void testDie() {
    Coordinates coordinates = new Coordinates(bossEnemy.getSpriteBase().getXCoordinate(), 
    		bossEnemy.getSpriteBase().getYCoordinate(),
                                              0, 0, 0, 0);
    PlayerBubble bubble = new PlayerBubble(coordinates, true, false, levelController);
    bossEnemy.checkCollision(bubble);
    assertTrue(bossEnemy.isDead());
  }

}