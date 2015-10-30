package model.gameobject.player;

import controller.LevelController;
import controller.LevelControllerMethods;
import controller.ScreenController;
import javafx.animation.AnimationTimer;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This class tests the Powerup class.
 */
public class PowerupTest {

    private Powerup powerup;
    private LevelController levelController;
    private double destx = 10.0;
    private double desty = 10.0;

    /**
     * This is the set up function that is called before every test.
     */
    @Before
    public void setUp() {
        Settings.initialize("test.properties");

        levelController = mock(LevelController.class);
        ScreenController screenController = mock(ScreenController.class);
        when(levelController.getScreenController()).thenReturn(screenController);
        Coordinates coordinates = new Coordinates(0, 0, 0, 0, 0, 0);
        powerup = new Powerup(0, coordinates, destx, desty, levelController);
    }

    /**
     * Remove the properties file if it exists.
     */
    @After
    public void breakDown() {
        try {
            Files.delete(Paths.get("test.properties"));
        } catch (IOException e) {
            return;
        }
    }

    /**
     * This test tests the move() function.
     */
    @Test
    public void testMove() {
        assertEquals(0, powerup.getSpriteBase().getDxCoordinate(), 0.1);
        assertEquals(0, powerup.getSpriteBase().getDyCoordinate(), 0.1);
        assertFalse(powerup.isAbleToPickup());

        powerup.move();

        assertEquals((destx - powerup.getSpriteBase().getXCoordinate()) / 20.0,
                powerup.getSpriteBase().getDxCoordinate(), 0.1);
        assertEquals((desty - powerup.getSpriteBase().getYCoordinate()) / 20.0,
                powerup.getSpriteBase().getDyCoordinate(), 0.1);

        Coordinates coordinates = new Coordinates(0, 0, 0, 0, 0, 0);
        powerup = new Powerup(0, coordinates, 0, 0, levelController);
        powerup.move();

        assertEquals(0, powerup.getSpriteBase().getDxCoordinate(), 0.1);
        assertEquals(0, powerup.getSpriteBase().getDyCoordinate(), 0.1);
        assertTrue(powerup.isAbleToPickup());
    }

    /**
     * This function tests the causesCollision function.
     */
    @Test
    public void testCausesCollision() {
    	Coordinates coordinates = new Coordinates(1, 1, 0, 0, 0, 0);
        Player player = new Player(levelController, coordinates, 0, 1, mock(Input.class), 1);
        player.getSpriteBase().setHeight(10.0);
        player.getSpriteBase().setWidth(10.0);
        powerup.getSpriteBase().setHeight(10.0);
        powerup.getSpriteBase().setWidth(10.0);

        ScreenController screenController = mock(ScreenController.class);
        when(levelController.getScreenController()).thenReturn(screenController);

        powerup.setAbleToPickup(true);
        powerup.causesCollision(player);

        assertTrue(powerup.getPickedUp());
    }

    /**
     * This function tests the timer.
     */
    @Test
    public void testTimer() {
        AnimationTimer timer = powerup.createTimer();
        LevelControllerMethods lcm = mock(LevelControllerMethods.class);
        when(levelController.getLevelControllerMethods()).thenReturn(lcm);
        when(lcm.getGamePaused()).thenReturn(false);

        SpriteBase spriteBase = mock(SpriteBase.class);
        powerup.setSpriteBase(spriteBase);

        timer.handle(1);

        verify(spriteBase, atLeastOnce()).move();
    }

    /**
     * This function tests setCorrectImage.
     */
    @Test
    public void testSetCorrectImage() {
        powerup = new Powerup(Powerup.POWERUP_SPEED, mock(Coordinates.class),
                0, 0, levelController);
        assertEquals("banana.gif", powerup.getSpriteBase().getImagePath());

        powerup = new Powerup(Powerup.POWERUP_LIFE, mock(Coordinates.class),
                0, 0, levelController);
        assertEquals("heart.gif", powerup.getSpriteBase().getImagePath());

        powerup = new Powerup(Powerup.POWERUP_BUBBLE, mock(Coordinates.class),
                0, 0, levelController);
        assertEquals("apple.gif", powerup.getSpriteBase().getImagePath());

        powerup = new Powerup(Powerup.POWERUP_MONSTER, mock(Coordinates.class),
                0, 0, levelController);
        assertEquals("melon.png", powerup.getSpriteBase().getImagePath());

        powerup = new Powerup(Powerup.POWERUP_POINTS, mock(Coordinates.class),
                0, 0, levelController);
        assertEquals("coin.gif", powerup.getSpriteBase().getImagePath());
    }

    /**
     * This tests the function pickedUp.
     */
    @Test
    public void testPickedUp() {
        Player player = mock(Player.class);

        powerup = new Powerup(Powerup.POWERUP_SPEED, mock(Coordinates.class),
                0, 0, levelController);
        powerup.pickedUp(player);
//        verify(player, atLeastOnce()).addPowerup();

        powerup = new Powerup(Powerup.POWERUP_LIFE, mock(Coordinates.class),
                0, 0, levelController);
        powerup.pickedUp(player);
        verify(player, atLeastOnce()).addLife();

        powerup = new Powerup(Powerup.POWERUP_BUBBLE, mock(Coordinates.class),
                0, 0, levelController);
        powerup.pickedUp(player);
//        verify(player, atLeastOnce()).addPowerup(SuperBubble::new);

        powerup = new Powerup(Powerup.POWERUP_MONSTER, mock(Coordinates.class),
                0, 0, levelController);
        powerup.pickedUp(player);
//        verify(player, atLeastOnce()).addPowerup(SlowMonster::new);

        powerup = new Powerup(Powerup.POWERUP_POINTS, mock(Coordinates.class),
                0, 0, levelController);
        powerup.pickedUp(player);
        verify(player, atLeastOnce()).scorePoints(50);
    }



}
