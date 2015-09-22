package controller;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


/**
 * Tests the creation of a LevelController.
 * @author Lili
 *
 */
public class LevelControllerTest {

	private LevelController levelController;

	/**
	 * The setup before every test.
	 */
	@Before
	public void setUp() {
		levelController = new LevelController();
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
		assertTrue(levelController.getMaps().isEmpty());
		levelController.findMaps();
		ArrayList<String> maps = levelController.getMaps();
		System.out.println(maps);
		assertEquals(4, maps.size());
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

	//Needs FXML
//	@Test
//	public void testCreatePlayer() {
//		assertTrue(levelController.getPlayers().isEmpty());
//		levelController.createPlayer();
//		ArrayList<Player> players = levelController.getPlayers();
//		assertTrue(!players.isEmpty());
//		assertEquals(200, players.get(0).getX(), 0.001);
//		assertEquals(200, players.get(0).getY(), 0.001);
//		assertEquals(Settings.PLAYER_SPEED, players.get(0).getSpeed(), 0.001);
//	}

	/**
	 * Tests the startLevel() method without maps.
	 */
	@Test
	public void testStartLevelNoMaps() {
		levelController.setPlayfieldLayer(new Pane());
		levelController.setMaps(new ArrayList<>());
		levelController.startLevel(mock(AnimationTimer.class));
		assertTrue(levelController.getPlayfieldLayer().getOnMousePressed() == null);
	}

	/**
	 * Tests the startLevel() method with maps.
	 */
	@Test
	public void testStartLevel() {
		levelController.setPlayfieldLayer(new Pane());
		levelController.findMaps();
		levelController.startLevel(mock(AnimationTimer.class));
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
