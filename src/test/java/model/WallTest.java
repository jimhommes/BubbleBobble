package model;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by toinehartman on 11/09/15.
 */
public class WallTest {

    /**
     * This tests that there is a wall, that isn't null.
     */
    @Test
    public void testWall() {
        Wall wall = new Wall(0, 0, 0, 0, 0, 0);
        assertNotNull(wall);
    }
}
