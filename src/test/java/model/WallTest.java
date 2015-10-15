package model;

import controller.LevelController;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

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
        wall = new Wall(0, 0, 0, 0, 0, 0);
        assertNotNull(wall);
    }
}
