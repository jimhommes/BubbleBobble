package model.powerups;

import controller.LevelController;
import model.Level;
import model.Monster;
import model.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the bubbles.
 */
public class SlowMonsterTest {

    private PlayerEnhancement playerEnhancement;
    private Player p = mock(Player.class);

    /**
     * This method is run before all the tests to initialize them.
     */
    @Before
    public void setUp() {
        LevelController lvlController = mock(LevelController.class);
        Level lvl = mock(Level.class);
        ArrayList<Monster> monsters = new ArrayList<>();
        Monster monster = mock(Monster.class);
        monsters.add(monster);

        when(p.getLevelController()).thenReturn(lvlController);
        when(lvlController.getCurrLvl()).thenReturn(lvl);
        when(lvl.getMonsters()).thenReturn(monsters);

        playerEnhancement = spy(new SlowMonster(p));
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

        assertEquals(450, counter);
        verify(playerEnhancement, times(1)).remove();
    }
}
