package model.support;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the Coordinate class. 
 */
public class CoordinatesTest {

	private Coordinates coordinate;
	
	/**
	 * This happens before all the tests.
	 */
	@Before
    public void setUp() {
		coordinate = new Coordinates(1, 2, 3, 4, 5, 6);
    }
	
	/**
	 * This tests the getX().
	 */
	@Test
	public void testGetX() {
		assertEquals(1, coordinate.getX(), 0);
	}

	/**
	 * This tests the getY().
	 */
	@Test
	public void testGetY() {
		assertEquals(2, coordinate.getY(), 0);
	}
	
	/**
	 * This tests the getRotation().
	 */
	@Test
	public void testGetRotation() {
		assertEquals(3, coordinate.getRotation(), 0);
	}
	
	/**
	 * This tests the getDX().
	 */
	@Test
	public void testGetDX() {
		assertEquals(4, coordinate.getDX(), 0);
	}
	
	/**
	 * This tests the getDY().
	 */
	@Test
	public void testGetDY() {
		assertEquals(5, coordinate.getDY(), 0);
	}
	
	/**
	 * This tests the getDRotation().
	 */
	@Test
	public void testGetDRotation() {
		assertEquals(6, coordinate.getDRotation(), 0);
	}
	
	
	
	
	/**
	 * This tests the setX().
	 */
	@Test
	public void testSetX() {
		coordinate.setX(7);
		assertEquals(7, coordinate.getX(), 0);
	}

	/**
	 * This tests the setY().
	 */
	@Test
	public void testSetY() {
		coordinate.setY(8);
		assertEquals(8, coordinate.getY(), 0);
	}
	
	/**
	 * This tests the setRotation().
	 */
	@Test
	public void testSetRotation() {
		coordinate.setRotation(9);
		assertEquals(9, coordinate.getRotation(), 0);
	}
	
	/**
	 * This tests the setDX().
	 */
	@Test
	public void testSetDX() {
		coordinate.setDX(10);
		assertEquals(10, coordinate.getDX(), 0);
	}
	
	/**
	 * This tests the setDY().
	 */
	@Test
	public void testSetDY() {
		coordinate.setDY(11);
		assertEquals(11, coordinate.getDY(), 0);
	}
	
	/**
	 * This tests the setDRotation().
	 */
	@Test
	public void testSetDRotation() {
		coordinate.setDRotation(12);
		assertEquals(12, coordinate.getDRotation(), 0);
	}
	
}
