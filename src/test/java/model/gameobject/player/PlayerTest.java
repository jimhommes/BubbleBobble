package model.gameobject.player;

import controller.LevelController;
import controller.LevelControllerMethods;
import controller.ScreenController;
import javafx.animation.AnimationTimer;
import model.gameobject.bubble.Bubble;
import model.gameobject.enemy.Monster;
import model.gameobject.level.Level;
import model.gameobject.level.Wall;
import model.gameobject.powerup.Immortality;
import model.support.Coordinates;
import model.support.Input;
import model.support.SpriteBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utility.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * This tests the Player class.
 */
public class PlayerTest {

    private Player player;
    private Input input;
    private LevelController levelController;
    private ScreenController screenController;
    private ArrayList<Wall> walls;

    /**
     * This method is used to initialize the tests.
     *
     * @throws Exception is thrown.
     */
    @Before
    public void setUp() throws Exception {
        Settings.initialize("test.properties");
        Settings.initializeHighscores("testHighscores.properties");
        Settings.setName("TEST_P1", 0);
        input = mock(Input.class);
        levelController = mock(LevelController.class);
        screenController = mock(ScreenController.class);
        Level level = mock(Level.class);
        when(levelController.getScreenController()).thenReturn(screenController);
        Coordinates coordinates = new Coordinates(
                Settings.SPRITE_SIZE, Settings.SPRITE_SIZE, 0, 0, 0, 0);
        player = new Player(levelController, coordinates, Settings.PLAYER_SPEED, 1, input, 1);
    	walls = new ArrayList<>();
    	when(levelController.getCurrLvl()).thenReturn(level);
    	when(level.getWalls()).thenReturn(walls);
    }

    /**
     * Remove the properties file if it exists.
     */
    @After
    public void breakDown() {
        Settings.setName(null, 0);
        try {
            Files.delete(Paths.get("test.properties"));
            Files.delete(Paths.get("testHighscores.properties"));
        } catch (IOException e) {
            return;
        }
    }

    /**
     * Tests the process when the player is not dead,
     * and checks that correct dx and dy are returned. .
     *
     * @throws Exception .
     */
    @Test
    public void testProcessInputNotDeadGetDs() throws Exception {
        when(input.isMoveDown()).thenReturn(true);
        when(input.isMoveLeft()).thenReturn(true);
        SpriteBase sprite = player.getSpriteBase();
        assertEquals(Settings.SPRITE_SIZE, sprite.getX(), 0.001);
        assertEquals(Settings.SPRITE_SIZE, sprite.getY(), 0.001);
    }

    /**
     * This test process when the player is not dead,
     * and checks that the correct x and y are returned.
     *
     * @throws Exception .
     */
    @Test
    public void testProcessInputNotDeadGetXY() throws Exception {
        when(input.isMoveDown()).thenReturn(true);
        when(input.isMoveLeft()).thenReturn(true);
        SpriteBase sprite = player.getSpriteBase();

        player.processInput();
        assertEquals(-Settings.PLAYER_SPEED, sprite.getDx(), 0.001);
        assertEquals(Settings.GRAVITY_CONSTANT, sprite.getDy(), 0.001);
        assertEquals(Settings.SPRITE_SIZE, sprite.getX(), 0.001);
        assertEquals(Settings.SPRITE_SIZE, sprite.getY(), 0.001);
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
     *
     * @throws Exception .
     */
    @Test
    public void testProcessInputDead() throws Exception {
        player.die();
        for (int i = 0; i < 100; i++) {
            player.processInput();
        }
    }

    /**
     * Tests the move method.
     *
     * @throws Exception .
     */
    @Test
    public void testMove() throws Exception {
        when(input.isMoveLeft()).thenReturn(true);
        player.processInput();
        player.move();
        SpriteBase sprite = player.getSpriteBase();
        assertEquals(-Settings.PLAYER_SPEED + Settings.SPRITE_SIZE, sprite.getX(), 0.001);
        assertEquals(Settings.SPRITE_SIZE - player.calculateGravity(), sprite.getY(), 0.001);
    }

    /**
     * Tests if the player has collided with a monster.
     *
     * @throws Exception .
     */
    @Test
    public void testCheckCollideMonster() throws Exception {
    	Monster monster = mock(Monster.class);
    	SpriteBase monsterSprite = mock(SpriteBase.class);
    	when(monster.getSpriteBase()).thenReturn(monsterSprite);
    	player.getSpriteBase().setWidth(64);
    	player.getSpriteBase().setHeight(64);
    	SpriteBase sprite = player.getSpriteBase();
    	when(monsterSprite.causesCollision(sprite.getX(),
    			sprite.getX() + sprite.getWidth(),
    			sprite.getY(),
    			sprite.getY() + sprite.getHeight())).thenReturn(true);
    	player.checkCollideMonster(monster);
    	assertTrue(player.noLivesLeft());
    } 
    

    /**
     * Tests what happens when the player dies.
     * @throws Exception .
     */
    @Test
    public void testDie() throws Exception {
    	assertFalse(player.noLivesLeft());
    	SpriteBase sprite = player.getSpriteBase();
    	double x = sprite.getX();
    	player.die();
    	assertTrue(player.noLivesLeft());
    	assertEquals(x, sprite.getX(), 0.001);
    	assertEquals(0, sprite.getDx(), 0.001);
    }
    

    /**
     * Tests that that player moved to the right.
     *
     * @throws Exception .
     */
    @Test
    public void testMoveRight() throws Exception {
        when(input.isMoveRight()).thenReturn(true);
        SpriteBase sprite = player.getSpriteBase();
        assertEquals(Settings.SPRITE_SIZE, sprite.getX(), 0.001);
        player.processInput();
        player.move();
        assertEquals(Settings.PLAYER_SPEED + Settings.SPRITE_SIZE, sprite.getX(), 0.001);
    }

    /**
     * Tests what happens when the player has a collides from the right.
     *
     * @throws Exception .
     */
    @Test
    public void testCollisionRight() throws Exception {
        SpriteBase sprite = player.getSpriteBase();
        Coordinates coordinates = 
        		new Coordinates(sprite.getX() + player.getSpeed(), sprite.getY(), 0, 0, 0, 0);
    	Wall wall = new Wall(coordinates);
    	walls.add(wall);
        when(input.isMoveRight()).thenReturn(true);
        player.processInput();
        assertEquals(Settings.SPRITE_SIZE, sprite.getX(), 0.001);
    }

    /**
     * Tests what happens when the player has a collision from the left.
     *
     * @throws Exception .
     */
    @Test
    public void testCollisionLeft() throws Exception {
        SpriteBase sprite = player.getSpriteBase();
        Coordinates coordinates = new Coordinates(sprite.getX(), sprite.getY(), 0, 0, 0, 0);
    	Wall wall = new Wall(coordinates);
    	walls.add(wall);
    	when(input.isMoveLeft()).thenReturn(true);
        player.processInput();
        assertEquals(Settings.SPRITE_SIZE, sprite.getX(), 0.001);
    }

    /**
     * Tests what happens when the player has a collision from above.
     *
     * @throws Exception .
     */
    @Test
    public void testCollisionUp() throws Exception {
        when(input.isMoveUp()).thenReturn(true);

        SpriteBase sprite = player.getSpriteBase();
        double y = sprite.getY();
        player.processInput();

        assertEquals(y, sprite.getY(), 0.001);
    }

    /**
     * This tests the speed.
     */
    @Test
    public void testGetSpeed() {
        assertEquals(player.getSpeed(), 5.0, 0.001);
    }

    /**
     * This tests to see if the jumping is correct.
     */
    @Test
    public void testSetJumping() {
        player.setJumping(true);
        assertTrue(player.isJumping());
    }

    /**
     * Test what happens when the player moves out of the bottom screen.
     *
     * @throws Exception .
     */
    @Test
    public void testMoveDown() throws Exception {
        Level level = mock(Level.class);
    	levelController = mock(LevelController.class);
        ScreenController screenController = mock(ScreenController.class);
        when(levelController.getScreenController()).thenReturn(screenController);
        when(levelController.getCurrLvl()).thenReturn(level);
        Coordinates coordinates = new Coordinates(0, Settings.SCENE_HEIGHT, 0, 0, 0, 0);
        Player player1 = new Player(levelController, coordinates, 
        		Settings.PLAYER_SPEED, Settings.PLAYER_LIVES, input, 1);
        player1.processInput();
        SpriteBase sprite = player1.getSpriteBase();
        assertEquals(Settings.SPRITE_SIZE / 2, sprite.getY(), 0.0001);
    }

    /**
     * This function tests the timer.
     */
    @Test
    public void testTimer() {
        AnimationTimer timer = player.createTimer();
        LevelControllerMethods lcm = mock(LevelControllerMethods.class);
        when(levelController.getLevelControllerMethods()).thenReturn(lcm);
        when(lcm.getGamePaused()).thenReturn(false);

        SpriteBase spriteBase = mock(SpriteBase.class);
        when(spriteBase.getDy()).thenReturn(0.0);
        player.setSpriteBase(spriteBase);

        timer.handle(1);

        assertEquals(0, player.getSpriteBase().getDy(), 0.1);
        verify(spriteBase, atLeastOnce()).move();
    }

    /**
     * This function tests the timer when player is dead.
     */
    @Test
    public void testTimerDead() {
    	AnimationTimer timer = player.createTimer();
    	LevelControllerMethods lcm = mock(LevelControllerMethods.class);
    	when(levelController.getLevelControllerMethods()).thenReturn(lcm);
    	when(lcm.getGamePaused()).thenReturn(false);

    	SpriteBase spriteBase = mock(SpriteBase.class);
        when(spriteBase.getDy()).thenReturn(0.0);
    	player.setSpriteBase(spriteBase);
    	player.die();

    	timer.handle(1);

        assertEquals(0, player.getSpriteBase().getDy(), 0.1);
    	verify(spriteBase, never()).move();
    	verify(spriteBase, atLeastOnce()).setImage(anyString());
    }

    
    /**
     * This tests the function processInput.
     */
    @Test
     public void testProcessInput1() {
        SpriteBase spriteBase = mock(SpriteBase.class);
        when(spriteBase.getDy()).thenReturn(-5.0);
        player.setSpriteBase(spriteBase);
        player.setIsJumping(true);

        player.processInput();

        verify(spriteBase, atLeastOnce()).setDy(-5.0 + 0.6);
    }

    /**
     * This tests the second branch of processinput.
     */
    @Test
    public void testProcessInput2() {
        SpriteBase spriteBase = mock(SpriteBase.class);
        when(spriteBase.getDy()).thenReturn(5.0);
        player.setSpriteBase(spriteBase);
        player.setIsJumping(true);

        player.processInput();

        verify(spriteBase, atLeastOnce()).setDy(5.0 + 0.6);
        assertFalse(player.getIsJumping());
    }

    /**
     * This tests the function add powerup.
     */
    @Test
    public void testAddPowerup() {
        assertEquals(0, player.getPowerups().size());
        player.addPowerup(Immortality::new);
        assertEquals(1, player.getPowerups().size());
    }

    /**
     * This tests the function movecollisionchecker.
     */
    @Test
    public void testMoveCollisionChecker() {
        SpriteBase spriteBase = mock(SpriteBase.class);
        player.setSpriteBase(spriteBase);
        when(spriteBase.causesCollisionWall(anyDouble(), anyDouble(),
                anyDouble(), anyDouble(), any(LevelController.class))).thenReturn(false);

        when(spriteBase.getDy()).thenReturn(5.0);

        player.setAbleToJump(true);
        player.moveCollisionChecker(false, true);

        assertEquals(5.0, player.getSpriteBase().getDy(), 0.1);
        assertFalse(player.getAbleToJump());
    }

    /**
     * This tests the function movecollision checker when there is a collision.
     */
    @Test
    public void testMoveCollisionCheckerCollision() {
        SpriteBase spriteBase = mock(SpriteBase.class);
        player.setSpriteBase(spriteBase);
        when(spriteBase.causesCollisionWall(anyDouble(), anyDouble(),
                anyDouble(), anyDouble(), any(LevelController.class))).thenReturn(true);

        player.setAbleToJump(false);
        player.moveCollisionChecker(false, true);

        assertTrue(player.getAbleToJump());
    }

    /**
     * This tests the function causesBubbleCollision.
     */
    @Test
    public void testCausesBubbleCollision() {
        ArrayList<Bubble> bubbles = new ArrayList<>();
        Bubble bubble = mock(Bubble.class);
        bubbles.add(bubble);
        when(levelController.getBubbles()).thenReturn(bubbles);

        SpriteBase spriteBase = mock(SpriteBase.class);
        when(bubble.getSpriteBase()).thenReturn(spriteBase);
        when(spriteBase.causesCollision(anyDouble(), anyDouble(),
                anyDouble(), anyDouble())).thenReturn(true);
        when(bubble.isAbleToCatch()).thenReturn(false);

        assertTrue(player.causesBubbleCollision());
    }

    /**
     * This tests the function causes bubbleCollision without bubbles.
     */
    @Test
    public void testCausesBubbleCollisionNoBubbles() {
        assertFalse(player.causesBubbleCollision());
    }

    /**
     * This tests the function causesBubbleCollision without collision.
     */
    @Test
    public void testCausesBubbleCollisionNoCollision() {
        ArrayList<Bubble> bubbles = new ArrayList<>();
        Bubble bubble = mock(Bubble.class);
        bubbles.add(bubble);
        when(levelController.getBubbles()).thenReturn(bubbles);

        SpriteBase spriteBase = mock(SpriteBase.class);
        when(bubble.getSpriteBase()).thenReturn(spriteBase);
        when(spriteBase.causesCollision(anyDouble(), anyDouble(),
                anyDouble(), anyDouble())).thenReturn(false);
        when(bubble.isAbleToCatch()).thenReturn(false);

        assertFalse(player.causesBubbleCollision());
    }

    /**
     * This tests the function die.
     */
    @Test
    public void testDieMoreThanOneLife() {
        player = new Player(levelController, new Coordinates(0, 0, 0, 0, 0, 0),
                Settings.PLAYER_SPEED, 5, input, 1);
        player.die();
        assertTrue(player.getIsDelayed());
    }

    /**
     * This tests the function applygravity.
     */
    @Test
    public void testApplyGravity() {
        SpriteBase spriteBase = mock(SpriteBase.class);
        player.setSpriteBase(spriteBase);
        when(spriteBase.causesCollisionWall(anyDouble(), anyDouble(),
                anyDouble(), anyDouble(), any(LevelController.class))).thenReturn(true);
        player.setIsJumping(false);
        player.setAbleToDoubleJump(true);
        player.setAbleToJump(false);

        ArrayList<Bubble> bubbles = new ArrayList<>();
        Bubble bubble = mock(Bubble.class);
        bubbles.add(bubble);
        when(levelController.getBubbles()).thenReturn(bubbles);

        when(bubble.getSpriteBase()).thenReturn(spriteBase);
        when(spriteBase.causesCollision(anyDouble(), anyDouble(),
                anyDouble(), anyDouble())).thenReturn(true);
        when(bubble.isAbleToCatch()).thenReturn(false);

        player.applyGravity();

        assertTrue(player.getAbleToJump());
        assertFalse(player.getAbleToDoubleJump());
    }


    /**
     * This tests the function applygravity with collision.
     */
    @Test
    public void testApplyGravityCollision() {
        SpriteBase spriteBase = mock(SpriteBase.class);
        player.setSpriteBase(spriteBase);
        when(spriteBase.causesCollisionWall(anyDouble(), anyDouble(),
                anyDouble(), anyDouble(), any(LevelController.class))).thenReturn(false);
        player.setIsJumping(false);
        player.setAbleToDoubleJump(true);
        player.setAbleToJump(true);

        ArrayList<Bubble> bubbles = new ArrayList<>();
        Bubble bubble = mock(Bubble.class);
        bubbles.add(bubble);
        when(levelController.getBubbles()).thenReturn(bubbles);

        when(bubble.getSpriteBase()).thenReturn(spriteBase);
        when(spriteBase.causesCollision(anyDouble(), anyDouble(),
                anyDouble(), anyDouble())).thenReturn(false);
        when(bubble.isAbleToCatch()).thenReturn(false);

        player.applyGravity();

        assertFalse(player.getAbleToJump());
        assertTrue(player.getAbleToDoubleJump());
    }

    /**
     * This tests the function move vertical.
     */
    @Test
    public void testMoveVertical() {
        when(input.isMoveUp()).thenReturn(true);
        player.setAbleToJump(true);

        player.moveVertical();

        assertFalse(player.getAbleToJump());
        assertTrue(player.getIsJumping());
        assertEquals(-Settings.JUMP_SPEED, player.getSpriteBase().getDy(), 0.1);
    }

    /**
     * This tests the function checkfireprimary.
     */
    @Test
    public void testCheckFirePrimary() {
        when(input.isFirePrimaryWeapon()).thenReturn(true);
        player.setCounter(100);

        player.checkFirePrimary();

        verify(levelController, atLeastOnce()).addBubble(any(Bubble.class));
        assertEquals(0, player.getCounter());
    }

    /**
     * This tests the function addLife.
     */
    @Test
    public void testAddLife() {
        assertEquals(1, player.getLives());
        player.addLife();
        assertEquals(2, player.getLives());
    }

    /**
     * This tests the function factorSpeed.
     */
    @Test
    public void testFactorSpeed() {
        double speed = player.getSpeed();
        player.factorSpeed(3.0);
        assertEquals(speed * 3.0, player.getSpeed(), 0.1);
    }

    /**
     * This tests the function setSpeed.
     */
    @Test
    public void testSetSpeed() {
        player.setSpeed(10.0);
        assertEquals(10.0, player.getSpeed(), 0.1);
    }

    /**
     * This tests the function getLevelController.
     */
    @Test
    public void testGetLevelController() {
        assertEquals(levelController, player.getLevelController());
    }

    /**
     * This tests the function setLevelController.
     */
    @Test
    public void testSetLevelController() {
        LevelController mock = mock(LevelController.class);
        player.setLevelController(mock);
        assertEquals(mock, player.getLevelController());
    }

    /**
     * This tests the function setBubblepowerup.
     */
    @Test
    public void testSetBubblePowerup() {
        player.setBubblePowerup(true);
        assertTrue(player.getBubblePowerup());
    }

    /**
     * This tests the function setImageImmortal.
     */
    @Test
    public void setImageImmortal() {
        player.setFacingRight(true);
        player.setImmortal(true);

        player.setImage();
        assertEquals("Bub1RightImmortal.png", player.getSpriteBase().getImagePath());

        player.setFacingRight(false);

        player.setImage();
        assertEquals("Bub1LeftImmortal.png", player.getSpriteBase().getImagePath());
    }
    
    /**
     * This tests that the right death image is set when the player is facing right and they die.
     */
    @Test
    public void testDieImageRight() {
    	player.setFacingRight(true);
    	player.die();
    	assertEquals("Bub1RightDeath.png", player.getSpriteBase().getImagePath());
    }
    
    /**
     * This tests that the left death image is set when the player is facing left and they die.
     */
    @Test
    public void testDieImageLeft() {
    	player.setFacingRight(false);
    	player.die();
    	assertEquals("Bub1LeftDeath.png", player.getSpriteBase().getImagePath());
    }

}