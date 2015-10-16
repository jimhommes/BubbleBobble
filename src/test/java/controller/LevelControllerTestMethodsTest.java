package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import java.util.ArrayList;
import org.junit.Test;
import utility.Settings;
import static org.mockito.Mockito.mock;

public class LevelControllerTestMethodsTest {

	private LevelControllerMethods levelControllerMethods;
	
	@Before
	public void setUp(){
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
    
    /**
     * The function that sets the path to the maps.
     */
    @Test
    public void testSetPathMaps() {
        String pathMaps = "path";
        levelControllerMethods.setPathMaps(pathMaps);
        assertEquals(levelControllerMethods.getPathMaps(), pathMaps);
    }

}
