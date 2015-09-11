package model;

import junit.framework.TestCase;

/**
 * Created by toinehartman on 11/09/15.
 */
public class SettingsTest extends TestCase {
    private Settings settings;

    /**
     * This method is used to initialize the tests.
     */
    public void setUp() {
    	 settings = new Settings();
         assertNotNull(settings);
    }
    
    /**
     * This tests that the height settings are correct.
     */
    public void testSettingsHeight() {
        assertEquals(832.d, Settings.SCENE_HEIGHT);
    }
    
    /**
     * This tests that the width settings are correct.
     */
    public void testSettingsWidth() {
        assertEquals(832.d, Settings.SCENE_WIDTH);
    }
    
    /**
     * This tests that the health settings are correct.
     */
    public void testSettingsHealth() {
        assertEquals(100.d, Settings.PLAYER_HEALTH);
    }
    
    /**
     * This tests that the player speed settings are correct.
     */
    public void testSettingsPlayerSpeed() {
        assertEquals(5.d, Settings.PLAYER_SPEED);
    }
    
    /**
     * This tests that the monster height settings are correct.
     */
    public void testSettingsMonsterSpeed() {
        assertEquals(5.d, Settings.MONSTER_SPEED);
    }
}
