package utility;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This tests the Settings class.
 */
public class SettingsTest {

    /**
     * This tests that the height settings are correct.
     */
    @Test
    public void testSettingsHeight() {
        assertEquals(832.d, Settings.SCENE_HEIGHT, 0.001);
    }

    /**
     * This tests that the width settings are correct.
     */
    @Test
    public void testSettingsWidth() {
        assertEquals(832.d, Settings.SCENE_WIDTH, 0.001);
    }

    /**
     * This tests that the health settings are correct.
     */
    @Test
    public void testSettingsLives() {
        assertEquals(5, Settings.PLAYER_LIVES);
    }

    /**
     * This tests that the player speed settings are correct.
     */
    @Test
    public void testSettingsPlayerSpeed() {
        assertEquals(5.d, Settings.PLAYER_SPEED, 0.001);
    }

    /**
     * This tests that the monster height settings are correct.
     */
    @Test
    public void testSettingsMonsterSpeed() {
        assertEquals(3.5, Settings.MONSTER_SPEED, 0.001);
    }
}
