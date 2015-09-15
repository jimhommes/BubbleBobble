package controller;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Tests the creation of a LevelController.
 * @author Lili
 *
 */
public class LevelControllerTest {
	
	/**
	 * Tests the initialization.
	 */
	@Test
	public void testInitialisation() {
		LevelController levelController = new LevelController();
		assertTrue(levelController != null);
	}

}
