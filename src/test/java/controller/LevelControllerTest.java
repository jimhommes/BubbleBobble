package controller;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.Coordinates;
import model.Input;
import model.Level;
import model.Monster;
import model.Player;
import model.Powerup;
import model.SpriteBase;
import model.Wall;
import org.junit.Before;
import org.junit.Test;

import utility.Settings;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;


/**
 * Tests the creation of a LevelController.
 */
public class LevelControllerTest {

    private LevelController levelController;
    private MainController mainController;

    private AnimationTimer gameLoopTest; 
    @SuppressWarnings("rawtypes")
	private ArrayList<Player> playersTest = new ArrayList<>();
    private Player playerTest = mock(Player.class);
    @SuppressWarnings("rawtypes")
	private ArrayList<Monster> monstersTest = new ArrayList<>();
    private Monster monsterTest = mock(Monster.class);

    /**
     * The setup before every test.
     */
    @SuppressWarnings("unchecked")
	@Before
    public void setUp() {
        mainController = mock(MainController.class);
        when(mainController.createInput(any(Integer.class))).thenReturn(mock(Input.class));
        Pane pane = mock(Pane.class);
        when(mainController.getPlayFieldLayer()).thenReturn(pane);
        levelController = new LevelController(mainController, 1);
        levelController.setScreenController(new ScreenController(new Pane()));
        
        gameLoopTest = levelController.createTimer();
        playersTest.add(playerTest);
        monstersTest.add(monsterTest);
    }

    /**
     * Tests the initialization.
     */
    @Test
    public void testInitialisation() {
        assertTrue(levelController != null);
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testCreatePlayer() {
		assertTrue(levelController.getPlayers().isEmpty());
        Level level = mock(Level.class);
        levelController.setCurrLvl(level);
        levelController.setScreenController(mock(ScreenController.class));
        ArrayList resplayers = new ArrayList();
        Coordinates coordinates = new Coordinates(200.0, 200.0, 0, 0, 0, 0);
        resplayers.add(new Player(levelController, coordinates, 5.0, 5, mock(Input.class), 1));
        when(level.getPlayers()).thenReturn(resplayers);
        levelController.createPlayers();
        ArrayList<Player> players = levelController.getPlayers();

		assertTrue(!players.isEmpty());
		assertEquals(200, players.get(0).getSpriteBase().getX(), 0.001);
		assertEquals(200, players.get(0).getSpriteBase().getY(), 0.001);
		assertEquals(Settings.PLAYER_SPEED, players.get(0).getSpeed(), 0.001);
	}

    /**
     * Tests the startLevel() method without maps.
     */
    @Test
    public void testStartLevelNoMaps() {
        levelController.setMaps(new ArrayList<>());
        levelController.startLevel();
        assertNull(levelController.getPlayFieldLayer().getOnMousePressed());
    }

    /**
     * Tests the startLevel() method with maps.
     */
    @Test
    public void testStartLevel() {
        assertTrue(levelController.getPlayFieldLayer().getOnMousePressed() != null);
        assertEquals(0, levelController.getIndexCurrLvl());
    }
    
    
    /**
     * Tests the getScreenController.
     */
    @Test
    public void testScreenController() {
    	ScreenController sc = mock(ScreenController.class);
    	levelController.setScreenController(sc);
    	assertEquals(sc, levelController.getScreenController());
    }
    
    /**
     * This is the test that tests the createLvl() function.
     */
	@Test
	public void testCreateLevel() {
        levelController.setScreenController(mock(ScreenController.class));
		assertNull(levelController.getCurrLvl());
		levelController.getLevelControllerMethods().findMaps();
		levelController.createLvl();
		assertNotNull(levelController.getCurrLvl());
	}

    /**
     * This tests the mouse click event from the gameLoop.
     */
    @Test
    public void testStartLevelMouseEvent() {
        assertFalse(levelController.getGameStarted());
        assertNull(levelController.getCurrLvl());

        levelController.setScreenController(mock(ScreenController.class));

        EventHandler<MouseEvent> handler = levelController.getStartMousePressEventHandler();
        handler.handle(new MouseEvent(MouseEvent.MOUSE_PRESSED,
                0, 0, 0, 0, MouseButton.PRIMARY, 1, true,
                true, true, true, true, true, true, true, true, true, null));

        assertTrue(levelController.getGameStarted());
        assertNotEquals(levelController.getPlayers().size(), 0);
        assertNotEquals(levelController.getCurrLvl(), null);
    }

    /**
     * This tests the nextLevel() function.
     */
    @Test
    public void testNextLevel() {
        levelController.setScreenController(mock(ScreenController.class));
        int index = levelController.getIndexCurrLvl();

        levelController.nextLevel();

        assertEquals(levelController.getIndexCurrLvl(), index + 1);
    }

    /**
     * This tests the next level function when the game is out of maps.
     */
    @Test
    public void testNextLevelWon() {
        levelController.setScreenController(mock(ScreenController.class));
        levelController.setIndexCurrLvl(levelController.getMaps().size());
        int index = levelController.getIndexCurrLvl();

        levelController.nextLevel();

        assertEquals(levelController.getIndexCurrLvl(), index + 1);
        verify(mainController, atLeastOnce()).showWinScreen();
    }

    /**
     * This tests the gameOver function.
     */
    @Test
    public void testGameOver() {
        levelController.gameOver();
        verify(mainController, atLeastOnce()).showGameOverScreen();
    }


    /**
     * This tests the gameLoop.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
    public void testGameLoop() {
        AnimationTimer gameLoop = levelController.createTimer();
        ArrayList<Player> players = new ArrayList<>();
        Player player = mock(Player.class);
        players.add(player);

        ArrayList<Monster> monsters = new ArrayList<>();
        Monster monster = mock(Monster.class);
        monsters.add(monster);

        levelController.setPlayers(players);
        Level level = mock(Level.class);
        levelController.setCurrLvl(level);
        levelController.setScreenController(mock(ScreenController.class));
        when(level.getMonsters()).thenReturn(monsters);
        when(level.update()).thenReturn(true);
        int index = levelController.getIndexCurrLvl();
        when(player.isGameOver()).thenReturn(false);

        gameLoop.handle(1);

        verify(player, atLeastOnce()).processInput();
        verify(player, atLeastOnce()).move();
        verify(player, atLeastOnce()).getBubbles();
        verify(player, atLeastOnce()).checkCollideMonster(monster);
        verify(monster, atLeastOnce()).move();
        assertEquals(levelController.getIndexCurrLvl(), index + 1);
        
         
    }
    
    /**
     * This tests the gameLoop.
     */
	@SuppressWarnings("unchecked")
	@Test
    public void testGameLoopWhenNotUpdated() {
  
        levelController.setPlayers(playersTest);
        Level level = mock(Level.class);
        levelController.setCurrLvl(level);
        levelController.setScreenController(mock(ScreenController.class));
        when(level.getMonsters()).thenReturn(monstersTest);
        when(level.update()).thenReturn(false);
        int index = levelController.getIndexCurrLvl();

        when(playerTest.isGameOver()).thenReturn(false);

        gameLoopTest.handle(1);

        verify(playerTest, atLeastOnce()).processInput();
        verify(playerTest, atLeastOnce()).move();
        verify(playerTest, atLeastOnce()).getBubbles();
        verify(playerTest, atLeastOnce()).checkCollideMonster(monsterTest);
        verify(monsterTest, atLeastOnce()).move();
        assertEquals(levelController.getIndexCurrLvl(), index);
    }
    
    /**
     * This tests the game loop when the game is paused.
     */
	@SuppressWarnings("unchecked")
	@Test
    public void testGameLoopPaused() {
   
       
        levelController.setPlayers(playersTest);
        Level level = mock(Level.class);
        levelController.setCurrLvl(level);
        levelController.setScreenController(mock(ScreenController.class));
        when(level.getMonsters()).thenReturn(monstersTest);
        when(level.update()).thenReturn(true);
        when(playerTest.isGameOver()).thenReturn(false);

        EventHandler<KeyEvent> handler = levelController.getPauseKeyEventHandler();
        handler.handle(new KeyEvent(null, null,
                null, "p", "p", KeyCode.P, false, false, false, false));
        gameLoopTest.handle(1);

        verify(playerTest, never()).processInput();
        verify(playerTest, never()).move();
        verify(playerTest, never()).getBubbles();
    }

    
    /**
     * This tests the gameLoop when the game is over for player 1.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
    public void testGameLoopGameOver() {
        AnimationTimer gameLoop = levelController.createTimer();
        ArrayList players = new ArrayList();
        Player player = mock(Player.class);
        players.add(player);
        levelController.setPlayers(players);
        levelController.setCurrLvl(mock(Level.class));

        when(player.isGameOver()).thenReturn(true);

        gameLoop.handle(1);

        verify(player, never()).processInput();
        verify(player, never()).move();
        verify(player, never()).getBubbles();
    }

    /**
     * This tests the gameLoop when the game is over for player 1.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
    public void testGameLoopNextLevel() {
        AnimationTimer gameLoop = levelController.createTimer();
        ArrayList players = new ArrayList();
        Player player = mock(Player.class);
        players.add(player);
        levelController.setPlayers(players);
        Level level = mock(Level.class);
        levelController.setCurrLvl(level);

        int index = levelController.getIndexCurrLvl();

        when(player.isGameOver()).thenReturn(true);
        when(level.update()).thenReturn(false);

        gameLoop.handle(1);

        assertEquals(levelController.getIndexCurrLvl(), index);

    }

    /**
     * This tests the pausekey handler.
     */
    @Test
    public void testPauseKeyHandler() {
        EventHandler<KeyEvent> handler = levelController.getPauseKeyEventHandler();
        handler.handle(new KeyEvent(null, null,
                null, "p", "p", KeyCode.P, false, false, false, false));
        verify(mainController, atLeastOnce()).showPauseScreen();
        assertTrue(levelController.getLevelControllerMethods().getGamePaused());
    }

    /**
     * This tests the release for the pausekey.
     */
    @Test
    public void testPauseKeyHandlerRelease() {
        EventHandler<KeyEvent> handler = levelController.getPauseKeyEventHandler();
        handler.handle(new KeyEvent(null, null,
                null, "p", "p", KeyCode.P, false, false, false, false));
        verify(mainController, atLeastOnce()).showPauseScreen();
        assertTrue(levelController.getLevelControllerMethods().getGamePaused());

        EventHandler<KeyEvent> handlerRelease = levelController.getPauseKeyEventHandlerRelease();
        handlerRelease.handle(new KeyEvent(null, null,
                KeyEvent.KEY_RELEASED, "p", "p", KeyCode.P, false, false, false, false));

        handler.handle(new KeyEvent(null, null,
                null, "p", "p", KeyCode.P, false, false, false, false));
        verify(mainController, atLeastOnce()).hidePauseScreen();
        assertFalse(levelController.getLevelControllerMethods().getGamePaused());
    }

    /**
     * This tests that nothing happens when p isn't pressed.
     */
    @Test
    public void testPauseKeyHandlerPausedGame() {
        EventHandler<KeyEvent> handler = levelController.getPauseKeyEventHandler();
        handler.handle(new KeyEvent(null, null,
                null, "a", "a", KeyCode.A, false, false, false, false));
        assertFalse(levelController.getLevelControllerMethods().getGamePaused());
    }

    /**
     * This tests if the performPowerupsCycle activates the right methods.
     */
    @Test
    public void testPerformPowerupsCycle() {
        Powerup powerup = mock(Powerup.class);
        levelController.performPowerupsCycle(powerup, mock(Player.class));

        verify(powerup, atLeastOnce()).causesCollision(any(Player.class),
                any(LevelController.class));
        verify(powerup, atLeastOnce()).move();
    }

    /**
     * This tests the updatePowerups function.
     */
    @Test
    public void testUpdatePowerups() {
        Powerup powerup = mock(Powerup.class);
        ArrayList<Powerup> list = new ArrayList<>();
        list.add(powerup);
        levelController.setPowerups(list);

        when(powerup.isPickedUp()).thenReturn(false);
        levelController.updatePowerups();
        assertEquals(1, levelController.getPowerups().size());

        when(powerup.isPickedUp()).thenReturn(true);
        levelController.updatePowerups();
        assertEquals(0, levelController.getPowerups().size());
    }

    /**
     * This function tests the spawnPowerup function.
     */
    @Test
    public void testSpawnPowerup() {
        assertEquals(0, levelController.getPowerups().size());
        Monster monster = mock(Monster.class);
        SpriteBase sprite = mock(SpriteBase.class);
        when(monster.getSpriteBase()).thenReturn(sprite);
        when(sprite.getX()).thenReturn(15.0);
        when(sprite.getY()).thenReturn(30.0);

        Level level = mock(Level.class);
        ArrayList<Wall> list = new ArrayList<>();
        Coordinates coordinates = new Coordinates(0, 0, 0, 0, 0, 0);
        list.add(new Wall(coordinates));
        when(level.getWalls()).thenReturn(list);
        levelController.setCurrLvl(level);

        levelController.setScreenController(mock(ScreenController.class));

        levelController.spawnPowerup(monster);

        assertEquals(1, levelController.getPowerups().size());
        Powerup powerup = levelController.getPowerups().get(0);
        assertEquals(15.0, powerup.getSpriteBase().getX(), 0.1);
        assertEquals(30.0, powerup.getSpriteBase().getY(), 0.1);
    }

    /**
     * This tests the SetGameStarted method.
     */
    @Test
    public void testSetGameStarted() {
        levelController.setGameStarted(true);
        assertEquals(levelController.getGameStarted(), true);
    }
}
