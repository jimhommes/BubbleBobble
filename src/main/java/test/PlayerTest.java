package test;

import controller.LevelController;
import javafx.scene.layout.Pane;
import junit.framework.TestCase;
import model.Input;
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
    private static Pane layer;
    private static Input input;
    private static LevelController levelController;

    public void setUp() throws Exception {
        layer = mock(Pane.class);
        input = mock(Input.class);
        levelController = mock(LevelController.class);
        player = new Player(layer, "../BubRight.png", 0, 0, 0, 0, 0, 0, Settings.PLAYER_SPEED, input, levelController);
    }

    public void testProcessInput() throws Exception {
        when(input.isMoveDown()).thenReturn(true);
        player.processInput();
        assertEquals(player.getY(), Settings.PLAYER_SPEED);
    }

    public void testMove() throws Exception {

    }

    public void testGetBubbles() throws Exception {

    }

    public void testCheckCollideMonster() throws Exception {

    }

    public void testDie() throws Exception {

    }

    public void testGetDead() throws Exception {

    }

    public void testGetGameOver() throws Exception {

    }
}