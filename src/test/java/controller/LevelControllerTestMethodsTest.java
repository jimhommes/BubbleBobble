package controller;

import org.junit.Before;
import org.junit.Test;
import utility.Settings;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Test, yeah!
 */
public class LevelControllerTestMethodsTest {

	private LevelControllerMethods levelControllerMethods;

    /**
     * Set up.
     */
	@Before
	public void setUp() {
		LevelController levelController = mock(LevelController.class);
		levelControllerMethods = new LevelControllerMethods(levelController);
	}
	
	/**
     * Tests the findmaps function.
     */
    @Test
    public void testFindMaps() {
        ArrayList<String> maps = levelControllerMethods.findMaps();
        assertEquals(Settings.AMOUNT_MAPS, maps.size());
        levelControllerMethods.findMaps();
        assertEquals(Settings.AMOUNT_MAPS, maps.size());
        maps.forEach(map -> assertTrue(map.matches("map[0-9]*.txt")));
    }
    
}
