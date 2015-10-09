package model;

import controller.LevelController;
import controller.ScreenController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import utility.Settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.when;

import java.util.ArrayList;


/**
 * Created by Jim on 9/11/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/11/2015
 */
public class PlayerTest {

    private Player player;
    private Input input;
    private LevelController levelController;
    private ScreenController screenController;
    private Level level;
    private ArrayList<Wall> walls;

    /**
     * This method is used to initialize the tests.
     * @throws Exception is thrown.
     */
    @Before
    public void setUp() throws Exception {
        input = mock(Input.class);
        levelController = mock(LevelController.class);
        screenController = mock(ScreenController.class);
        level = mock(Level.class);
        when(levelController.getScreenController()).thenReturn(screenController);
        player = new Player(Level.SPRITE_SIZE, Level.SPRITE_SIZE
                , 0, 0, 0, 0, Settings.PLAYER_SPEED, input, levelController);
    	walls = new ArrayList<Wall>();
    	when(levelController.getCurrLvl()).thenReturn(level);
    	when(level.getWalls()).thenReturn(walls);
    }

    /**
     * Tests the process when the player is not dead,
     * and checks that correct dx and dy are returned. .
     * @throws Exception .
     */
    @Test
    public void testProcessInputNotDeadGetDs() throws Exception {
        when(input.isMoveDown()).thenReturn(true);
        when(input.isMoveLeft()).thenReturn(true);
        assertEquals(0.0, player.getDy(), 0.001);
        assertEquals(0.0, player.getDx(), 0.001);
    }

    /**
     * This test process when the player is not dead,
     * and checks that the correct x and y are returned.
     * @throws Exception .
     */
    @Test
    public void testProcessInputNotDeadGetXY() throws Exception {
        when(input.isMoveDown()).thenReturn(true);
        when(input.isMoveLeft()).thenReturn(true);
        player.processInput();
        assertEquals(-Settings.PLAYER_SPEED, player.getDx(), 0.001);
        assertEquals(0.0, player.getDy(), 0.001);
        assertEquals(Level.SPRITE_SIZE, player.getX(), 0.001);
        assertEquals(Level.SPRITE_SIZE, player.getY(), 0.001);
    }
    
    
    /**
     * This tests that the correct input is recieved.
     */
    @Test
    public void testInput() {
    	player.setInput(input);
    	assertEquals(player.getInput(), input);
    }

    /**
     * Tests the process when the player is dead.
     * @throws Exception .
     */
    @Test
    public void testProcessInputDead() throws Exception {
        player.die();
        for (int i = 0; i < 100; i++) {
            player.processInput();
        }
        assertTrue(player.getGameOver());
    }

    /**
     * Tests the move method.
     * @throws Exception .
     */
    @Test
    public void testMove() throws Exception {
        when(input.isMoveLeft()).thenReturn(true);
        player.processInput();
        player.move();
        assertEquals(-Settings.PLAYER_SPEED + Level.SPRITE_SIZE, player.getX(), 0.001);
        assertEquals(Level.SPRITE_SIZE - player.calculateGravity(), player.getY(), 0.001);
    }
    
   

    /**
     * Tests the getBubbles method.
     * @throws Exception .
     */
    @Test
    public void testGetBubbles() throws Exception {
        when(input.isFirePrimaryWeapon()).thenReturn(true);
        when(levelController.getScreenController()).thenReturn(screenController);
        assertSame(player.getBubbles().size(), 0);
        player.processInput();
        assertTrue(player.getBubbles().size() > 0);
    }

    /**
     * Tests the if bubbles are removed from player.bubbles after they are popped.
     * @throws Exception .
     */
    @Test
    public void testPopBubbles() throws Exception {

        when(input.isFirePrimaryWeapon()).thenReturn(true);
        when(levelController.getScreenController()).thenReturn(screenController);
        player.processInput();

        assertTrue(player.getBubbles().size() > 0);

        when(input.isFirePrimaryWeapon()).thenReturn(false);
        for (int i = 0; i <= 300; i++) {
            player.checkBubbles();
            player.getBubbles().forEach(Bubble::move);
            assertTrue(player.getBubbles().size() > 0);
        }

        player.checkBubbles();
        player.getBubbles().forEach(Bubble::move);
        assertSame(player.getBubbles().size(), 0);

    }

    /**
     * Tests if the player has collided with a monster.
     * @throws Exception .
     */
    @Test
    public void testCheckCollideMonster() throws Exception {
        Monster monster = mock(Monster.class);
        player.setWidth(100);
        player.setHeight(100);
        when(monster.causesCollision(player.getX(),
                player.getX() + player.getWidth(),
                player.getY(),
                player.getY() + player.getHeight())).thenReturn(true);
        player.checkCollideMonster(monster);
        assertTrue(player.getDead());
    }

    /**
     * Tests what happens when the player dies.
     * @throws Exception .
     */
    @Test
    public void testDie() throws Exception {
        assertFalse(player.getDead());
        double x = player.getX();
        player.die();
        assertTrue(player.getDead());
        assertEquals(x, player.getX(), 0.001);
        assertEquals(0, player.getDx(), 0.001);
    }

    /**
     * Tests that that player moved to the right.
     * @throws Exception .
     */
    @Test
    public void testMoveRight() throws Exception {
        when(input.isMoveRight()).thenReturn(true);
        assertEquals(Level.SPRITE_SIZE, player.getX(), 0.001);
        player.processInput();
        player.move();
        assertEquals(Settings.PLAYER_SPEED + Level.SPRITE_SIZE, player.getX(), 0.001);
    }

    /**
     * Tests what happens when the player has a collides from the right.
     * @throws Exception .
     */
    @Test
    public void testCollisionRight() throws Exception {
    	Wall wall = new Wall(player.getX() + player.getSpeed(), player.getY(), 0, 0, 0, 0);
    	walls.add(wall);
        when(input.isMoveRight()).thenReturn(true);
        player.processInput();
        assertEquals(Level.SPRITE_SIZE, player.getX(), 0.001);
    }

    /**
     * Tests what happens when the player has a collision from the left.
     * @throws Exception .
     */
    @Test
    public void testCollisionLeft() throws Exception {
    	Wall wall = new Wall(player.getX(), player.getY(), 0, 0, 0, 0);
    	walls.add(wall);
    	when(input.isMoveLeft()).thenReturn(true);  
        player.processInput();
        assertEquals(Level.SPRITE_SIZE, player.getX(), 0.001);
    }
    

    /**
     * Tests what happens when the player has a collision from above.
     * @throws Exception .
     */
    @Test
    public void testCollisionUp() throws Exception {
        when(input.isMoveUp()).thenReturn(true);
        
        player.processInput();

        assertEquals(Level.SPRITE_SIZE, player.getY(), 0.001);
    }
    
    /**
     * This tests the speed.
     */
    @Test
    public void testgetSpeed() {
    	assertEquals(player.getSpeed(), 5.0, 0.001);
    }
    
    /**
     * This tests to see if the jumping is correct.
     */
    @Test
    public void testSetJumping() {
    	player.setJumping(true);
    	assertTrue(player.getJumping());
    }

    /**
     * Test what happens when the player moves out of the bottom screen.
     * @throws Exception .
     */
    @Test
    public void testMoveDown() throws Exception {
    	levelController = mock(LevelController.class);
    	Player player1 = new Player(0, Settings.SCENE_HEIGHT
                , 0, 0, 0, 0, Settings.PLAYER_SPEED, input, levelController);
    	player1.processInput();
    	assertEquals(Level.SPRITE_SIZE, player1.getY(), 0.0001);
    }
}