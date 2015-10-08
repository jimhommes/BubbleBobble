package model;

import controller.LevelController;
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
	
	 /**
     * This is run before all the tests to initialize them.
     */
	@BeforeClass
	public static void before() {
		levelController = mock(LevelController.class);
		walker = new Walker(0, 0, 0, 10, 0, 0, Settings.MONSTER_SPEED, true, levelController);
		
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
		ArrayList<Wall> wall = new ArrayList<Wall>();
		wall.add(new Wall(32, 32, 32, 1, 1, 1));
		when(levelController.getCurrLvl().getWalls()).thenReturn(wall);
		walker.move();
		assertEquals(Level.SPRITE_SIZE + Settings.MONSTER_SPEED , walker.getX(), 0);
		walker.setFacingRight(false);
		walker.move();
		assertEquals(Level.SPRITE_SIZE, walker.getX(), 0);
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
		when(bubble.getX()).thenReturn(30.0);
        when(bubble.getY()).thenReturn(4.0);
        when(bubble.getWidth()).thenReturn(32.0);
        when(bubble.getHeight()).thenReturn(32.0);
        when(bubble.getAbleToCatch()).thenReturn(true);
        walker.setWidth(32.0);
        walker.setHeight(32.0);
        walker.checkCollision(bubble);
        walker.move();
        assertEquals(bubble.getX(), walker.getX(), 0);
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
    	assertEquals(Level.SPRITE_SIZE, walker1.getY(), 0.0001);
	}
	
	/**
	 * Test what happens when the walker has a horizontal collision.
	 * @throws Exception .
	 */
	@Test
	public void testMoveCollision() throws Exception {
		Wall wall = new Wall(32, 32, 32, 0, 0, 0);
		ArrayList<Wall> walls = new ArrayList<Wall>();
		walls.add(wall);
		when(levelController.getCurrLvl().getWalls()).thenReturn(walls);
		double locationY = walker.getY();
		walker.move();
		assertEquals(locationY, walker.getY(), 0.0001);
	}
}
