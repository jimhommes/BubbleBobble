package model.gameobject.bubble;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import controller.LevelController;
import controller.ScreenController;
import model.gameobject.player.Player;
import model.support.Coordinates;
import utility.Settings;

/**
 * This class tests what happens to the EnemyBubble.
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
    double newX = enemyBubble.getSpriteBase().getXCoordinate() + Settings.BUBBLE_INIT_SPEED;
    enemyBubble.move();
    assertEquals(newX, enemyBubble.getSpriteBase().getXCoordinate(), 0.001);
    enemyBubble.move();
  }
  
  /**
   * This test tests the movement to the left.
   */
  @Test
  public void testMoveLeft() {
    enemyBubble = new EnemyBubble(coordinates, false, false, levelController);
    double newX = enemyBubble.getSpriteBase().getXCoordinate() - Settings.BUBBLE_INIT_SPEED;
    enemyBubble.move();
    assertEquals(newX, enemyBubble.getSpriteBase().getXCoordinate(), 0.001);
  }  
  
  /**
   * This test tests what happens when the enemyBubble collides with a player.
   */
  @Test
  public void testPlayerCollisionRight() {
    Player player = mock(Player.class);
    //Player player = new Player(levelController, coordinates, 0, 5, input, 1);
    
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    when(levelController.getPlayers()).thenReturn(players);
    when(player.getSpriteBase()).thenReturn(enemyBubble.getSpriteBase());
    enemyBubble.move();
    assertTrue(enemyBubble.getIsPopped());
  }
  
  /**
   * This test tests what happens when the enemyBubble collides with a player.
   */
  @Test
  public void testPlayerCollisionLeft() {
    Player player = mock(Player.class);
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    coordinates = new Coordinates(enemyBubble.getSpriteBase().getXCoordinate(), 
                                  0, 0, 0, 0, 0);

    enemyBubble = new EnemyBubble(coordinates, false, false, levelController);
    when(levelController.getPlayers()).thenReturn(players);
    when(player.getSpriteBase()).thenReturn(enemyBubble.getSpriteBase());
    enemyBubble.move();
    assertTrue(enemyBubble.getIsPopped());
  }
  
  /**
   * This test tests what happens when the enemyBubble collides with a player.
   */
  @Test
  public void testPlayerNoCollision() {
    Player player = mock(Player.class);
    //Player player = new Player(levelController, coordinates, 0, 5, input, 1);
    
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    when(levelController.getPlayers()).thenReturn(players);
    when(player.getSpriteBase()).thenReturn(enemyBubble.getSpriteBase());
    coordinates = new Coordinates(enemyBubble.getSpriteBase().getXCoordinate(), 
        0, 0, 0, 0, 0);

    enemyBubble = new EnemyBubble(coordinates, false, false, levelController);
    enemyBubble.move();
    assertFalse(enemyBubble.getIsPopped());
  }
}