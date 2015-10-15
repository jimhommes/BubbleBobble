package model;

import controller.LevelController;
import controller.ScreenController;

import org.junit.BeforeClass;
import org.junit.Test;

import utility.Settings;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

/**
 * This class tests what happens to the walkers.
 * @author Lili
 *
 */
public class WalkerTest {

	private static Walker walker;
	private static LevelController levelController;
	private static ArrayList<Wall> walls;
	
	 /**
     * This is run before all the tests to initialize them.
     */
	@BeforeClass
	public static void before() {
		levelController = mock(LevelController.class);
		ScreenController screenController = mock(ScreenController.class);
		Level level = mock(Level.class);
		when(levelController.getScreenController()).thenReturn(screenController);
		walls = new ArrayList<>();
		Coordinates coordinates = new Coordinates(0, 0, 0, 10, 0, 0);
		walker = new Walker(coordinates, Settings.MONSTER_SPEED, true, levelController);
    	when(levelController.getCurrLvl()).thenReturn(level);
    	when(level.getWalls()).thenReturn(walls);
		
	}
	
	/**
	 * This tests what happens when the monster is switched direction while facing left.
	 */
	@Test
	public void testSwitchingDirectionFalse() {
		walker.switchDirection();
		assertEquals(false, walker.isFacingRight());
	}
	
	/**
	 * This tests what happens when the monster is switched direction while facing right.
	 */
	@Test
	public void testSwitchingDirectionTrue() {
		walker.setFacingRight(false);
		walker.switchDirection();
		assertEquals(true, walker.isFacingRight());
	}

	/**
	 * This tests what happens the monster moved.
	 *
	 * The 32 comes from the size of the sprite.
	 * And the 37 is the sprite size + 1 speed (=5),
	 * 32 + 5 = 37.
	 *
	 * @throws Exception .
	 */
	@Test
	public void testMove() throws Exception {
//		ArrayList<Wall> wall = new ArrayList<Wall>();
//		wall.add(new Wall(32, 32, 32, 1, 1, 1));
//		when(levelController.getCurrLvl().getWalls()).thenReturn(wall);
		walker.move();
		assertEquals(Level.SPRITE_SIZE + Settings.MONSTER_SPEED , walker.getSpriteBase().getX(), 0);
		walker.setFacingRight(false);
		walker.move();
		assertEquals(Level.SPRITE_SIZE, walker.getSpriteBase().getX(), 0);
	}
	
	/**
	 * This tests what happens when the monsters collides with a bubble.
	 *
	 * The 37 comes from the sprite size + speed (32 + 5).
	 *
	 * @throws Exception .
	 */
	@Test
	public void testMoveBubble() throws Exception {
		ArrayList<Wall> wall = new ArrayList<>();
		Coordinates coordinates = new Coordinates(32, 32, 32, 1, 1, 1);
		wall.add(new Wall(coordinates));
		when(levelController.getCurrLvl().getWalls()).thenReturn(wall);
		//when(levelcon)
		Bubble bubble = mock(Bubble.class);
		SpriteBase bubbleSprite = mock(SpriteBase.class);
		when(bubble.getSpriteBase()).thenReturn(bubbleSprite);
		when(bubbleSprite.getX()).thenReturn(30.0);
        when(bubbleSprite.getY()).thenReturn(4.0);
        when(bubbleSprite.getWidth()).thenReturn(32.0);
        when(bubbleSprite.getHeight()).thenReturn(32.0);
        when(bubble.isAbleToCatch()).thenReturn(true);
        walker.getSpriteBase().setWidth(32.0);
        walker.getSpriteBase().setHeight(32.0);
        walker.checkCollision(bubble);
        walker.move();
        assertEquals(bubble.getSpriteBase().getX(), walker.getSpriteBase().getX(), 0);
	}
	
	/**
	 * Test what happens when the walker moves out of the bottom screen.
	 * @throws Exception .
	 */
	@Test
	public void testMoveDown() throws Exception {
		ArrayList<Wall> wall = new ArrayList<>();
		Coordinates wallCoordinates = new Coordinates(32, 32, 32, 1, 1, 1);
		wall.add(new Wall(wallCoordinates));
		when(levelController.getCurrLvl().getWalls()).thenReturn(wall);
		Coordinates walkerCoordinates = new Coordinates(0, Settings.SCENE_HEIGHT
				, 0, 0, 0, 0);
		Walker walker1 = 
				new Walker(walkerCoordinates, Settings.PLAYER_SPEED, true, levelController);
		walker1.move();
    	assertEquals(Level.SPRITE_SIZE, walker1.getSpriteBase().getY(), 0.0001);
	}
	
	/**
	 * Test what happens when the walker has a horizontal collision.
	 * @throws Exception .
	 */
	@Test
	public void testMoveCollision() throws Exception {
		Coordinates coordinates = new Coordinates(32, 32, 32, 0, 0, 0);
		Wall wall = new Wall(coordinates);
		walls.add(wall);
		when(levelController.getCurrLvl().getWalls()).thenReturn(walls);
		double locationY = walker.getSpriteBase().getY();
		
		assertEquals(locationY, walker.getSpriteBase().getY(), 0.0001);
	}
}
