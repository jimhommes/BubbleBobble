package model;

import controller.LevelController;
import controller.ScreenController;

import org.junit.Before;
import org.junit.Test;

import utility.Settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * 
 * @author jeffrey
 *
 */
public class MonsterTest {

	private Monster monster;
	

	private static double epsilon = 0.001;
	
    /**
     * This is run before all the tests to initialize them.
     * @throws Exception .
     */
	@Before
	public void setUp() throws Exception {
		LevelController levelController = mock(LevelController.class);
        ScreenController screenController = mock(ScreenController.class);
        when(levelController.getScreenController()).thenReturn(screenController);
        monster = new Monster(1, 1, 0, 1, 0, 0,
				Settings.MONSTER_SPEED, true, levelController);
	}
	
	/**
	 * This tests that collisions can occur.
	 * @throws Exception .
	 */
	@Test
	public void testCheckCollision1() throws Exception {
		Bubble bubble = mock(Bubble.class);
		when(bubble.getSpriteBase().getX()).thenReturn(1.0);
        when(bubble.getSpriteBase().getY()).thenReturn(1.0);
        when(bubble.getSpriteBase().getWidth()).thenReturn(300.0);
        when(bubble.getSpriteBase().getHeight()).thenReturn(300.0);
        when(bubble.isAbleToCatch()).thenReturn(true);
        monster.checkCollision(bubble);
        when(bubble.isAbleToCatch()).thenReturn(false);
        monster.checkCollision(bubble);
        assertTrue(monster.isCaughtByBubble());
        assertEquals(monster.getPrisonBubble(), bubble);
	}
	
	/**
	 * This tests that collisions occur.
	 * @throws Exception .
	 */
	@Test
	public void testCheckCollision2() throws Exception {
		Bubble bubble = mock(Bubble.class);
		when(bubble.getSpriteBase().getX()).thenReturn(1.0);
        when(bubble.getSpriteBase().getY()).thenReturn(1.0);
        when(bubble.getSpriteBase().getWidth()).thenReturn(300.0);
        when(bubble.getSpriteBase().getHeight()).thenReturn(300.0);
        when(bubble.isAbleToCatch()).thenReturn(true);
        monster.checkCollision(bubble);
        monster.checkCollision(bubble);
        assertTrue(monster.isCaughtByBubble());
	}
	   
	/**
	 * This tests if the monster is facing right.
	 * @throws Exception .
	 */
	@Test
	public void testIsFacingRight() throws Exception {
		assertTrue(monster.isFacingRight());
		monster.setFacingRight(false);
		assertFalse(monster.isFacingRight());
	}

	/**
	 * This tests the getspeed method. 
	 * @throws Exception .
	 */
	@Test
	public void testGetSpeed() throws Exception {
		assertEquals(monster.getSpeed(), Settings.MONSTER_SPEED, 0);
	}
	
	/**
	 * This tests that the monsters move. 
	 * @throws Exception .
	 */
	@Test
	public void testMove() throws Exception {
		monster.move();
		assertEquals(monster.getxLocation(), 2, 0);
	}
	
	/**
	 * This tests getting the rotation.
	 * @throws Exception .
	 */
	@Test
	public void testR() throws Exception {
    	monster.setRotation(10);
    	assertEquals(10.0, monster.getRotation(), epsilon);
    	monster.setdR(10);
    	assertEquals(10.0, monster.getdR(), epsilon);
    }

	/**
	 * This tests getting the height and width.
	 * @throws Exception .
	 */
	@Test
    public void testHeightWidth() throws Exception {
    	monster.getSpriteBase().setHeight(100);
    	assertEquals(100.0, monster.getSpriteBase().getHeight(), epsilon);
    	monster.getSpriteBase().setWidth(100);
    	assertEquals(100.0, monster.getSpriteBase().getWidth(), epsilon);
    }
    
    /**
     * This tests that the sprite changed.
     * @throws Exception .
     */
	@Test
    public void testSpriteChanged() throws Exception {
    	monster.getSpriteBase().setSpriteChanged(false);
    	assertFalse(monster.getSpriteBase().getSpriteChanged());
    }
    
    /**
     * This tests if the monster can actually move.
     * @throws Exception .
     */
	@Test
    public void testCanMove() throws Exception {
    	monster.getSpriteBase().setCanMove(false);
    	monster.move();
    	assertEquals(1.0, monster.getxLocation(), epsilon);
    }
}
