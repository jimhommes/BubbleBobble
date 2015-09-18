package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by toinehartman on 11/09/15.
 */
public class LevelTest {
    private Level level;


    /**
     * This is run before all the tests to initialize them.
     */
    @Before
    public void setUp() {
        level = new Level("map1.txt", null);
    }

    /**
     * This tests that the map is actually drawn.
     */
    @Test
    public void testDrawMap() {
        level.drawMap();
        assertEquals(8, level.getMonsters().size());
        assertEquals(380, level.getWalls().size());
    }

}
