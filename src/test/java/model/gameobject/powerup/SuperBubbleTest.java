package model.gameobject.powerup;

import model.gameobject.player.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utility.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests the bubbles.
 */
public class SuperBubbleTest {

    private PlayerEnhancement playerEnhancement;
    private Player p = mock(Player.class);

    /**
     * This method is run before all the tests to initialize them.
     */
    @Before
    public void setUp() {
        Settings.initialize("test.properties");

        playerEnhancement = spy(new SuperBubble(p));
    }

    /**
     * Remove the properties file if it exists.
     */
    @After
    public void breakDown() {
        try {
            Files.delete(Paths.get("test.properties"));
        } catch (IOException e) {
            return;
        }
    }

    /**
     * Test the check method.
     */
    @Test
    public void checkTest() {
        boolean result = playerEnhancement.check();
        int counter = 1;

        while (result) {
            assertTrue(result);
            counter++;

            result = playerEnhancement.check();
        }

        assertEquals(400, counter);
        verify(playerEnhancement, times(1)).remove();
    }
}
