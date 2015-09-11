package test;

import controller.LevelController;
import junit.framework.TestCase;
import model.Input;
import model.Monster;
import model.Player;
import model.Settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Jim on 9/11/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/11/2015
 */
public class PlayerTest extends TestCase {

    private static Player player;
    private static Input input;

    public void setUp() throws Exception {
        input = mock(Input.class);
        LevelController levelController = mock(LevelController.class);
        player = new Player(0, 0, 0, 0, 0, 0, Settings.PLAYER_SPEED, input, levelController);
    }

    public void testProcessInput() throws Exception {
        when(input.isMoveDown()).thenReturn(true);
        when(input.isMoveLeft()).thenReturn(true);
        assertEquals(0.0, player.getDy());
        assertEquals(0.0, player.getDx());
        player.processInput();
        assertEquals(-Settings.PLAYER_SPEED, player.getDx());
        assertEquals(Settings.PLAYER_SPEED, player.getDy());
        assertEquals(0.0, player.getX());
        assertEquals(0.0, player.getY());
    }

    public void testMove() throws Exception {
        when(input.isMoveDown()).thenReturn(true);
        when(input.isMoveLeft()).thenReturn(true);
        player.processInput();
        player.move();
        assertEquals(-Settings.PLAYER_SPEED, player.getX());
        assertEquals(Settings.PLAYER_SPEED - player.calculateGravity(), player.getY());
    }

    public void testGetBubbles() throws Exception {
        when(input.isFirePrimaryWeapon()).thenReturn(true);
        assertTrue(player.getBubbles().size() == 0);
        player.processInput();
        assertTrue(player.getBubbles().size() > 0);
    }

    public void testCheckCollideMonster() throws Exception {
        Monster monster = mock(Monster.class);
        when(monster.getX()).thenReturn(1.0);
        when(monster.getY()).thenReturn(1.0);
        when(monster.getWidth()).thenReturn(300.0);
        when(monster.getHeight()).thenReturn(300.0);
        player.checkCollideMonster(monster);
//        assertTrue(player.getDead());
    }

    public void testDie() throws Exception {
        assertFalse(player.getDead());
        player.die();
        assertTrue(player.getDead());
        assertTrue(player.getImagePath().equals("/BubbleBobbleLogo.png"));
    }

}