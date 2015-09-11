package model;

import junit.framework.TestCase;

/**
 * Created by toinehartman on 11/09/15.
 */
public class WallTest extends TestCase {
    private Wall wall;

    /**
     * This tests that there is a wall, that isn't null.
     */
    public void testWall() {
        wall = new Wall(0, 0, 0, 0, 0, 0);
        assertNotNull(wall);
    }
}
