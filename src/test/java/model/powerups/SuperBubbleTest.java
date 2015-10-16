package model.powerups;

import model.Player;
import org.junit.Before;
import org.junit.Test;

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
        playerEnhancement = spy(new SuperBubble(p));
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
