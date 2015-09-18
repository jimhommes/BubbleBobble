package controller;

import javafx.animation.AnimationTimer;
import model.Player;
import model.Settings;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Tests the creation of a LevelController.
 * @author Lili
 *
 */
public class LevelControllerTest {

	static LevelController levelController;

	/**
	 * The setup before every test.
	 */
	@Before
	public void setUp() {
		levelController = new LevelController();
	}

	/**
	 * Tests the initialization.
	 */
	@Test
	public void testInitialisation() {
		levelController = new LevelController();
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
		assertEquals(2, maps.size());
		maps.forEach(map -> assertTrue(map.matches("map[0-9]*.txt")));
	}

	@Test
	public void testCreateTimer() {
		AnimationTimer timer = levelController.createTimer();
		assertTrue(timer != null);
	}

	@Test
	public void testCreatePlayer() {
		assertTrue(levelController.getPlayers().isEmpty());
		levelController.createPlayer();
		ArrayList<Player> players = levelController.getPlayers();
		assertTrue(!players.isEmpty());
		assertEquals(200, players.get(0).getX(), 0.001);
		assertEquals(200, players.get(0).getY(), 0.001);
		assertEquals(Settings.PLAYER_SPEED, players.get(0).getSpeed(), 0.001);
	}

}
