package model.support;

import controller.LevelController;
import controller.ScreenController;
import model.gameobject.level.Level;
import model.gameobject.level.Wall;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class tests the Sprite Base.
 *
 */
public class SpriteBaseTest {

	private static Coordinates co;
	private static SpriteBase sb;
	private static LevelController lc;
	private static ArrayList<Wall> walls;
	
	/**
	 * This happens before all the tests.
	 */
	@BeforeClass
	public static void before() {
		co = new Coordinates(0, 0, 0, 0, 0, 0);
		sb = new SpriteBase("ImageJpeg", co);
		lc = mock(LevelController.class);
		ScreenController screenController = mock(ScreenController.class);
		Level level = mock(Level.class);
		when(lc.getScreenController()).thenReturn(screenController);
		walls = new ArrayList<>();
    	when(lc.getCurrLvl()).thenReturn(level);
    	when(level.getWalls()).thenReturn(walls);
	}
	
	/**
	 * Tests the get image path.
	 */
	@Test
	public void testGetImagePath() {
		assertEquals("ImageJpeg", sb.getImagePath());
	}

	/**
	 * Tests the cause collision.
	 */
	@Test
	public void testCauseCollision() {
		sb.setWidth(10);
		assertFalse(sb.causesCollision(1, 1, 1, 1));
	}
	
	/**
	 * Tests the bounds of x.
	 */
	@Test
	public void testCheckBoundsX() {
		sb.setWidth(1);
		sb.setX(1);
		sb.checkBounds(0, 1, 1, 1, lc);
		assertEquals(0, sb.getX(), 0);
	}
	
	/**
	 * Tests the bounds of y.
	 */
	@Test
	public void testCheckBoundsY() {
		sb.setHeight(1);
		sb.setY(1);
		sb.checkBounds(0, 1, 0, 3, lc);
		assertEquals(1, sb.getY(), 0);
	}


}
