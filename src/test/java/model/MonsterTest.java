package model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import controller.LevelController;
import junit.framework.TestCase;
import model.Input;
import model.Monster;
import model.Settings;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jeffrey
 *
 */
public class MonsterTest {

	private static Monster monster;
	private static Input input;
	
	@Before
	public void setUp() throws Exception{
		LevelController levelController = mock(LevelController.class);
		monster = new Monster("../resources/ZenChanLeft.png", 1, 1, 0, 1, 0, 0, Settings.MONSTER_SPEED, true);
	}
	
	@Test
	public void testCheckCollision() throws Exception {
		Bubble bubble = mock(Bubble.class);
		when(bubble.getX()).thenReturn(1.0);
        when(bubble.getY()).thenReturn(1.0);
        when(bubble.getWidth()).thenReturn(300.0);
        when(bubble.getHeight()).thenReturn(300.0);
        when(bubble.getAbleToCatch()).thenReturn(true);
        monster.checkCollision(bubble);
        when(bubble.getAbleToCatch()).thenReturn(false);
        monster.checkCollision(bubble);
        assertTrue(monster.isCaughtByBubble());
        assertEquals(monster.getPrisonBubble(), bubble);
	}
	
	@Test
	public void testIsFacingRight() throws Exception {
		assertTrue(monster.isFacingRight());
		monster.setFacingRight(false);
		assertFalse(monster.isFacingRight());
	}

	@Test
	public void testGetSpeed() throws Exception {
		assertEquals(monster.getSpeed(), Settings.MONSTER_SPEED, 0);
	}
	
	@Test
	public void testMove() throws Exception {
		monster.move();
		assertEquals(monster.getX(), 2, 0);
	}
}
