package model.gameobject.level;

import model.support.Coordinates;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Tests the Wall class.
 */
public class WallTest {

    /**
     * This tests that there is a wall, that isn't null.
     */
    @Test
    public void testWall() {
    	Coordinates coordinates = new Coordinates(0, 0, 0, 0, 0, 0);
        Wall wall = new Wall(coordinates);
        assertNotNull(wall);
    }
}
