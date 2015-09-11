package model;

import junit.framework.TestCase;

/**
 * Created by toinehartman on 11/09/15.
 */
public class WallTest extends TestCase {
    private static Wall wall;

    public void testWall() {
        wall = new Wall(0, 0, 0, 0, 0, 0);
        assertNotNull(wall);
    }
}
