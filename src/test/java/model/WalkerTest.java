package model;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WalkerTest {

	private static Walker walker;
	
	@BeforeClass
	public static void before() {
		walker = new Walker(0, 0, 0, 10, 0, 0, Settings.MONSTER_SPEED, true);
	}
	
	@Test
	public void testSwitchingDirectionFalse() {
		walker.switchDirection();
		assertEquals(false, walker.isFacingRight());
	}
	
	@Test
	public void testSwitchingDirectionTrue() {
		walker.setFacingRight(false);
		walker.switchDirection();
		assertEquals(true, walker.isFacingRight());
	}

	@Test
	public void testMove() throws Exception {
		walker.move();
		assertEquals(Settings.MONSTER_SPEED , walker.getX(), 0);
		walker.setFacingRight(false);
		walker.move();
		assertEquals(0, walker.getX(), 0);
	}
	
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
        assertEquals(0, walker.getX(), 0);
	}
}
