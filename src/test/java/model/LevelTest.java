package model;

import junit.framework.TestCase;

/**
 * Created by toinehartman on 11/09/15.
 */
public class LevelTest extends TestCase {
    private static Level level;


    /**
     * This is run before all the tests to initialize them.
     */
    public void setUp() {
        level = new Level("map1.txt", null);
    }

    /**
     * This tests that the map is actually drawn.
     */
    public void testDrawMap() {
        level.drawMap();
        assertEquals(8, level.getMonsters().size());
        assertEquals(380, level.getWalls().size());
    }

}
