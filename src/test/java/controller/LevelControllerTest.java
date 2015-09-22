package controller;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import model.Input;
import model.Player;
import org.junit.Before;
import org.junit.Test;
import utility.Settings;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Tests the creation of a LevelController.
 *
 * @author Lili
 */
public class LevelControllerTest {

    private LevelController levelController;
    private MainController mainController;
    private Pane pane;

    /**
     * The setup before every test.
     */
    @Before
    public void setUp() {
        mainController = mock(MainController.class);
        pane = mock(Pane.class);
        when(mainController.getPlayfieldLayer()).thenReturn(pane);
        levelController = new LevelController(mainController);
        levelController.setScreenController(new ScreenController(new Pane()));
    }

    /**
     * Tests the initialization.
     */
    @Test
    public void testInitialisation() {
        assertTrue(levelController != null);
    }

    /**
     * Tests the findmaps function.
     */
    @Test
    public void testFindMaps() {
        ArrayList<String> maps = levelController.getMaps();
        assertEquals(Settings.AMOUNT_MAPS, maps.size());
        levelController.findMaps();
        assertEquals(2 * Settings.AMOUNT_MAPS, maps.size());
        maps.forEach(map -> assertTrue(map.matches("map[0-9]*.txt")));
    }

    /**
     * Tests the createTimer() method.
     */
    @Test
    public void testCreateTimer() {
        AnimationTimer timer = levelController.createTimer();
        assertTrue(timer != null);
    }

    /**
     * The test that tests the createPlayer() method.
     */
	@Test
	public void testCreatePlayer() {
		assertTrue(levelController.getPlayers().isEmpty());
        levelController.setScreenController(mock(ScreenController.class));
		levelController.createPlayer(mock(Input.class));
		ArrayList<Player> players = levelController.getPlayers();
		assertTrue(!players.isEmpty());
		assertEquals(200, players.get(0).getX(), 0.001);
		assertEquals(200, players.get(0).getY(), 0.001);
		assertEquals(Settings.PLAYER_SPEED, players.get(0).getSpeed(), 0.001);
	}

    /**
     * Tests the startLevel() method without maps.
     */
    @Test
    public void testStartLevelNoMaps() {
        levelController.setMaps(new ArrayList<>());
        levelController.startLevel(mock(AnimationTimer.class));
        assertNull(levelController.getPlayfieldLayer().getOnMousePressed());
    }

    /**
     * Tests the startLevel() method with maps.
     */
    @Test
    public void testStartLevel() {
        assertTrue(levelController.getPlayfieldLayer().getOnMousePressed() != null);
        assertEquals(0, levelController.getIndexCurrLvl());
    }

    //Uses createPlayer();
//	@Test
//	public void testCreateLevel() {
//		assertTrue(levelController.getCurrLvl() == null);
//		levelController.findMaps();
//		levelController.createLvl();
//		assertTrue(levelController.getCurrLvl() != null);
//	}


}
