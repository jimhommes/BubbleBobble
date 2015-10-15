package model;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by toinehartman on 11/09/15.
 */
public class WallTest {
    private Wall wall;

    /**
     * This tests that there is a wall, that isn't null.
     */
    @Test
    public void testWall() {
    	Coordinates coordinates = new Coordinates(0, 0, 0, 0, 0, 0);
        wall = new Wall(coordinates);
        assertNotNull(wall);
    }
}
