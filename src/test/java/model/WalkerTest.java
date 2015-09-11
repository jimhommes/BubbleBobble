package model;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import controller.LevelController;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class tests what happens to the walkers.
 * @author Lili
 *
 */
public class WalkerTest {

	private static Walker walker;
	
	 /**
     * This is run before all the tests to initialize them.
     */
	@BeforeClass
	public static void before() {
		LevelController levelcontroller = mock(LevelController.class);
		walker = new Walker(0, 0, 0, 10, 0, 0, Settings.MONSTER_SPEED, true, levelcontroller);
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
	 * @throws Exception .
	 */
	@Test
	public void testMove() throws Exception {
		walker.move();
		assertEquals(Settings.MONSTER_SPEED , walker.getX(), 0);
		walker.setFacingRight(false);
		walker.move();
		assertEquals(0, walker.getX(), 0);
	}
	
	/**
	 * This tests what happens when the monsters collides with a bubble.
	 * @throws Exception .
	 */
	@Test
	public void testMoveBubble() throws Exception {
		Bubble bubble = mock(Bubble.class);
		when(bubble.getX()).thenReturn(0.0);
        when(bubble.getY()).thenReturn(4.0);
        when(bubble.getWidth()).thenReturn(300.0);
        when(bubble.getHeight()).thenReturn(300.0);
        when(bubble.getAbleToCatch()).thenReturn(true);
        walker.checkCollision(bubble);
        walker.move();
        assertEquals(5.0, walker.getX(), 0);
	}
}
