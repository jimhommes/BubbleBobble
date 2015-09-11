package model;

import junit.framework.TestCase;

/**
 * Created by toinehartman on 11/09/15.
 */
public class SettingsTest extends TestCase {
    private static Settings settings;

    public void testSettings() {
        assertEquals(832.d, Settings.SCENE_HEIGHT);
        assertEquals(832.d, Settings.SCENE_WIDTH);
        assertEquals(100.d, Settings.PLAYER_HEALTH);
        assertEquals(5.d, Settings.PLAYER_SPEED);
        assertEquals(5.d, Settings.MONSTER_SPEED);

        settings = new Settings();
        assertNotNull(settings);
    }
}
