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
		walker = new Walker(0, 0, 0, 10, 0, 0, Settings.MONSTER_SPEED, true, levelController);
		walls = new ArrayList<Wall>();
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
		assertEquals(Level.SPRITE_SIZE + Settings.MONSTER_SPEED , walker.getxLocation(), 0);
		walker.setFacingRight(false);
		walker.move();
		assertEquals(Level.SPRITE_SIZE, walker.getxLocation(), 0);
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
		ArrayList<Wall> wall = new ArrayList<Wall>();
		wall.add(new Wall(32, 32, 32, 1, 1, 1));
		when(levelController.getCurrLvl().getWalls()).thenReturn(wall);
		//when(levelcon)
		Bubble bubble = mock(Bubble.class);
		when(bubble.getxLocation()).thenReturn(30.0);
        when(bubble.getyLocation()).thenReturn(4.0);
        when(bubble.getSpriteBase().getWidth()).thenReturn(32.0);
        when(bubble.getSpriteBase().getHeight()).thenReturn(32.0);
        when(bubble.isAbleToCatch()).thenReturn(true);
        walker.getSpriteBase().setWidth(32.0);
        walker.getSpriteBase().setHeight(32.0);
        walker.checkCollision(bubble);
        walker.move();
        assertEquals(bubble.getxLocation(), walker.getxLocation(), 0);
	}
	
	/**
	 * Test what happens when the walker moves out of the bottom screen.
	 * @throws Exception .
	 */
	@Test
	public void testMoveDown() throws Exception {
		ArrayList<Wall> wall = new ArrayList<Wall>();
		wall.add(new Wall(32, 32, 32, 1, 1, 1));
		when(levelController.getCurrLvl().getWalls()).thenReturn(wall);
		Walker walker1 = new Walker(0, Settings.SCENE_HEIGHT
				, 0, 0, 0, 0, Settings.PLAYER_SPEED, true, levelController);
		walker1.move();
    	assertEquals(Level.SPRITE_SIZE, walker1.getyLocation(), 0.0001);
	}
	
	/**
	 * Test what happens when the walker has a horizontal collision.
	 * @throws Exception .
	 */
	@Test
	public void testMoveCollision() throws Exception {
		Wall wall = new Wall(32, 32, 32, 0, 0, 0);
		walls.add(wall);
		when(levelController.getCurrLvl().getWalls()).thenReturn(walls);
		double locationY = walker.getyLocation();
		
		assertEquals(locationY, walker.getyLocation(), 0.0001);
	}
}
