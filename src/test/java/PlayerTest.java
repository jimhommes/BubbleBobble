import controller.LevelController;
import controller.ScreenController;
import junit.framework.TestCase;
import model.Input;
import model.Monster;
import model.Player;
import model.Settings;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

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
    private static LevelController levelController;
    private static ScreenController screenController;

    @Before
    public void setUp() throws Exception {
        input = mock(Input.class);
        levelController = mock(LevelController.class);
        screenController = mock(ScreenController.class);
        player = new Player(0, 0, 0, 0, 0, 0, Settings.PLAYER_SPEED, input, levelController);
    }

    @Test
    public void testProcessInputNotDead() throws Exception {
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

    @Test
    public void testProcessInputDead() throws Exception {
        player.die();
        for(int i = 0; i < 100; i++) {
            player.processInput();
        }
        verify(levelController, atLeastOnce()).gameOver();
        assertTrue(player.getGameOver());
    }

    @Test
    public void testMove() throws Exception {
        when(input.isMoveDown()).thenReturn(true);
        when(input.isMoveLeft()).thenReturn(true);
        player.processInput();
        player.move();
        assertEquals(-Settings.PLAYER_SPEED, player.getX());
        assertEquals(Settings.PLAYER_SPEED - player.calculateGravity(), player.getY());
    }

    @Test
    public void testGetBubbles() throws Exception {
        when(input.isFirePrimaryWeapon()).thenReturn(true);
        when(levelController.getScreenController()).thenReturn(screenController);
        assertTrue(player.getBubbles().size() == 0);
        player.processInput();
        assertTrue(player.getBubbles().size() > 0);
    }

    @Test
    public void testCheckCollideMonster() throws Exception {
        Monster monster = mock(Monster.class);
        player.setWidth(100);
        player.setHeight(100);
        when(monster.getX()).thenReturn(1.0);
        when(monster.getY()).thenReturn(1.0);
        when(monster.getWidth()).thenReturn(300.0);
        when(monster.getHeight()).thenReturn(300.0);
        player.checkCollideMonster(monster);
        assertTrue(player.getDead());
    }

    @Test
    public void testDie() throws Exception {
        assertFalse(player.getDead());
        player.die();
        assertTrue(player.getDead());
        assertTrue(player.getImagePath().equals("/BubbleBobbleLogo.png"));
    }

    @Test
    public void testMoveRight() throws Exception {
        when(input.isMoveRight()).thenReturn(true);
        assertEquals(0.0, player.getX());
        player.processInput();
        player.move();
        assertEquals(Settings.PLAYER_SPEED, player.getX());
    }

    @Test
    public void testCollisionRight() throws Exception {
        when(input.isMoveRight()).thenReturn(true);
        when(levelController.causesCollision(any(Double.class),
                any(Double.class),
                any(Double.class),
                any(Double.class))).thenReturn(true);
        player.processInput();
        player.move();
        assertEquals(0.0, player.getX());
    }

    @Test
    public void testCollisionLeft() throws Exception {
        when(input.isMoveLeft()).thenReturn(true);
        when(levelController.causesCollision(any(Double.class),
                any(Double.class),
                any(Double.class),
                any(Double.class))).thenReturn(true);

        player.processInput();
        player.move();
        assertEquals(0.0, player.getX());
    }

    @Test
    public void testMoveUp() throws Exception {
        when(input.isMoveUp()).thenReturn(true);
        player.processInput();
        player.move();

        assertEquals(-Settings.PLAYER_SPEED - player.calculateGravity(), player.getY());
        assertEquals(0.0, player.getX());
    }

    @Test
    public void testCollisionUp() throws Exception {
        when(input.isMoveUp()).thenReturn(true);
        when(levelController.causesCollision(any(Double.class),
                any(Double.class),
                any(Double.class),
                any(Double.class))).thenReturn(true);
        player.processInput();
        player.move();

        //Gravity also doesn't work if for all doubles there is a collision
        assertEquals(0.0, player.getY());
    }

}